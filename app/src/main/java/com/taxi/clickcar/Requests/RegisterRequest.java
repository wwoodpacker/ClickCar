package com.taxi.clickcar.Requests;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Назар on 27.09.2016.
 */
public class RegisterRequest {
    @SerializedName("phone")
    String phone;
    @SerializedName("confirm_code")
    String confirm_code;
    @SerializedName("password")
    String password;
    @SerializedName("confirm_password")
    String confirm_password;
    @SerializedName("user_first_name")
    String user_first_name;
    public RegisterRequest(String phone,String confirm_code,String password,String confirm_password,String user_first_name){
        this.phone=phone;
        this.confirm_code=confirm_code;
        this.password=password;
        this.confirm_password=confirm_password;
        this.user_first_name=user_first_name;
    }
}
