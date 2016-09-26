package com.taxi.clickcar.WebOrdersAPI;

import com.taxi.clickcar.Requests.AuthRequest;
import com.taxi.clickcar.Responses.AuthResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

/**
 * Created by Назар on 11.09.2016.
 */
public interface WebOrdersApiInterface {
    @Headers({"Accept: application/json","Content-type: application/json; charset=utf-8"})
    @POST("/api/account")
    Call<AuthResponse> loadAuth(@Body AuthRequest user_authRequest);

}