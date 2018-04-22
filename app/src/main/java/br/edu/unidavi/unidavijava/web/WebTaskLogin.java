package br.edu.unidavi.unidavijava.web;

import android.content.Context;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

import br.edu.unidavi.unidavijava.R;
import br.edu.unidavi.unidavijava.model.User;

/**
 * Created by jessicapeixe
 */

public class WebTaskLogin extends WebTaskBase{

    private static final String SERVICE_NAME = "login";

    private String email;
    private String senha;

    public WebTaskLogin(Context context, String email, String senha) {
        super(context, SERVICE_NAME);
        this.email = email;
        this.senha = getSenhaCriptografada(senha);
    }

    private String getSenhaCriptografada(String senha){

        MessageDigest algorithm = null;

        try {
            algorithm = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        byte messageDigest[] = new byte[0];

        try {
            messageDigest = algorithm.digest(senha.getBytes("UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        StringBuilder hexString = new StringBuilder();
        for (byte b : messageDigest) {
            hexString.append(String.format("%02X", 0xFF & b));
        }

        return hexString.toString();
    }

    @Override
    public String getRequestBody() {
        Map<String,String> requestMap = new HashMap<>();

        JSONObject json = new JSONObject(requestMap);
        requestMap.put("a", "1");
        String jsonString = json.toString();

        return  jsonString;
    }

    @Override
    public void handleResponse(String response) {

        User user = new User();

        try {

            JSONArray jsonArray = new JSONArray(response);

            for(int index= 0; index < jsonArray.length(); index++){

                JSONObject userJSON = (JSONObject) jsonArray.get(index);

                String sEmail = userJSON.getString("email");
                String sSenha = userJSON.getString("senha");

                if (sEmail.equals(this.email) && sSenha.equals(this.senha)) {
                    user.setSenha(sSenha);
                    user.setEmail(sEmail);
                }
            }

            if (user.getSenha() == null) {
                EventBus.getDefault().post(new Error(getContext().getString(R.string.login_invalido)));
            }
            else {
                EventBus.getDefault().post(user);
            }
        }
        catch (JSONException e) {
            EventBus.getDefault().post(new Error(getContext().getString(R.string.label_error_invalid_response)));
        }
    }
}