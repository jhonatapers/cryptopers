package com.jhonatapers.comandos;

import com.jhonatapers.App;

public class ImportarChavePublica extends ComandoTemplate {

    public ImportarChavePublica(final App app) {
        super(app, Grupo.IMPORTACAO, 0, "import-pub", "Importar chave pública");
    }

    @Override
    public void executar() {
        app.importarChavePublica();
    }

}
