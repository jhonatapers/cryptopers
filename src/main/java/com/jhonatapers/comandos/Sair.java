package com.jhonatapers.comandos;

import com.jhonatapers.App;

public class Sair extends ComandoTemplate {

    public Sair(App app) {
        super(app, Grupo.OUTROS, 999, "sair", "Encerra execução");
    }

    @Override
    public void executar() {
        app.fechar();
    }

}
