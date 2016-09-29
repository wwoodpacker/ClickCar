package com.taxi.clickcar.WebOrdersAPI;

import com.taxi.clickcar.Responses.StatusResponse;

import java.io.IOException;
import java.lang.annotation.Annotation;

import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Response;

/**
 * Created by Назар on 27.09.2016.
 */
public class ErrorUtils {
    public static StatusResponse parseError(Response<?> response) {
        Converter<ResponseBody, StatusResponse> converter =
                ApiClient.getClient()
                        .responseBodyConverter(StatusResponse.class,new Annotation[0]);

        StatusResponse error;

        try {
            error = converter.convert(response.errorBody());
        } catch (IOException e) {
            return new StatusResponse();
        }

        return error;
    }
}
