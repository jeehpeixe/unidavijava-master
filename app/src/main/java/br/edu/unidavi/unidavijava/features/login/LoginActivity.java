package br.edu.unidavi.unidavijava.features.login;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import br.edu.unidavi.unidavijava.R;
import br.edu.unidavi.unidavijava.data.Session;
import br.edu.unidavi.unidavijava.features.home.MainActivity;
import br.edu.unidavi.unidavijava.model.User;
import br.edu.unidavi.unidavijava.web.WebTaskLogin;

public class LoginActivity extends AppCompatActivity {

    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Button buttonEnter = findViewById(R.id.button_enter);
        buttonEnter.setOnClickListener(
            new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    tryLogin();
                }
            }
        );

        Session session = new Session(this);

        EditText editTextEmail = findViewById(R.id.input_email);
        editTextEmail.setText(session.getEmailInSession());

        EditText editTextPassword = findViewById(R.id.input_password);
        editTextPassword.requestFocus();

        editTextEmail.requestFocus();

        if (session.getEmailInSession().length() > 0 && session.getSenhaInSession().length() > 0) {
            goToHome();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    public void tryLogin(){
        EditText inputEmail = findViewById(R.id.input_email);
        String emailValue = inputEmail.getText().toString();

        EditText inputPassword = findViewById(R.id.input_password);
        String passwordValue = inputPassword.getText().toString();

        showDialog();

        WebTaskLogin task = new WebTaskLogin(this, emailValue, passwordValue);
        task.execute();

    }

    @Subscribe
    public void onEvent(User user){
        hideDialog();
        Session session = new Session(this);
        session.saveEmailInSession(user.getEmail());
        session.saveSenhaInSession(user.getSenha());
        goToHome();
    }

    @Subscribe
    public void onEvent(Error error){
        hideDialog();
        Snackbar.make(findViewById(R.id.container), error.getMessage(), Snackbar.LENGTH_LONG).show();
    }

    private void goToHome() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);
        finish();
    }

    public void showDialog(){
        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage(getString(
                R.string.message_wait));
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setProgress(0);
        progressDialog.show();
    }

    public void hideDialog(){
        if(progressDialog != null && progressDialog.isShowing()){
            progressDialog.cancel();
        }
    }
}
