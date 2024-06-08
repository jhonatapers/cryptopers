package com.jhonatapers.comandos;

import com.jhonatapers.App;

public class InverterTexto extends ComandoTemplate {

    public InverterTexto(App app) {
        super(app, Grupo.OUTROS, 0, "reverse-text", "Inverte texto");
    }

    @Override
    public void executar() {
        app.inverterTexto();
    }

}
