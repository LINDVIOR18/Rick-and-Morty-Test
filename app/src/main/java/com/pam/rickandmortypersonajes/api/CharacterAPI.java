package com.pam.rickandmortypersonajes.api;

import com.pam.rickandmortypersonajes.entity.*;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface CharacterAPI {

    @GET("character")
    Call<ResultList> getCharacters(@Query("page") int page);

    @GET("character/{id}")
    Call<CharacterDetails> getCharacterDetails(@Path("id") int id);

    @GET("episode/{id}")
    Call<ResultEpisode> getEpisodeDetails(@Path("id") int id);

    @GET("location/{id}")
    Call<ResultLastLocation> getLocations(@Path("id") int id);
}