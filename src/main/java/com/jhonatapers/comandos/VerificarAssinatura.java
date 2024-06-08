package com.jhonatapers.comandos;

import com.jhonatapers.App;

public class VerificarAssinatura extends ComandoTemplate {

    public VerificarAssinatura(App app) {
        super(app, Grupo.CRIPT, 2, "verify", "verifica assinatura");
    }

    @Override
    public void executar() {
        app.verificarAssinatura();
    }

}
