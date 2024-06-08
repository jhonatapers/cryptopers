package com.jhonatapers.comandos;

public interface Comando {

    int ordinal();

    Grupo grupo();

    String key();

    String descricao();

    boolean aplicavel(String key);

    void executar();

}
