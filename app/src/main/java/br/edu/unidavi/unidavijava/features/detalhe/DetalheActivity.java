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
import android.widget.TextView;
import com.squareup.picasso.Picasso;
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

        TextView plataformaDoJogoTextView = findViewById(R.id.lblPlataforma);
        plataformaDoJogoTextView.append(" " + jogo.getPlataforma());

        TextView lancamentoDoJogoTextView = findViewById(R.id.lblLancamento);
        lancamentoDoJogoTextView.append(" " + jogo.getLancamento());

        TextView generoDoJogoTextView = findViewById(R.id.lblGenero);
        generoDoJogoTextView.append(" " + jogo.getPlataforma());


        RadioButton rdTenho = findViewById(R.id.rdTenho);
        RadioButton rdQuero = findViewById(R.id.rdQuero);
        CheckBox ckJoguei = findViewById(R.id.ckJoguei);
        CheckBox ckZerei = findViewById(R.id.ckZerei);
        CheckBox ckFisico = findViewById(R.id.ckFisico);
        final EditText pagueiEditText = findViewById(R.id.input_paguei);

        if(db.getMeuJogo(jogo.getId()).size() > 0){
            meuJogo = db.getMeuJogo(jogo.getId()).get(0);
            rdTenho.setChecked(meuJogo.isTenho());
            rdQuero.setChecked(meuJogo.isQuero());
            ckJoguei.setChecked(meuJogo.isJoguei());
            ckZerei.setChecked(meuJogo.isZerei());
            ckFisico.setChecked(meuJogo.isFisico());
            if(meuJogo.getPaguei() > 0) {
                pagueiEditText.setText(meuJogo.getPaguei().toString());
            }
        } else {
            meuJogo = new MeuJogo();
            meuJogo.setId(jogo.getId());
        }

        /* Events */
        rdTenho.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                meuJogo.setTenho(isChecked);
                db.createMeuJogo(meuJogo);
            }
        });
        rdQuero.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                meuJogo.setQuero(isChecked);
                db.createMeuJogo(meuJogo);
            }
        });
        ckJoguei.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                meuJogo.setJoguei(isChecked);
                db.createMeuJogo(meuJogo);
            }
        });
        ckZerei.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                meuJogo.setZerei(isChecked);
                db.createMeuJogo(meuJogo);
            }
        });
        ckFisico.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                meuJogo.setFisico(isChecked);
                db.createMeuJogo(meuJogo);
            }
        });

        pagueiEditText.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                try {
                    meuJogo.setPaguei(Float.parseFloat(pagueiEditText.getText().toString()));
                    db.createMeuJogo(meuJogo);
                }catch(java.lang.NumberFormatException nfe){
                    meuJogo.setPaguei(0f);
                    db.createMeuJogo(meuJogo);
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
