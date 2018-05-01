package br.edu.unidavi.unidavijava.features.lista_meus;

import android.os.AsyncTask;

import org.greenrobot.eventbus.EventBus;

import br.edu.unidavi.unidavijava.data.DatabaseHelper;
import br.edu.unidavi.unidavijava.data.Ordenacao;

public class LoadMeusJogosAsync extends AsyncTask<DatabaseHelper, Void, Void> {

    @Override
    protected Void doInBackground(DatabaseHelper... databaseHelpers) {
        EventBus.getDefault().post(databaseHelpers[0].getAllMeusJogos(Ordenacao.NOME));
        return null;
    }
}
