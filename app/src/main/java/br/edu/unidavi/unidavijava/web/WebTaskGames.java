package br.edu.unidavi.unidavijava.web;

import android.content.Context;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import br.edu.unidavi.unidavijava.R;
import br.edu.unidavi.unidavijava.model.Jogo;

/**
 * Created by thhen on 24/04/2018.
 */

public class WebTaskGames extends WebTaskBase {

    private static String SERVICE_URL = "games";
    private Context context;

    public WebTaskGames(Context context){
        super(context, SERVICE_URL);
        this.context = context;
    }

    @Override
    public void handleResponse(String response) {

        List<Jogo> gamesList = new ArrayList<Jogo>();

        try {
            JSONArray jsonArray = new JSONArray(response);
            for(int i = 0; i < jsonArray.length(); i++){
                JSONObject gameJSON = (JSONObject) jsonArray.get(i);
                Jogo game = new Jogo();
                game.setNome(gameJSON.getString("name"));
                game.setPlataforma(gameJSON.getString("platform"));
                game.setImageUrl(gameJSON.getString("img"));
                gamesList.add(game);
            }
            EventBus.getDefault().post(gamesList);
        } catch (JSONException e) {
            if(!isSilent()){
                EventBus.getDefault().post(new Error(getContext().getString(R.string.label_error_invalid_response)));
            }
        }
    }

    @Override
    public String getRequestBody(){
        return  "";
    }
}
