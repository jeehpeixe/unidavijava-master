package br.edu.unidavi.unidavijava.features.lista_geral;


import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import br.edu.unidavi.unidavijava.R;
import br.edu.unidavi.unidavijava.data.DatabaseHelper;
import br.edu.unidavi.unidavijava.data.Ordenacao;
import br.edu.unidavi.unidavijava.data.SessionConfig;
import br.edu.unidavi.unidavijava.model.Jogo;
import br.edu.unidavi.unidavijava.model.ListaJogo;
import br.edu.unidavi.unidavijava.web.WebTaskGames;

public class ListaGeralFragment extends Fragment {

    private RecyclerView recyclerView;
    private ListaGeralAdapter adapter;
    private ProgressDialog mDialog;
    private DatabaseHelper db;
    private LoadJogosAsync loader;
    private List<Jogo> listaCompletaGames;
    private SessionConfig session;

    public ListaGeralFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_aba_lista_geral, container, false);

        //WebTaskGames webTaskGames = new WebTaskGames(getActivity());
        //webTaskGames.execute();

        mDialog = new ProgressDialog(getActivity());
        mDialog.setMessage("Aguarde...");
        mDialog.setCancelable(false);
        mDialog.show();

        recyclerView = view.findViewById(R.id.recycler_list_games);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setHasFixedSize(true);

        adapter = new ListaGeralAdapter(getActivity(), new ArrayList<Jogo>());
        recyclerView.setAdapter(adapter);

        db = new DatabaseHelper(getActivity());

        session = new SessionConfig(getContext());

        loader = new LoadJogosAsync();
        /*
        loader.setOrdem(getOrdenacao());
        int filtro = 0;
        try {
            filtro = Integer.parseInt(session.getAnoInicioInSession());
        } catch (Exception e) {}
        loader.setInicioFiltro(filtro);
        try {
            filtro = Integer.parseInt(session.getAnoFinalInSession());
        } catch (Exception e) {}
        loader.setFimFiltro(filtro);
        */

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
        loader.doInBackground(db);
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe
    public void onEvent(Error error){
        Snackbar.make(getView(), error.getMessage(), Snackbar.LENGTH_LONG).show();
        //loader.doInBackground(db);
    }

    private Ordenacao getOrdenacao(){
        if (session.getOrdemCategoriaInSession()) {
            return Ordenacao.GENERO;
        }
        if (session.getOrdemDataInSession()) {
            return Ordenacao.DATA;
        }

        return Ordenacao.NOME;
    }

    @Subscribe
    public void onEvent(ListaJogo gamesList){
        //if(gamesList.getJogos().size() > 0) {
            carregarLista(gamesList.getJogos());
        //}
    }

    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    public void onEvent(String message){
        if (message.equals("RECARREGAR")){
            loader.doInBackground(db);
            //carregarLista(listaCompletaGames);
            EventBus.getDefault().removeStickyEvent(message);
        }
    }

    public void carregarLista(List<Jogo> gamesList){
        if(gamesList.size() != 0) {
            getView().findViewById(R.id.recycler_list_games).setVisibility(View.VISIBLE);
            getView().findViewById(R.id.lista_geral_empty_list_label).setVisibility(View.INVISIBLE);

            adapter.gamesList = new ArrayList<>();
            adapter.notifyDataSetChanged();

            adapter.gamesList = gamesList;
            adapter.notifyDataSetChanged();

            listaCompletaGames = gamesList;
        } else {
            getView().findViewById(R.id.lista_geral_empty_list_label).setVisibility(View.VISIBLE);
            getView().findViewById(R.id.recycler_list_games).setVisibility(View.INVISIBLE);
        }
        mDialog.dismiss();
    }
}