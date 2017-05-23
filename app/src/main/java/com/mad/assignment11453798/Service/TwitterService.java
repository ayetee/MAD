package com.mad.assignment11453798.Service;

import com.mad.assignment11453798.Interface.TwitterApi;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.mad.assignment11453798.Interface.TwitterApi.BASE_URL;

/**
 * Twitter Services Class
 */
public class TwitterService {

    public static TwitterApi createService() {
        return getRetrofit().create(TwitterApi.class);
    }

    private static Retrofit getRetrofit() {
        return new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }
}
