package br.edu.unidavi.unidavijava.data;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by jessicapeixe
 */

public class SessionConfig {

    private final String CATEGORY_SESSION       = "config";
    private final String FIELD_ANO_INICIO       = "anoinicio";
    private final String FIELD_ANO_FINAL        = "anofinal";
    private final String FIELD_FILTRO_TITULO    = "filtrotitulo";
    private final String FIELD_FILTRO_DATA      = "filtrodata";
    private final String FIELD_FILTRO_CATEGORIA = "filtrocategoria";

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

    public void saveOrdemTituloInSession(Boolean checked) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(FIELD_FILTRO_TITULO, checked);
        editor.commit();
    }

    public void saveOrdemDataInSession(Boolean checked) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(FIELD_FILTRO_DATA, checked);
        editor.commit();
    }

    public void saveOrdemCategoriaInSession(Boolean checked) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(FIELD_FILTRO_CATEGORIA, checked);
        editor.commit();
    }

    public boolean getOrdemDataInSession() {
        return sharedPreferences.getBoolean(FIELD_FILTRO_DATA, false);
    }

    public boolean getOrdemTituloInSession() {
        return sharedPreferences.getBoolean(FIELD_FILTRO_TITULO, false);
    }

    public boolean getOrdemCategoriaInSession() {
        return sharedPreferences.getBoolean(FIELD_FILTRO_CATEGORIA, false);
    }
}
