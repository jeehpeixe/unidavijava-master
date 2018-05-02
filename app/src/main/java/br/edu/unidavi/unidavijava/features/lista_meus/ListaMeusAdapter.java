package br.edu.unidavi.unidavijava.features.lista_meus;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.squareup.picasso.Picasso;

import java.util.List;

import br.edu.unidavi.unidavijava.R;
import br.edu.unidavi.unidavijava.model.MeuJogo;

public class ListaMeusAdapter extends RecyclerView.Adapter<ListaMeusViewHolder> {

    Context context;
    List<MeuJogo> meusGames;

    public ListaMeusAdapter(Context context, List<MeuJogo> meusGames){
        this.context = context;
        this.meusGames = meusGames;
    }

    @Override
    public ListaMeusViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.lista_geral_item, parent, false);

        ListaMeusViewHolder myViewHolder = new ListaMeusViewHolder(v);

        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(ListaMeusViewHolder holder, int position) {

        final MeuJogo myGame = meusGames.get(position);

        holder.labelTitle.setText(myGame.getNome());
        holder.labelPlatform.setText(myGame.getPlataforma().toUpperCase());

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
        return meusGames.size();
    }


}
