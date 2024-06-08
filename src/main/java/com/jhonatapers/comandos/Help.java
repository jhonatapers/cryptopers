package com.jhonatapers.comandos;

import com.jhonatapers.App;

public class Help extends ComandoTemplate {

    public Help(final App app) {
        super(app, Grupo.AJUDA, 0, "help", "Mostra comandos disponíveis");
    }

    @Override
    public void executar() {
        app.listarComandos();
    }

}
