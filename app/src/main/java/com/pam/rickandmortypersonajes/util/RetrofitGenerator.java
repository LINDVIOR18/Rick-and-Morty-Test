package com.pam.rickandmortypersonajes.util;

import com.pam.rickandmortypersonajes.api.CharacterAPI;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.pam.rickandmortypersonajes.util.Constants.BASE_URL;

public class RetrofitGenerator {
    private static Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    private static CharacterAPI character;

    public static CharacterAPI getApiService() {
        if (character == null) {
            character = retrofit.create(CharacterAPI.class);
        }
        return character;
    }
}
