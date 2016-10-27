package com.thunder.lifecare.rest;

import android.content.Context;

import java.io.IOException;
import java.lang.annotation.Annotation;

import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Response;

public class ErrorUtils {

    public static String parseError(Response<?> response, Context mContext) {
        Converter<ResponseBody, String> converter =
                RestClient.Single.INSTANCE.getInstance().getRetrofit().
                responseBodyConverter(String.class, new Annotation[0]);
        String error;
        try {
            error = converter.convert(response.errorBody());
        } catch (IOException e) {
            return null;
        }

        return error;
    }
}