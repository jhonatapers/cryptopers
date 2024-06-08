package com.jhonatapers.comandos;

public enum Grupo {
    CRIPT("Cript", 0),
    IMPORTACAO("Importaçao", 1),
    GERACAO("Geração", 2),
    AJUDA("Ajuda", 3),
    OUTROS("Outros", 4);

    private final String nome;

    private final int ordinal;

    Grupo(String nome, int ordinal) {
        this.nome = nome;
        this.ordinal = ordinal;
    }

    public String getNome() {
        return nome;
    }

    public int getOrdinal() {
        return ordinal;
    }
}
