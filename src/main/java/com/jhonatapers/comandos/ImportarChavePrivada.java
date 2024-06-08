package com.jhonatapers.comandos;

import com.jhonatapers.App;

public class ImportarChavePrivada extends ComandoTemplate {

    public ImportarChavePrivada(final App app) {
        super(app, Grupo.IMPORTACAO, 1, "import-priv", "Importar chave privada");
    }

    @Override
    public void executar() {
        app.importarChavePrivada();
    }

}
