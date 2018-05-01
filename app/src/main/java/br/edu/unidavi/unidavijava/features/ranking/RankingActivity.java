package br.edu.unidavi.unidavijava.features.ranking;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

import br.edu.unidavi.unidavijava.R;
import br.edu.unidavi.unidavijava.data.DatabaseHelper;

public class RankingActivity extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.tela_ranking, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();

        DatabaseHelper data = new DatabaseHelper(getContext());
        List<Float> ranking = data.getInfoRanking();

        TextView viewByIdTenho = getView().findViewById(R.id.text_tenho_qtde);
        viewByIdTenho.setText(String.valueOf(Math.round(ranking.get(0))));

        TextView viewByIdJoguei = getView().findViewById(R.id.text_joguei_qtde);
        viewByIdJoguei.setText(String.valueOf(Math.round(ranking.get(1))));

        TextView viewByIdZerei = getView().findViewById(R.id.text_zerei_qtde);
        viewByIdZerei.setText(String.valueOf(Math.round(ranking.get(2))));

        TextView viewByIdQuero = getView().findViewById(R.id.text_quero_qtde);
        viewByIdQuero.setText(String.valueOf(Math.round(ranking.get(3))));

        Locale ptBr = new Locale("pt", "BR");
        TextView viewByIdGastei = getView().findViewById(R.id.text_gastei_qtde);
        viewByIdGastei.setText(NumberFormat.getCurrencyInstance(ptBr).format(ranking.get(4)));
    }
}
