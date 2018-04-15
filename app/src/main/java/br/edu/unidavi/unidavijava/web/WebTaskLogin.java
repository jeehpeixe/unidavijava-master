package br.edu.unidavi.unidavijava.web;

import android.content.Context;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

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
    private String password;

    public WebTaskLogin(Context context, String email, String password) {
        super(context, SERVICE_NAME);
        this.email = email;
        this.password = password;
    }

    @Override
    public String getRequestBody() {
        Map<String,String> requestMap = new HashMap<>();
        requestMap.put("email", email);
        requestMap.put("password", password);

        JSONObject json = new JSONObject(requestMap);
        String jsonString = json.toString();

        return  jsonString;
    }

    @Override
    public void handleResponse(String response) {
        User user = new User();
        try {
            JSONObject responseAsJSON = new JSONObject(response);
            String name = responseAsJSON.getString("name");
            user.setName(name);
            String token = responseAsJSON.getString("token");
            user.setToken(token);
            user.setEmail(email);

            EventBus.getDefault().post(user);

        }
        catch (JSONException e) {
            EventBus.getDefault().post(new Error(getContext().getString(
                    R.string.label_error_invalid_response)));
        }
    }
}