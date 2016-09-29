package com.taxi.clickcar.WebOrdersAPI;

import com.taxi.clickcar.StaticMethods;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Назар on 29.09.2016.
 */
public class ApiClient {
    public static final String BASE_URL = "http://176.38.246.10:7200";
    public static Retrofit getClient(){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        return retrofit;
    }
}
