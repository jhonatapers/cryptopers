package com.jhonatapers.comandos;

import com.jhonatapers.App;

public class GerarParChavesAssimetricas extends ComandoTemplate {

    public GerarParChavesAssimetricas(App app) {
        super(app, Grupo.GERACAO, 0, "gen-asy", "Gerar par de chaves assimétricas");
    }

    @Override
    public void executar() {
        app.gerarParChavesAsimetricas();
    }

}
