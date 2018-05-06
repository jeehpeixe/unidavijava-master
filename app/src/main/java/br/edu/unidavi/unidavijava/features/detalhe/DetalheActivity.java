package br.edu.unidavi.unidavijava.features.detalhe;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RatingBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.greenrobot.eventbus.EventBus;

import java.text.NumberFormat;
import java.util.Locale;

import br.edu.unidavi.unidavijava.R;
import br.edu.unidavi.unidavijava.data.DatabaseHelper;
import br.edu.unidavi.unidavijava.model.Jogo;
import br.edu.unidavi.unidavijava.model.MeuJogo;

public class DetalheActivity extends AppCompatActivity {

    private Jogo jogo;
    private MeuJogo meuJogo = null;
    private DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tela_detalhe);

        this.db = new DatabaseHelper(getApplicationContext());
        this.jogo = db.getJogo(getIntent().getExtras().getInt("gameid", 1)).get(0);

        TextView nomeDoJogoTextView = findViewById(R.id.nome_jogo);
        nomeDoJogoTextView.setText(this.jogo.getNome());

        ImageView imagemDoJogoImageView = findViewById(R.id.imagem_jogo);
        Picasso.with(getApplicationContext()).load(this.jogo.getImageUrl()).placeholder(R.drawable.bomberman).into(imagemDoJogoImageView);

        TextView plataformaDoJogoTextView = findViewById(R.id.txtPlataforma);
        plataformaDoJogoTextView.setText(jogo.getPlataforma());

        TextView lancamentoDoJogoTextView = findViewById(R.id.txtLancamento);
        lancamentoDoJogoTextView.setText(jogo.getLancamento().toString());

        TextView generoDoJogoTextView = findViewById(R.id.txtGenero);
        generoDoJogoTextView.setText(jogo.getGenero());


        RadioButton rdTenho = findViewById(R.id.rdTenho);
        RadioButton rdQuero = findViewById(R.id.rdQuero);
        CheckBox ckJoguei   = findViewById(R.id.ckJoguei);
        CheckBox ckZerei    = findViewById(R.id.ckZerei);
        CheckBox ckFisico   = findViewById(R.id.ckFisico);
        RatingBar ratingBar = findViewById(R.id.ratingBar);

        final EditText pagueiEditText = findViewById(R.id.input_paguei);

        if(db.getMeuJogo(jogo.getId()).size() > 0){
            meuJogo = db.getMeuJogo(jogo.getId()).get(0);
            rdTenho.setChecked(meuJogo.isTenho());
            rdQuero.setChecked(meuJogo.isQuero());
            ckJoguei.setChecked(meuJogo.isJoguei());
            ckZerei.setChecked(meuJogo.isZerei());
            ckFisico.setChecked(meuJogo.isFisico());
            ratingBar.setRating(meuJogo.getNotaPessoal());

            if(meuJogo.getPaguei() > 0) {
                pagueiEditText.setText(String.format("%.2f", meuJogo.getPaguei()));
            }
        }
        else {
            meuJogo = new MeuJogo();
            meuJogo.setId(jogo.getId());
        }

        /* Events */
        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                meuJogo.setNotaPessoal(rating);
                EventBus.getDefault().postSticky(new String("RECARREGAR"));
            }
        });

        rdTenho.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                meuJogo.setTenho(isChecked);
                db.createMeuJogo(meuJogo);
                EventBus.getDefault().postSticky(new String("RECARREGAR"));
            }
        });
        rdQuero.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                meuJogo.setQuero(isChecked);
                db.createMeuJogo(meuJogo);
                EventBus.getDefault().postSticky(new String("RECARREGAR"));
            }
        });
        ckJoguei.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                meuJogo.setJoguei(isChecked);
                db.createMeuJogo(meuJogo);
                EventBus.getDefault().postSticky(new String("RECARREGAR"));
            }
        });
        ckZerei.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                meuJogo.setZerei(isChecked);
                db.createMeuJogo(meuJogo);
                EventBus.getDefault().postSticky(new String("RECARREGAR"));
            }
        });
        ckFisico.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                meuJogo.setFisico(isChecked);
                db.createMeuJogo(meuJogo);
                EventBus.getDefault().postSticky(new String("RECARREGAR"));
            }
        });

        pagueiEditText.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (pagueiEditText.getText().toString().length() > 0) {
                    try {
                        meuJogo.setPaguei(Float.parseFloat(pagueiEditText.getText().toString()));
                        db.createMeuJogo(meuJogo);
                    } catch (java.lang.NumberFormatException nfe) {
                    }
                    EventBus.getDefault().postSticky(new String("RECARREGAR"));
                }
                return false;
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
    }
}
