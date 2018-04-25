package br.edu.unidavi.unidavijava.features.lista_geral;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.squareup.picasso.Picasso;

import java.util.List;

import br.edu.unidavi.unidavijava.R;
import br.edu.unidavi.unidavijava.model.Jogo;

public class ListaGeralAdapter extends RecyclerView.Adapter<ListaGeralViewHolder> {

    Context context;
    List<Jogo> gamesList;

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

        final Jogo myGame = gamesList.get(position);

        holder.labelTitle.setText(myGame.getNome());
        holder.labelPlatform.setText(myGame.getPlataforma().replaceAll("/.+", "").toUpperCase());

        Picasso.with(context)
                .load(myGame.getImageUrl())
                .placeholder(R.drawable.ic_videogame_asset_black_24dp)
                .into(holder.thumbnail);

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
