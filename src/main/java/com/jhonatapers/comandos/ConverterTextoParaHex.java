package com.jhonatapers.comandos;

import com.jhonatapers.App;

public class ConverterTextoParaHex extends ComandoTemplate {

    public ConverterTextoParaHex(App app) {
        super(app, Grupo.OUTROS, 800, "string-to-hex", "Converte String para hexadecimal");
    }

    @Override
    public void executar() {
        app.converterStringParaHex();
    }

}
