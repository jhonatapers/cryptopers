package com.jhonatapers;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.math.BigInteger;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.regex.Pattern;

import com.jhonatapers.CriptoAssimetrico.ChavePrivada;
import com.jhonatapers.CriptoAssimetrico.ChavePublica;
import com.jhonatapers.comandos.Assinar;
import com.jhonatapers.comandos.CifrarAES;
import com.jhonatapers.comandos.CifrarComChave;
import com.jhonatapers.comandos.Comando;
import com.jhonatapers.comandos.ConverterTextoParaHex;
import com.jhonatapers.comandos.DecifrarAES;
import com.jhonatapers.comandos.GerarChaveSimetrica;
import com.jhonatapers.comandos.GerarParChavesAssimetricas;
import com.jhonatapers.comandos.Grupo;
import com.jhonatapers.comandos.Help;
import com.jhonatapers.comandos.ImportarChavePrivada;
import com.jhonatapers.comandos.ImportarChavePublica;
import com.jhonatapers.comandos.InverterTexto;
import com.jhonatapers.comandos.Sair;
import com.jhonatapers.comandos.VerificarAssinatura;

public final class App implements Runnable {

    private final boolean fakeLoading;

    private final BufferedReader reader;

    private final List<Comando> comandos;

    public App(boolean fakeLoading) {

        this.fakeLoading = fakeLoading;

        printarLogo();

        reader = new BufferedReader(new InputStreamReader(System.in));

        this.comandos = List.of(
                new ImportarChavePublica(this),
                new ImportarChavePrivada(this),
                new Help(this),
                new Sair(this),
                new InverterTexto(this),
                new GerarParChavesAssimetricas(this),
                new GerarChaveSimetrica(this),
                new Assinar(this),
                new VerificarAssinatura(this),
                new CifrarComChave(this),
                new DecifrarAES(this),
                new CifrarAES(this),
                new ConverterTextoParaHex(this));
    }

    @Override
    public void run() {

        try {

            while (true) {
                String comando = reader.readLine();
                comandos
                        .stream()
                        .filter(c -> c.aplicavel(comando))
                        .forEach(Comando::executar);
            }

        } catch (Exception e) {
            e.printStackTrace();
            System.exit(0);
        }
    }

