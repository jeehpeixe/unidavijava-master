package br.edu.unidavi.unidavijava.features.lista_geral;

import android.content.Context;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import br.edu.unidavi.unidavijava.R;
import br.edu.unidavi.unidavijava.data.DatabaseHelper;
import br.edu.unidavi.unidavijava.data.Ordenacao;
import br.edu.unidavi.unidavijava.features.lista_meus.LoadMeusJogosAsync;
import br.edu.unidavi.unidavijava.model.Jogo;
import br.edu.unidavi.unidavijava.model.MeuJogo;

public class ListaGeralAdapter extends RecyclerView.Adapter<ListaGeralViewHolder> {

    private Context context;
    public List<Jogo> gamesList;
    private LoadMeusJogosAsync loader;
    private DatabaseHelper db;

    public ListaGeralAdapter(Context context, List<Jogo> gamesList){
        this.context = context;
        this.gamesList = gamesList;
        this.db = new DatabaseHelper(context);
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
        holder.labelPlatform.setText(game.getPlataforma() != null ? game.getPlataforma().toUpperCase() : "");

        Picasso.with(context)
                .load(game.getImageUrl())
                .placeholder(R.drawable.ic_videogame_asset_black_24dp)
                .into(holder.thumbnail);

        final ImageView botaoAdd = holder.botaoAddToMeusJogos;
        Picasso.with(context).load(R.drawable.ic_add_black_24dp).placeholder(R.drawable.ic_add_black_24dp).error(R.drawable.ic_add_black_24dp).into(botaoAdd);

        if (db.getMeuJogo(game.getId()).size() > 0) {
            Picasso.with(context).load(R.drawable.ic_clear_black_24dp).placeholder(R.drawable.ic_clear_black_24dp).error(R.drawable.ic_clear_black_24dp).into(botaoAdd);
        }

        holder.botaoAddToMeusJogos.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                if (db.getMeuJogo(game.getId()).size() == 0) {
                    MeuJogo meuJogo = new MeuJogo();
                    meuJogo.setId(game.getId());
                    meuJogo.setTenho(true);
                    if (db.createMeuJogo(meuJogo) > 0) {
                        Snackbar.make(v, String.format("O Jogo %s foi adicionado a sua lista!", game.getNome()), Snackbar.LENGTH_LONG).show();
                        Picasso.with(context).load(R.drawable.ic_clear_black_24dp).placeholder(R.drawable.ic_clear_black_24dp).error(R.drawable.ic_clear_black_24dp).into(botaoAdd);
                    } else {
                        Snackbar.make(v, String.format("Erro ao adicionar o jogo na sua lista!", game.getNome()), Snackbar.LENGTH_LONG).show();
                    }
                } else {
                    if (db.deleteMeuJogo(game.getId()) > 0) {
                        Snackbar.make(v, String.format("O Jogo %s foi removido da sua lista!", game.getNome()), Snackbar.LENGTH_LONG).show();
                        Picasso.with(context).load(R.drawable.ic_add_black_24dp).placeholder(R.drawable.ic_add_black_24dp).error(R.drawable.ic_add_black_24dp).into(botaoAdd);
                    }
                }
                loader = new LoadMeusJogosAsync();
                loader.doInBackground(db);

                return false;
            }
        });
    }

    @Override
    public int getItemCount() {
        return gamesList.size();
    }


}
