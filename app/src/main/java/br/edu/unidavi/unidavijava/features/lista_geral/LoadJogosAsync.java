package br.edu.unidavi.unidavijava.features.lista_geral;

import android.os.AsyncTask;

import org.greenrobot.eventbus.EventBus;

import br.edu.unidavi.unidavijava.data.DatabaseHelper;
import br.edu.unidavi.unidavijava.data.Ordenacao;
import br.edu.unidavi.unidavijava.model.ListaJogo;

public class LoadJogosAsync extends AsyncTask<DatabaseHelper, Void, Void> {

    @Override
    public Void doInBackground(DatabaseHelper... databaseHelpers) {
        ListaJogo lista = new ListaJogo();
        lista.setJogos(databaseHelpers[0].getAllJogos(Ordenacao.NOME));
        EventBus.getDefault().post(lista);
        return null;
    }
}
