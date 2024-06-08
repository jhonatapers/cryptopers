package com.jhonatapers.comandos;

import com.jhonatapers.App;

public class CifrarAES extends ComandoTemplate {

    public CifrarAES(App app) {
        super(app, Grupo.CRIPT, 4, "encrypt-aes", "Cifrar (utilizando AES)");
    }

    @Override
    public void executar() {
        app.cifrarComAES();
    }

}
