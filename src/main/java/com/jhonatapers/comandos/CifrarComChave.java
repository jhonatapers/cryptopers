package com.jhonatapers.comandos;

import com.jhonatapers.App;

public class CifrarComChave extends ComandoTemplate {

    public CifrarComChave(App app) {
        super(app, Grupo.CRIPT, 3, "encrypt", "Cifrar (utilizando chave)");
    }

    @Override
    public void executar() {
        app.cifrarComChave();
    }

}
