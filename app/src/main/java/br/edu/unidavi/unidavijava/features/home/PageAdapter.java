package br.edu.unidavi.unidavijava.features.home;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import br.edu.unidavi.unidavijava.features.configuracoes.ConfiguracaoActivity;
import br.edu.unidavi.unidavijava.features.lista_geral.ListaGeralFragment;
import br.edu.unidavi.unidavijava.features.lista_meus.ListaMeusFragment;
import br.edu.unidavi.unidavijava.features.ranking.RankingActivity;

public class PageAdapter extends FragmentPagerAdapter {

    private int numOfTabs;

    public PageAdapter(FragmentManager fm, int numOfTabs) {
        super(fm);
        this.numOfTabs = numOfTabs;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new ListaGeralFragment();
            case 1:
                return new ListaMeusFragment();
            case 2:
                return new ConfiguracaoActivity();
            case 3:
                return new RankingActivity();
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return numOfTabs;
    }
}