    private String inputString() {
        try {
            return reader.readLine();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static void fakeLoading(int iteracoes, int limiteTempoPorIteracao) {
        int barLength = 50;
        Random random = new Random();

        for (int i = 0; i <= iteracoes; i++) {

            int progress = (i * barLength) / iteracoes;

            StringBuilder bar = new StringBuilder("[");
            for (int j = 0; j < barLength; j++) {
                if (j < progress) {
                    bar.append("=");
                } else {
                    bar.append(" ");
                }
            }
            bar.append("]");

            int percent = (i * 100) / iteracoes;
            System.out.print("\rCarregando... " + bar + " " + percent + "%");

            try {
                Thread.sleep(random.nextInt(limiteTempoPorIteracao));
            } catch (InterruptedException e) {
                continue;
            }
        }

        System.out.print("\r");
    }

    private void printarLogo() {

        if (fakeLoading)
            fakeLoading(30, 300);

        System.out.println(ResourceUtils.lerArquivoTexto("logo"));
    }

    public void cifrarComChave() {
        System.out.println("Digite o valor em hexadecimal do texto claro:");
        String textoClaro = inputString();

        BigInteger textoCifrado = CriptoAssimetrico.cifrar(
                HexUtils.hexToBigInteger(textoClaro),
                formularioEscolherChavePublica());

        if (fakeLoading)
            fakeLoading(100, 5);

        System.out.println("\n\nTextoCifrado=[%s]".formatted(textoCifrado.toString(16)));
    }

    public void cifrarComAES() {
        System.out.println("Digite o valor em hexa da mensagem:");
        String mensagem = inputString();

        ChaveSimetrica chaveSimetrica = null;

        System.out.println("Buscar chave cadastrada? [S,n]");
        if (formularioSimOuNao())
            chaveSimetrica = formularioEscolherChaveSimetrica();
        else
            chaveSimetrica = novaChaveSemetrica();

        System.out.println("Digite o tamanho do IV:");
        Integer tamanhoIV = inputInteger();

        if (fakeLoading)
            fakeLoading(100, 5);

        System.out.println("\n\nMensagem=[%s]".formatted(AES.cifrar(
                HexUtils.hexToByteArray(mensagem),
                chaveSimetrica.getBytes(),
                tamanhoIV)));
    }

    public void converterStringParaHex() {
        System.out.println("Digite o valor do texto:");
        String texto = inputString();

        
        System.out.println("Valor em hexadecimal:\n" + HexUtils.toHexString(texto));

    }

    public void decifrarAES() {
        System.out.println("Digite o valor em hexa da mensagem:");
        String mensagem = inputString();

        ChaveSimetrica chaveSimetrica = null;

        System.out.println("Buscar chave cadastrada? [S,n]");
        if (formularioSimOuNao())
            chaveSimetrica = formularioEscolherChaveSimetrica();
        else {
            System.out.println("Digite o valor em hexa da chave:");
            chaveSimetrica = new ChaveSimetrica(inputString());
        }

        System.out.println("Digite o tamanho do IV:");
        Integer tamanhoIV = inputInteger();

        if (fakeLoading)
            fakeLoading(100, 5);

        System.out.println("\n\nMensagem=[%s]".formatted(AES.decifrar(
                HexUtils.hexToByteArray(mensagem),
                chaveSimetrica.getBytes(),
                tamanhoIV)));
    }

    public void gerarChaveSimetrica() {

        ChaveSimetrica chaveSimetrica = novaChaveSemetrica();

        System.out.println("\nSalvar chave gerada? [n/S]");
        if (formularioSimOuNao()) {
            String alias = alias();
            if (ResourceUtils.verificaSeArquivoExisteEmResources("%s.key.json".formatted(alias))) {
                System.out.println("\nAlias já cadastrado, deseja sobrescrever chave? [n/S]");
                if (!formularioSimOuNao())
                    return;
            }

            salvarChaveSimetrica(alias, chaveSimetrica);
        }

    }

    private Integer inputInteger() {
        try {
            return Integer.parseInt(reader.readLine());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void assinar() {

        System.out.println("Digite o valor em hexa da mensagem:");
        final String mensagem = inputString();

        BigInteger assinatura = CriptoAssimetrico.assinar(HexUtils.hexToBigInteger(mensagem),
                formularioEscolherChavePrivada());

        System.out.println("\nAssinatura=[%s]".formatted(assinatura.toString(16)));

    }

    public void verificarAssinatura() {

        System.out.println("Digite o valor em hexa da mensagem:");
        final String mensagem = inputString();

        System.out.println("Digite o valor em hexa da assiantura:");
        final String assinatura = inputString();
        
        boolean passou = CriptoAssimetrico.verificarAssinatura(
                HexUtils.hexToBigInteger(mensagem),
                HexUtils.hexToBigInteger(assinatura),
                formularioEscolherChavePublica());

        System.out.println(String.format("\n\nAssinado=[%s]", passou));
    }

    private ChavePrivada formularioEscolherChavePrivada() {

        ChavePrivada chavePrivada = null;

        System.out.println("Buscar chave privada já importada? [S,n]");
        if (formularioSimOuNao()) {
            do {
                Optional<ChavePrivada> optionalChavePrivada = buscarChavePrivada(alias());

                if (optionalChavePrivada.isPresent())
                    chavePrivada = optionalChavePrivada.get();
                else {

                    System.out.println("Chave não encontrada, deseja buscar novamente? [S,n]");
                    if (formularioSimOuNao())
                        continue;
                    else {
                        chavePrivada = novaChavePrivada();
                        System.out.println("Salvar nova chave privada? [S,n]");

                        if (formularioSimOuNao())
                            salvarChavePrivada(alias(), chavePrivada);
                    }
                }

            } while (chavePrivada == null);
        } else {
            chavePrivada = novaChavePrivada();
            System.out.println("Salvar nova chave privada? [S,n]");

            if (formularioSimOuNao())
                salvarChavePrivada(alias(), chavePrivada);
        }

        return chavePrivada;
    }

    private ChavePublica formularioEscolherChavePublica() {

        ChavePublica chavePublica = null;

        System.out.println("Buscar chave pública já importada? [S,n]");
        if (formularioSimOuNao()) {
            do {
                Optional<ChavePublica> optionalChavePrivada = buscarChavePublica(alias());

                if (optionalChavePrivada.isPresent())
                    chavePublica = optionalChavePrivada.get();
                else {

                    System.out.println("Chave não encontrada, deseja buscar novamente? [S,n]");
                    if (formularioSimOuNao())
                        continue;
                    else {
                        chavePublica = novaChavePublica();
                        System.out.println("Salvar nova chave pública? [S,n]");

                        if (formularioSimOuNao())
                            salvarChavePublica(alias(), chavePublica);
                    }
                }

            } while (chavePublica == null);
        } else {
            chavePublica = novaChavePublica();
            System.out.println("Salvar nova chave pública? [S,n]");

            if (formularioSimOuNao())
                salvarChavePublica(alias(), chavePublica);
        }

        return chavePublica;

    }

    private ChaveSimetrica formularioEscolherChaveSimetrica() {

        ChaveSimetrica chaveSimetrica = null;

        System.out.println("Buscar chave simetrica já importada? [S,n]");
        if (formularioSimOuNao()) {
            do {
                Optional<ChaveSimetrica> optionalChavePrivada = buscarChaveSimetrica(alias());

                if (optionalChavePrivada.isPresent())
                    chaveSimetrica = optionalChavePrivada.get();
                else {

                    System.out.println("Chave não encontrada, deseja buscar novamente? [S,n]");
                    if (formularioSimOuNao())
                        continue;
                    else {
                        chaveSimetrica = novaChaveSemetrica();
                        System.out.println("Salvar nova chave simetrica? [S,n]");

                        if (formularioSimOuNao())
                            salvarChaveSimetrica(alias(), chaveSimetrica);
                    }
                }

            } while (chaveSimetrica == null);
        } else {
            chaveSimetrica = novaChaveSemetrica();
            System.out.println("Salvar nova chave pública? [S,n]");

            if (formularioSimOuNao())
                salvarChaveSimetrica(alias(), chaveSimetrica);
        }

        return chaveSimetrica;

    }

    private Optional<ChavePrivada> buscarChavePrivada(String alias) {

        ChavePrivada chavePrivada = ResourceUtils.lerObjetoAPartirDeUmJson(ChavePrivada.class,
                "%s.priv.json".formatted(alias));

        return chavePrivada == null ? Optional.empty() : Optional.of(chavePrivada);

    }

    private Optional<ChavePublica> buscarChavePublica(String alias) {

        ChavePublica chavePublica = ResourceUtils.lerObjetoAPartirDeUmJson(ChavePublica.class,
                "%s.pub.json".formatted(alias));

        return chavePublica == null ? Optional.empty() : Optional.of(chavePublica);

    }

    private Optional<ChaveSimetrica> buscarChaveSimetrica(String alias) {

        ChaveSimetrica chaveSimetrica = ResourceUtils.lerObjetoAPartirDeUmJson(ChaveSimetrica.class,
                "%s.key.json".formatted(alias));

        return chaveSimetrica == null ? Optional.empty() : Optional.of(chaveSimetrica);

    }

    public void inverterTexto() {
        System.out.println("Digite o texto:");
        System.out.println(String.format("\nResultado\n%s", StringUtils.inverter(inputString())));
    }

    public void fechar() {
        System.out.println("Encerrando...");
        System.exit(0);
    }

    public void listarComandos() {

        int keyDeMaiorTamanho = comandos
                .stream()
                .map(Comando::key)
                .mapToInt(String::length)
                .max()
                .orElse(0) + 3;

        comandos
                .stream()
                .map(Comando::grupo)
                .distinct()
                .sorted(Comparator.comparingInt(Grupo::getOrdinal))
                .forEachOrdered(grupo -> listarComandosDeUmGrupo(grupo, keyDeMaiorTamanho));

    }

    private void listarComandosDeUmGrupo(final Grupo grupo, int keyDeMaiorTamanho) {

        System.out.println("|\n|-[%s]".formatted(grupo.getNome()));

        comandos
                .stream()
                .filter(comando -> grupo.equals(comando.grupo()))
                .sorted(Comparator.comparingInt(Comando::ordinal))
                .map(comando -> formatarComando(comando, keyDeMaiorTamanho))
                .forEachOrdered(System.out::println);

    }

    public void gerarParChavesAsimetricas() {

        CriptoAssimetrico criptoAssimetrico = new CriptoAssimetrico(1000);

        System.out.println("Digite o valor do tamanho em bits dos fatores primos:");
        criptoAssimetrico.gerarParDeChaves(inputInteger());
        System.out.println("\nPar de Chaves:\n\n%s".formatted(criptoAssimetrico.toString()));

        System.out.println("\n\nDeseja salvar par de chaves? [S/n]");
        if (formularioSimOuNao()) {
            String alias = alias();
            final String arquivoChavePrivada = salvarChavePrivada(alias, criptoAssimetrico.chavePrivada());
            final String arquivoChavePublica = salvarChavePublica(alias, criptoAssimetrico.chavePublica());

            System.out.println("\nPar de chaves armazenado");

            printarChaves(List.of(arquivoChavePrivada, arquivoChavePublica));
        }

    }

    private boolean formularioSimOuNao() {
        String resposta;
        do {
            resposta = inputString();
            if (resposta.equalsIgnoreCase("s"))
                return true;
            if (resposta.equalsIgnoreCase("n"))
                return false;
        } while (!(resposta.equalsIgnoreCase("s") || resposta.equalsIgnoreCase("n")));
        return false;
    }

    private String formatarComando(Comando comando, int keyDeMaiorTamanho) {
        return String.format("|--- %-" + keyDeMaiorTamanho + "s|-(%s)", comando.key(),
                comando.descricao());
    }

    public void importarChavePublica() {

        try {
            String alias = alias();

            if (ResourceUtils.verificaSeArquivoExisteEmResources("%s.pub.json".formatted(alias))) {
                System.out.println("\n\nAlias já cadastrado, deseja sobrescrever chave? [n/S]");
                if (!formularioSimOuNao())
                    return;
            }

            salvarChavePublica(alias, novaChavePublica());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    public void importarChavePrivada() {

        try {
            String alias = alias();

            if (ResourceUtils.verificaSeArquivoExisteEmResources("%s.priv.json".formatted(alias))) {
                System.out.println("\n\nAlias já cadastrado, deseja sobrescrever chave? [n/S]");
                if (!formularioSimOuNao())
                    return;
            }

            salvarChavePrivada(alias, novaChavePrivada());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    private String alias() {
        System.out.println("\nDigite um alias:");
        String alias = inputString();

        final Pattern ALIAS_INVALIDO = Pattern.compile("[\\\\/:*?\"<>|\\s.]+");

        if (ALIAS_INVALIDO.matcher(alias).find()) {
            do {
                System.out.println(
                        "\n\nErro: O alias contém caracteres inválidos ou espaços.\nUm alias válido não deve conter nenhum dos seguintes caracteres: \\\\ / : * ? \" < > |");
                System.out.println("\n\nDigite um alias válido:");
                alias = inputString();
            } while (ALIAS_INVALIDO.matcher(alias).find());
        }

        return alias;
    }

    private ChavePublica novaChavePublica() {
        System.out.println("Digite o valor 'e' em hexadecimal da chave pública:");
        String e = inputString();

        System.out.println("Digite o valor 'N' em hexadecimal da chave pública:");
        String N = inputString();

        return new ChavePublica(HexUtils.hexToBigInteger(e), HexUtils.hexToBigInteger(N));
    }

    private ChavePrivada novaChavePrivada() {
        System.out.println("Digite o valor 'd' em hexadecimal da chave privada:");
        String da = inputString();

        System.out.println("Digite o valor 'N' em hexadecimal da chave privada:");
        String Na = inputString();

        return new ChavePrivada(HexUtils.hexToBigInteger(da), HexUtils.hexToBigInteger(Na));
    }

    private ChaveSimetrica novaChaveSemetrica() {

        ChaveSimetrica chaveSimetrica;

        System.out.println("Gerar uma chave aleatória? [S,n]");
        if (formularioSimOuNao()) {
            System.out.println("Digite o valor do tamanho em bits do tamanho da chave:");
            chaveSimetrica = ChaveSimetrica.gerar(inputInteger());

            if (fakeLoading)
                fakeLoading(100, 5);

            System.out.println("Gerada\n");
            System.out.println(chaveSimetrica);
        } else {
            System.out.println("Digite o valor em hexa da chave:");
            chaveSimetrica = new ChaveSimetrica(inputString());
        }

        return chaveSimetrica;
    }

    private String salvarChaveSimetrica(String alias, ChaveSimetrica chaveSimetrica) {

        if (fakeLoading)
            fakeLoading(100, 5);

        return ResourceUtils.salvarObjetoComoJson(chaveSimetrica, "%s.key".formatted(alias));
    }

    private String salvarChavePublica(String alias, ChavePublica chavePublica) {

        if (fakeLoading)
            fakeLoading(100, 5);

        return ResourceUtils.salvarObjetoComoJson(chavePublica, "%s.pub".formatted(alias));
    }

    private String salvarChavePrivada(String alias, ChavePrivada chavePrivada) {

        if (fakeLoading)
            fakeLoading(100, 5);

        return ResourceUtils.salvarObjetoComoJson(chavePrivada, "%s.priv".formatted(alias));
    }

    private void printarChaves(List<String> chaves) {
        int maxLength = chaves.stream()
                .mapToInt(String::length)
                .max()
                .orElse(0) + 3;

        chaves.stream()
                .map(a -> formatarVisualizacaoArquivoChave(a, maxLength))
                .forEach(System.out::println);
    }

    private String formatarVisualizacaoArquivoChave(String nomeArquivoChave, int maxLength) {
        return String.format(
                "%-" + maxLength + "s| Alias=[%s]",
                nomeArquivoChave,
                removerExtensaoDoNomeDoArquivo(nomeArquivoChave));
    }

    private static String removerExtensaoDoNomeDoArquivo(String fileName) {
        int indexOcorrencia = fileName.indexOf('.');
        return fileName.substring(0, indexOcorrencia == -1 ? 0 : indexOcorrencia);
    }

}
