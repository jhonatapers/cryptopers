package com.jhonatapers.comandos;

import com.jhonatapers.App;

public abstract class ComandoTemplate implements Comando {

    protected final App app;

    private final int ordinal;

    private final Grupo grupo;

    private final String key;

    private final String descricao;

    public ComandoTemplate(App app, Grupo grupo, int ordinal, String key, String descricao) {
        this.app = app;
        this.grupo = grupo;
        this.ordinal = ordinal;
        this.key = key;
        this.descricao = descricao;
    }

    @Override
    public int ordinal() {
        return Integer.parseInt("%s%s".formatted(grupo.getOrdinal(), ordinal));
    }

    @Override
    public boolean aplicavel(String key) {
        return this.key.equals(key);
    }

    @Override
    public Grupo grupo() {
        return grupo;
    }

    @Override
    public String key() {
        return key;
    }

    @Override
    public String descricao() {
        return descricao;
    }

}
