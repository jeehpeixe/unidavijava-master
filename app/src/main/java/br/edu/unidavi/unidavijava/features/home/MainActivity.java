package br.edu.unidavi.unidavijava.features.home;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import org.greenrobot.eventbus.Subscribe;

import br.edu.unidavi.unidavijava.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("LIFECYCLE", "CREATE");
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("LIFECYCLE", "RESUME");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d("LIFECYCLE", "PAUSE");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d("LIFECYCLE", "RESTART");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d("LIFECYCLE", "DESTROY");
    }

    @Subscribe
    public void onEvent(Error error){
        Snackbar.make(findViewById(R.id.container), error.getMessage(), Snackbar.LENGTH_LONG).show();
    }

}
