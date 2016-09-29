package com.taxi.clickcar.Requests;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Назар on 27.09.2016.
 */
public class PhoneRequest {
    @SerializedName("phone")
    String phone;
    public PhoneRequest(String _phone){
        phone=_phone;
    }
}
