package br.edu.unidavi.unidavijava.features.lista_meus;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import br.edu.unidavi.unidavijava.R;
import br.edu.unidavi.unidavijava.data.DatabaseHelper;
import br.edu.unidavi.unidavijava.features.detalhe.DetalheActivity;
import br.edu.unidavi.unidavijava.features.lista_geral.ListaGeralViewHolder;
import br.edu.unidavi.unidavijava.model.MeuJogo;

public class ListaMeusAdapter extends RecyclerView.Adapter<ListaGeralViewHolder> {

    private Context context;
    public List<MeuJogo> meusGames;
    private LoadMeusJogosAsync loader;
    private DatabaseHelper db;
    private boolean artificioTecnico = false;

    public ListaMeusAdapter(Context context, List<MeuJogo> meusGames){
        this.context   = context;
        this.meusGames = meusGames;
        this.db        = new DatabaseHelper(context);
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

        final MeuJogo game = meusGames.get(position);

        holder.labelTitle.setText(game.getNome());
        holder.labelPlatform.setText(game.getPlataforma().toUpperCase());

        Picasso.with(context)
               .load(game.getImageUrl())
               .placeholder(R.drawable.ic_videogame_asset_black_24dp)
               .into(holder.thumbnail);

        final ImageView botaoAdd = holder.botaoAddToMeusJogos;
        Picasso.with(context).load(R.drawable.ic_clear_black_24dp).placeholder(R.drawable.ic_clear_black_24dp).error(R.drawable.ic_clear_black_24dp).into(botaoAdd);

        holder.botaoAddToMeusJogos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            int gameId = game.getId();

            if (db.deleteMeuJogo(gameId) > 0) {
                Snackbar.make(v, String.format("O Jogo %s foi removido da sua lista!", game.getNome()), Snackbar.LENGTH_LONG).show();
                loader = new LoadMeusJogosAsync();
                loader.doInBackground(db);
                EventBus.getDefault().postSticky(new String("RECARREGAR"));
            }
            else {
                Snackbar.make(v, String.format("O Jogo %s não pode ser eliminado de sua lista!", game.getNome()), Snackbar.LENGTH_LONG).show();
            }

            artificioTecnico = true;
            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            if (!artificioTecnico) {
                Intent i = new Intent(context, DetalheActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                i.putExtra("gameid", game.getId());
                v.getContext().startActivity(i);
            }
            else {
                artificioTecnico = false;
            }
            }
        });

    }

    @Override
    public int getItemCount() {
        return meusGames.size();
    }

}
