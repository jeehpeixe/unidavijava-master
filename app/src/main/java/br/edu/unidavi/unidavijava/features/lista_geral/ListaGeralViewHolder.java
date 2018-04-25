package br.edu.unidavi.unidavijava.features.lista_geral;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import br.edu.unidavi.unidavijava.R;

public class ListaGeralViewHolder extends RecyclerView.ViewHolder {

    ImageView thumbnail;
    TextView labelTitle;
    TextView labelPlatform;

    public ListaGeralViewHolder(View itemView) {
        super(itemView);
        thumbnail = itemView.findViewById(R.id.games_list_thumbnail);
        labelTitle = itemView.findViewById(R.id.games_list_game_title);
        labelPlatform = itemView.findViewById(R.id.games_list_game_platform);
    }

}
