package br.edu.unidavi.unidavijava.data;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by jessicapeixe
 */

public class Session {

    private final String FIELD_USERNAME = "username";
    private final String FIELD_TOKEN = "token";
    private final String CATEGORY_SESSION = "session";

    private SharedPreferences sharedPreferences;

    public Session(Context context){
        sharedPreferences = context.getSharedPreferences(CATEGORY_SESSION, Context.MODE_PRIVATE);
    }

    public void saveEmailInSession(String emailValue){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(FIELD_USERNAME, emailValue);
        editor.commit();
    }

    public String getEmailInSession(){
        return sharedPreferences.getString(FIELD_USERNAME,"");
    }

    public void saveSenhaInSession(String tokenValue){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(FIELD_TOKEN, tokenValue);
        editor.commit();
    }

    public String getSenhaInSession(){
        return sharedPreferences.getString(FIELD_TOKEN,"");
    }

}