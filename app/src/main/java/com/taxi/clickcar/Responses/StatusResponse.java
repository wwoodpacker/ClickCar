package com.taxi.clickcar.Responses;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Назар on 27.09.2016.
 */
public class StatusResponse {
    @SerializedName("Id")
    public int id;
    @SerializedName("Message")
    public String message;
    public StatusResponse(){};

    public String getStatus() {
        return message;
    }
}
