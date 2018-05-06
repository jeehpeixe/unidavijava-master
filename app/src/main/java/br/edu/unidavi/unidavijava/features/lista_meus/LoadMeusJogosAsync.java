package br.edu.unidavi.unidavijava.features.lista_meus;

import android.os.AsyncTask;

import org.greenrobot.eventbus.EventBus;

import br.edu.unidavi.unidavijava.data.DatabaseHelper;
import br.edu.unidavi.unidavijava.data.Ordenacao;
import br.edu.unidavi.unidavijava.model.ListaMeuJogo;

public class LoadMeusJogosAsync extends AsyncTask<DatabaseHelper, Void, Void> {

    @Override
    public Void doInBackground(DatabaseHelper... databaseHelpers) {
        ListaMeuJogo lista = new ListaMeuJogo();
        //lista.setJogos(databaseHelpers[0].getAllMeusJogos(Ordenacao.NOME));
        lista.setJogos(databaseHelpers[0].getAllMeusJogos());
        EventBus.getDefault().post(lista);
        return null;
    }
}
