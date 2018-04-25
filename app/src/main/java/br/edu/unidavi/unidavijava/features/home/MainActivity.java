package br.edu.unidavi.unidavijava.features.home;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import br.edu.unidavi.unidavijava.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(getResources().getString(R.string.app_name));
        setSupportActionBar(toolbar);

        final TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);

        // Criando as abas
        tabLayout.addTab(tabLayout.newTab().setText("Jogos"));
        tabLayout.addTab(tabLayout.newTab().setText("Meus"));
        tabLayout.addTab(tabLayout.newTab().setText("Opções"));
        tabLayout.addTab(tabLayout.newTab().setText("Ranking"));

        // Adicionando os icones nas abas
        tabLayout.getTabAt(0).setIcon(R.drawable.ic_gamepad_black_24dp);
        tabLayout.getTabAt(1).setIcon(R.drawable.ic_videogame_black_24dp);
        tabLayout.getTabAt(2).setIcon(R.drawable.ic_settings_black_24dp);
        tabLayout.getTabAt(3).setIcon(R.drawable.ic_chart_black_24dp);

        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        //Alterando a aba ao clicar no item do menu
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                ViewPager mPager = (ViewPager) findViewById(R.id.page_viewer);
                mPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {}

            @Override
            public void onTabReselected(TabLayout.Tab tab) {}
        });

        // Instanciando o ViewPager
        ViewPager viewPager = (ViewPager) findViewById(R.id.page_viewer);

        // Descomentar essa área assim que criar os fragmentos e adicioná-los no PageAdapter
        PageAdapter pageAdapter = new PageAdapter(getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(pageAdapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
    }
}
