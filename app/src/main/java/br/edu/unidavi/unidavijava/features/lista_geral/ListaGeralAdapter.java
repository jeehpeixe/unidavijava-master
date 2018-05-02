package br.edu.unidavi.unidavijava.features.lista_geral;

import android.content.Context;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import br.edu.unidavi.unidavijava.R;
import br.edu.unidavi.unidavijava.data.DatabaseHelper;
import br.edu.unidavi.unidavijava.features.lista_meus.LoadMeusJogosAsync;
import br.edu.unidavi.unidavijava.model.Jogo;
import br.edu.unidavi.unidavijava.model.MeuJogo;

public class ListaGeralAdapter extends RecyclerView.Adapter<ListaGeralViewHolder> {

    Context context;
    List<Jogo> gamesList;
    private LoadMeusJogosAsync loader;

    public ListaGeralAdapter(Context context, List<Jogo> gamesList){
        this.context = context;
        this.gamesList = gamesList;
    }

    @Override
    public ListaGeralViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.lista_geral_item, parent, false);

        ListaGeralViewHolder myViewHolder = new ListaGeralViewHolder(v);

        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(ListaGeralViewHolder holder, int position) {

        final Jogo game = gamesList.get(position);

        holder.labelTitle.setText(game.getNome());
        holder.labelPlatform.setText(game.getPlataforma().toUpperCase());

        Picasso.with(context)
                .load(game.getImageUrl())
                .placeholder(R.drawable.ic_videogame_asset_black_24dp)
                .into(holder.thumbnail);


        holder.botaoAddToMeusJogos.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                DatabaseHelper db = new DatabaseHelper(context);
                loader = new LoadMeusJogosAsync();

                MeuJogo meuJogo = new MeuJogo();
                meuJogo.setId(game.getId());
                meuJogo.setNome(game.getNome());
                meuJogo.setPlataforma(game.getPlataforma());
                meuJogo.setLancamento(game.getLancamento());
                meuJogo.setGenero(game.getGenero());
                meuJogo.setImageUrl(game.getImageUrl());
                meuJogo.setNota(game.getNota());
                meuJogo.setTenho(true);
                if (db.createMeuJogo(meuJogo) == 1){
                    Snackbar.make(v, String.format("O Jogo %s foi adicionado a sua lista!", game.getNome()), Snackbar.LENGTH_LONG).show();
                    EventBus.getDefault().post(new String("RELOAD"));
                } else {
                    Snackbar.make(v, String.format("Erro ao adicionar o jogo na sua lista!", game.getNome()), Snackbar.LENGTH_LONG).show();
                }
                loader.doInBackground(db);

                return false;
            }
        });


        /*holder.itemView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                Toast.makeText(context, view.toString(), Toast.LENGTH_SHORT).show();

                return true;
            }
        });*/

    }

    @Override
    public int getItemCount() {
        return gamesList.size();
    }


}
