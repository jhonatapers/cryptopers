package com.jhonatapers;

import java.io.Serializable;
import java.math.BigInteger;
import java.security.SecureRandom;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public final class CriptoAssimetrico {

    private final int maximoTentativas;

    public CriptoAssimetrico(int maximoTentativas) {
        this.maximoTentativas = maximoTentativas;
    }

    private BigInteger p;

    private BigInteger q;

    private BigInteger L;

    private BigInteger e;

    private BigInteger d;

    private BigInteger N;

    public void gerarParDeChaves(int tamanhoBitsDosPrimos) {

        p = BigInteger.probablePrime(tamanhoBitsDosPrimos, new SecureRandom());
        q = BigInteger.probablePrime(tamanhoBitsDosPrimos, new SecureRandom());

        N = p.multiply(q);

        L = Euler.calcular(p, q);

        int tentativas = 0;
        BigInteger _e;
        do {

            tentativas++;

            _e = new BigInteger(1024, new SecureRandom());

            if (_e.bitLength() < 1024)
                continue;

            if (possuiInverso(_e, L)) {
                e = _e;

                d = e.modInverse(L);
                break;
            }

        } while (tentativas < maximoTentativas);

    }

    private static boolean possuiInverso(final BigInteger _e, final BigInteger L) {
        return Euclides.mdc(_e, L).compareTo(BigInteger.ONE) == 0;
    }

    public static BigInteger cifrar(BigInteger textoClaro, ChavePublica chavePublica) {
        return textoClaro.modPow(chavePublica.e, chavePublica.N);
    }

    public static BigInteger assinar(BigInteger mensagem, ChavePrivada chavePrivada) {
        return new BigInteger(1, HashUtils.sha256(HexUtils.hexToByteArray(mensagem.toString(16)))).modPow(chavePrivada.d, chavePrivada.N);
    }

    public static boolean verificarAssinatura(BigInteger mensagem, BigInteger assinatura, ChavePublica chavePublica) {

        BigInteger hc = new BigInteger(1, HashUtils.sha256(HexUtils.hexToByteArray(mensagem.toString(16))));        
        return assinatura.modPow(chavePublica.e, chavePublica.N).compareTo(hc) == 0;
    }

    public ChavePublica chavePublica() {

        if (null == e || null == N)
            return null;

        return new ChavePublica(e, N);
    }

    public ChavePrivada chavePrivada() {

        if (null == e || null == p || null == q)
            return null;

        return new ChavePrivada(d, p.multiply(q));
    }

    public String toString() {
        return "---------------------------\n%s\n%s".formatted(
                chavePrivada().toString(),
                chavePublica().toString());
    }

    public static final class ChavePrivada implements Serializable {

        private final BigInteger d;

        private final BigInteger N;

        @JsonCreator
        public ChavePrivada(@JsonProperty("d") final BigInteger d,
                @JsonProperty("N") final BigInteger N) {
            this.d = d;
            this.N = N;
        }

        public BigInteger getD() {
            return this.d;
        }

        @JsonProperty("N")
        public BigInteger getN() {
            return this.N;
        }

        public String toString() {
            return "-----  CHAVE-PRIVADA  -----\nd=[%s]\nN=[%s]\n---------------------------"
                    .formatted(this.d.toString(16), this.N.toString(16));
        }

    }

    public static final class ChavePublica implements Serializable {

        @JsonProperty("e")
        private final BigInteger e;

        @JsonProperty("N")
        private final BigInteger N;

        @JsonCreator
        public ChavePublica(@JsonProperty("e") final BigInteger e, @JsonProperty("N") final BigInteger N) {
            this.e = e;
            this.N = N;
        }

        @JsonProperty("e")
        public BigInteger getE() {
            return this.e;
        }

        @JsonProperty("N")
        public BigInteger getN() {
            return this.N;
        }

        public String toString() {
            return "-----  CHAVE-PUBLICA  -----\ne=[%s]\nN=[%s]\n---------------------------".formatted(e.toString(16),
                    N.toString(16));
        }

    }

}
