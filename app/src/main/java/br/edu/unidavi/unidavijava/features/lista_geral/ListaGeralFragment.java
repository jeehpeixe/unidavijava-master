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
import android.widget.Toast;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import br.edu.unidavi.unidavijava.R;
import br.edu.unidavi.unidavijava.data.DatabaseHelper;
import br.edu.unidavi.unidavijava.data.Ordenacao;
import br.edu.unidavi.unidavijava.model.Jogo;
import br.edu.unidavi.unidavijava.web.WebTaskGames;


/**
 * A simple {@link Fragment} subclass.
 */
public class ListaGeralFragment extends Fragment {

    private RecyclerView recyclerView;
    private ListaGeralAdapter adapter;
    private ProgressDialog mDialog;
    private DatabaseHelper db;

    public ListaGeralFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_aba_lista_geral, container, false);

        WebTaskGames webTaskGames = new WebTaskGames(getActivity());
        webTaskGames.execute();

        mDialog = new ProgressDialog(getActivity());
        mDialog.setMessage("Aguarde...");
        mDialog.setCancelable(false);
        mDialog.show();

        recyclerView = view.findViewById(R.id.recycler_list_games);
        recyclerView.setLayoutManager(
                new LinearLayoutManager(getActivity()));
        recyclerView.setHasFixedSize(true);

        adapter = new ListaGeralAdapter(getActivity(), new ArrayList<Jogo>());
        recyclerView.setAdapter(adapter);

        db = new DatabaseHelper(getActivity());

        return view;
    }

    private void salvar(List<Jogo> jogoList) {

        for (Jogo jogo : jogoList) {
            db.createJogo(jogo);
        }

    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe
    public void onEvent(Error error){
        /*
        Toast.makeText(getActivity(), "Erro ao buscar lista de jogos...", Toast.LENGTH_SHORT).show();
        getView().findViewById(R.id.lista_geral_empty_list_label).setVisibility(View.VISIBLE);
        getView().findViewById(R.id.recycler_list_games).setVisibility(View.INVISIBLE);
        mDialog.dismiss();
        */
        Snackbar.make(getView(), error.getMessage(), Snackbar.LENGTH_LONG).show();

        // Recupar os jogos do banco
        List<Jogo> gamesList = db.getAllJogos(Ordenacao.NOME);

        carregarLista(gamesList);
    }

    @Subscribe
    public void onEvent(List<Jogo> gamesList){

        // Salavar os jogos na base de dados
        salvar(gamesList);
        carregarLista(gamesList);
    }

    public void carregarLista(List<Jogo> gamesList){
        if(gamesList.size() != 0) {
            getView().findViewById(R.id.recycler_list_games).setVisibility(View.VISIBLE);
            getView().findViewById(R.id.lista_geral_empty_list_label).setVisibility(View.INVISIBLE);
            adapter.gamesList = gamesList;
            adapter.notifyDataSetChanged();
        } else {
            getView().findViewById(R.id.lista_geral_empty_list_label).setVisibility(View.VISIBLE);
            getView().findViewById(R.id.recycler_list_games).setVisibility(View.INVISIBLE);
        }
        mDialog.dismiss();
    }


}
