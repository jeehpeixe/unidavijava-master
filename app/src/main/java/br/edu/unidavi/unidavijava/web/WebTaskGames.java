package br.edu.unidavi.unidavijava.web;

import android.content.Context;
import android.util.Log;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import br.edu.unidavi.unidavijava.data.DatabaseHelper;
import br.edu.unidavi.unidavijava.model.Jogo;
import br.edu.unidavi.unidavijava.model.ListaJogo;

/**
 * Created by thhen on 24/04/2018.
 */

public class WebTaskGames extends WebTaskBase {

    private static String SERVICE_URL = "games";
    private Context context;
    private DatabaseHelper db;

    public WebTaskGames(Context context){
        super(context, SERVICE_URL);
        this.context = context;
        db = new DatabaseHelper(context);
    }

    @Override
    public void handleResponse(String response) {

        List<Jogo> gamesList = new ArrayList<Jogo>();
        ListaJogo lista = new ListaJogo();

        try {
            JSONArray jsonArray = new JSONArray(response);
            JSONArray generos;
            String genero = "";
            Jogo game;
            for(int i = 0; i < jsonArray.length(); i++){

                JSONObject gameJSON = (JSONObject) jsonArray.get(i);
                Log.v("Jogo " + i, gameJSON.toString());
                game = new Jogo();
                game.setId(gameJSON.getInt("id"));
                game.setNome(gameJSON.getString("name"));
                game.setImageUrl(gameJSON.getString("imageUrl"));

                try {
                    game.setPlataforma(gameJSON.getString("platform"));
                }
                catch (JSONException ex) {
                    Log.v("Erro Plataforma", ex.getMessage());
                }

                genero = "";

                try {
                    generos = new JSONArray(gameJSON.getString("genres"));
                    for(int j = 0; j < generos.length(); j++){
                        if (genero.equals(""))
                            genero = (String) generos.get(j);
                        else
                            genero = ", " + (String) generos.get(j);
                    }
                    game.setGenero(genero);
                }
                catch (JSONException ex) {
                    Log.v("Erro Genero", ex.getMessage());
                }

                try {
                    game.setNota(gameJSON.getDouble("score"));
                }
                catch (JSONException ex) {
                    Log.v("Erro Score", ex.getMessage());
                }

                try {
                    game.setLancamento(gameJSON.getInt("year"));
                }
                catch (JSONException ex) {
                    Log.v("Erro Ano", ex.getMessage());
                }

                gamesList.add(game);
                db.createJogo(game);
            }
            EventBus.getDefault().post("RECARREGAR");
        }
        catch (JSONException e) {
            if(!isSilent()){
                Log.v("Erro JSON", e.getMessage());
            }
        }
    }

    @Override
    public String getRequestBody(){
        return  "";
    }
}
