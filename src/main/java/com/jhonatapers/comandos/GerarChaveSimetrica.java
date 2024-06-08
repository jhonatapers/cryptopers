package com.jhonatapers.comandos;

import com.jhonatapers.App;

public class GerarChaveSimetrica extends ComandoTemplate {

    public GerarChaveSimetrica(App app) {
        super(app, Grupo.GERACAO, 2, "gen-sym", "Gerar chave simetrica");
    }

    @Override
    public void executar() {
        app.gerarChaveSimetrica();
    }

}
