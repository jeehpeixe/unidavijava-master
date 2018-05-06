package br.edu.unidavi.unidavijava.features.lista_geral;

import android.os.AsyncTask;

import org.greenrobot.eventbus.EventBus;

import br.edu.unidavi.unidavijava.data.DatabaseHelper;
import br.edu.unidavi.unidavijava.data.Ordenacao;
import br.edu.unidavi.unidavijava.model.ListaJogo;

public class LoadJogosAsync extends AsyncTask<DatabaseHelper, Void, Void> {

    private Ordenacao ordem;
    private Integer inicioFiltro;
    private Integer fimFiltro;

    @Override
    public Void doInBackground(DatabaseHelper... databaseHelpers) {
        ListaJogo lista = new ListaJogo();
        //lista.setJogos(databaseHelpers[0].getAllJogos(this.ordem, this.inicioFiltro, this.fimFiltro));
        lista.setJogos(databaseHelpers[0].getAllJogos());
        EventBus.getDefault().post(lista);
        return null;
    }

    public void setOrdem(Ordenacao ordenacao){
        this.ordem = ordenacao;
    }

    public void setInicioFiltro(Integer inicio){
        this.inicioFiltro = inicio;
    }

    public void setFimFiltro(Integer fim){
        this.fimFiltro = fim;
    }
}
