package com.jhonatapers.comandos;

import com.jhonatapers.App;

public class DecifrarAES extends ComandoTemplate {

    public DecifrarAES(App app) {
        super(app, Grupo.CRIPT, 4, "decrypt-aes", "Decifrar (utilizando AES)");
    }

    @Override
    public void executar() {
        app.decifrarAES();
    }

}
