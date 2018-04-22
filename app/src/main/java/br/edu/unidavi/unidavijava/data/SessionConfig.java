package br.edu.unidavi.unidavijava.data;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by jessicapeixe
 */

public class SessionConfig {

    private final String CATEGORY_SESSION = "config";
    private final String FIELD_ANO_INICIO = "anoinicio";
    private final String FIELD_ANO_FINAL  = "anofinal";

    private SharedPreferences sharedPreferences;

    public SessionConfig(Context context){
        sharedPreferences = context.getSharedPreferences(CATEGORY_SESSION, Context.MODE_PRIVATE);
    }

    public void saveAnoFinalInSession(String anoFinal){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(FIELD_ANO_FINAL, anoFinal);
        editor.commit();
    }

    public void saveAnoInicioInSession(String anoInicio){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(FIELD_ANO_INICIO, anoInicio);
        editor.commit();
    }

    public String getAnoInicioInSession(){
        return sharedPreferences.getString(FIELD_ANO_INICIO,"");
    }

    public String getAnoFinalInSession(){
        return sharedPreferences.getString(FIELD_ANO_FINAL,"");
    }
}
