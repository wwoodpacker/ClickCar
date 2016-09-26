package com.taxi.clickcar.Requests;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Назар on 26.09.2016.
 */
public class AuthRequest {
    @SerializedName("Login")
    String login;
    @SerializedName("Password")
    String password;
    public AuthRequest(String _log, String _pass){
        login=_log;
        password=_pass;
    }
}
