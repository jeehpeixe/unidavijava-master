package br.edu.unidavi.unidavijava.features.lista_geral;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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

public class ListaGeralFragment extends Fragment {

    private RecyclerView recyclerView;
    private ListaGeralAdapter adapter;
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

        recyclerView = view.findViewById(R.id.recycler_list_games);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setHasFixedSize(true);

        adapter = new ListaGeralAdapter(getActivity(), new ArrayList<Jogo>());
        recyclerView.setAdapter(adapter);

        db      = new DatabaseHelper(getActivity());
        session = new SessionConfig(getContext());
        loader  = new LoadJogosAsync();

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
    }

    @Subscribe
    public void onEvent(ListaJogo gamesList){
        carregarLista(gamesList.getJogos());
    }

    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    public void onEvent(String message){
        if (message.equals("RECARREGAR")){
            loader.doInBackground(db);
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
        }
        else {
            getView().findViewById(R.id.lista_geral_empty_list_label).setVisibility(View.VISIBLE);
            getView().findViewById(R.id.recycler_list_games).setVisibility(View.INVISIBLE);
        }
    }
}