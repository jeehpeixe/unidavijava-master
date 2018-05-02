package br.edu.unidavi.unidavijava.features.lista_meus;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import br.edu.unidavi.unidavijava.R;
import br.edu.unidavi.unidavijava.data.DatabaseHelper;
import br.edu.unidavi.unidavijava.model.MeuJogo;

public class ListaMeusFragment extends Fragment {

    private RecyclerView recyclerView;
    private ListaMeusAdapter adapter;
    private DatabaseHelper db;

    public ListaMeusFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_aba_lista_meus, container, false);

        recyclerView = view.findViewById(R.id.lista_meus_recyclerview);
        recyclerView.setLayoutManager(
                new LinearLayoutManager(getActivity()));
        recyclerView.setHasFixedSize(true);

        adapter = new ListaMeusAdapter(getActivity(), new ArrayList<MeuJogo>());
        recyclerView.setAdapter(adapter);

        db = new DatabaseHelper(getActivity());

        LoadMeusJogosAsync loader = new LoadMeusJogosAsync();
        loader.doInBackground(db);

        return view;
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
    public void onEvent(List<MeuJogo> gamesList){
        if (gamesList.size() > 0) {
            if (gamesList.get(0).getClass().getName().equals("br.edu.unidavi.unidavijava.model.MeuJogo")) {
                carregarLista(gamesList);
            }
        }
    }

    public void carregarLista(List<MeuJogo> gamesList){
        if(gamesList.size() != 0) {
            getView().findViewById(R.id.lista_meus_recyclerview).setVisibility(View.VISIBLE);
            getView().findViewById(R.id.lista_meus_empty_list_label).setVisibility(View.INVISIBLE);
            adapter.meusGames = gamesList;
            adapter.notifyDataSetChanged();
        } else {
            getView().findViewById(R.id.lista_meus_empty_list_label).setVisibility(View.VISIBLE);
            getView().findViewById(R.id.lista_meus_recyclerview).setVisibility(View.INVISIBLE);
        }
    }


}
