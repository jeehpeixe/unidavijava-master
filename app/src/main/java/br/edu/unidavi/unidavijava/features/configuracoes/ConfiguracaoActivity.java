package br.edu.unidavi.unidavijava.features.configuracoes;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;


import org.greenrobot.eventbus.EventBus;

import br.edu.unidavi.unidavijava.R;
import br.edu.unidavi.unidavijava.data.DatabaseHelper;
import br.edu.unidavi.unidavijava.data.Session;
import br.edu.unidavi.unidavijava.data.SessionConfig;
import br.edu.unidavi.unidavijava.features.lista_meus.LoadMeusJogosAsync;
import br.edu.unidavi.unidavijava.features.login.LoginActivity;

public class ConfiguracaoActivity extends Fragment {

    private AlertDialog alerta;

    private EditText editAnoInicial;
    private EditText editAnoFinal;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.tela_configuracoes, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        editAnoInicial = getView().findViewById(R.id.input_ano_inicio);
        editAnoFinal   = getView().findViewById(R.id.input_ano_final);

        SessionConfig session = new SessionConfig(getContext());
        editAnoInicial.setText(session.getAnoInicioInSession());
        editAnoFinal.setText(session.getAnoFinalInSession());

        RadioButton ordemData = getView().findViewById(R.id.radio_ordenacao_data);
        ordemData.setChecked(session.getOrdemDataInSession());

        RadioButton ordemTitulo = getView().findViewById(R.id.radio_ordenacao_titulo);
        ordemTitulo.setChecked(session.getOrdemTituloInSession());

        RadioButton ordemCategoria = getView().findViewById(R.id.radio_ordenacao_categoria);
        ordemCategoria.setChecked(session.getOrdemCategoriaInSession());

        adicionaOnFocusChangeAno(editAnoInicial);
        adicionaOnFocusChangeAno(editAnoFinal);
        adicionaOnChangeOrdenacao();
        adicionaOnClickSair();
        adicionaOnClickResetarMeusJogos();
    }

    private void adicionaOnFocusChangeAno(EditText edit){
        final EditText editAno= edit;
        editAno.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            public void onFocusChange(View v, boolean hasFocus) {

                if (!hasFocus && !editAno.getText().toString().isEmpty()){

                    int iAno = Integer.parseInt(editAno.getText().toString());

                    if (iAno < 1950 || iAno > 2050) {
                        editAno.setText("");

                        AlertDialog alerta = new AlertDialog.Builder(getActivity()).create();

                        alerta.setCancelable(true);
                        alerta.setCanceledOnTouchOutside(true);
                        alerta.setTitle("Ano Inválido");
                        alerta.setMessage("O ano deve estar entre 1900 e 2100");
                        alerta.show();
                        alerta.setOnCancelListener(new DialogInterface.OnCancelListener() {
                            @Override
                            public void onCancel(DialogInterface dialog) {
                                editAno.requestFocus();
                            }
                        });
                    }
                    else {
                        gravaFiltroAnoSessao(editAno);
                    }
                }
            }
        });
    }

    private void gravaFiltroAnoSessao(EditText editAno){

        SessionConfig session = new SessionConfig(getContext());

        if (editAno.getId() == getView().findViewById(R.id.input_ano_inicio).getId()) {
            session.saveAnoInicioInSession(editAno.getText().toString());
        }
        else {
            session.saveAnoFinalInSession(editAno.getText().toString());
        }
        EventBus.getDefault().post("RECARREGAR");
    }

    private void adicionaOnChangeOrdenacao(){
        final RadioButton ordemTitulo    = getView().findViewById(R.id.radio_ordenacao_titulo);
        final RadioButton ordemData      = getView().findViewById(R.id.radio_ordenacao_data);
        final RadioButton ordemCategoria = getView().findViewById(R.id.radio_ordenacao_categoria);

        ordemTitulo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SessionConfig session = new SessionConfig(getContext());
                session.saveOrdemTituloInSession(ordemTitulo.isChecked());
                session.saveOrdemDataInSession(ordemData.isChecked());
                session.saveOrdemCategoriaInSession(ordemCategoria.isChecked());
                EventBus.getDefault().post("RECARREGAR");
                EventBus.getDefault().post("RECARREGARMEUS");
            }
        });

        ordemData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SessionConfig session = new SessionConfig(getContext());
                session.saveOrdemTituloInSession(ordemTitulo.isChecked());
                session.saveOrdemDataInSession(ordemData.isChecked());
                session.saveOrdemCategoriaInSession(ordemCategoria.isChecked());
                EventBus.getDefault().post("RECARREGAR");
                EventBus.getDefault().post("RECARREGARMEUS");
            }
        });

        ordemCategoria.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SessionConfig session = new SessionConfig(getContext());
                session.saveOrdemTituloInSession(ordemTitulo.isChecked());
                session.saveOrdemDataInSession(ordemData.isChecked());
                session.saveOrdemCategoriaInSession(ordemCategoria.isChecked());
                EventBus.getDefault().post("RECARREGAR");
                EventBus.getDefault().post("RECARREGARMEUS");
            }
        });

    }

    private void adicionaOnClickSair(){

        Button botaoSair = getView().findViewById(R.id.button_sair);

        botaoSair.setOnClickListener(
            new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    limpaLoginCache();
                    backToLogin();
                }
            }
        );
    }

    private void backToLogin() {
        Intent intent = new Intent(getContext(), LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);
        getActivity().finish();
    }

    private void limpaLoginCache(){
        Session session = new Session(getContext());
        session.saveEmailInSession("");
        session.saveSenhaInSession("");
    }

    private void adicionaOnClickResetarMeusJogos(){
        Button botaoResetar = getView().findViewById(R.id.button_reset);

        botaoResetar.setOnClickListener(
            new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    limpaMeusJogos();
                }
            }
        );
    }

    private void limpaMeusJogos(){

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Atenção!");
        builder.setMessage("Tem certeza que deseja resetar todos os seus jogos?");

        builder.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface arg0, int arg1) {
                DatabaseHelper db = new DatabaseHelper(getContext());
                db.limparMeusJogos();

                LoadMeusJogosAsync loader = new LoadMeusJogosAsync();
                loader.doInBackground(db);
            }
        });

        builder.setNegativeButton("Não", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface arg0, int arg1) {}
        });
        alerta = builder.create();
        alerta.show();
    }
}