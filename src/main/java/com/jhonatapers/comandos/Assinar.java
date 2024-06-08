package com.jhonatapers.comandos;

import com.jhonatapers.App;

public class Assinar extends ComandoTemplate {

    public Assinar(App app) {
        super(app, Grupo.CRIPT, 2, "sign", "Assinar (utilizando chave privada)");
    }

    @Override
    public void executar() {
        app.assinar();
    }

}
