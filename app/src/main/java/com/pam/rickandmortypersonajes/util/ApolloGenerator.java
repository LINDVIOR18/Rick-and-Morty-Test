package com.pam.rickandmortypersonajes.util;

import com.apollographql.apollo.ApolloClient;
import okhttp3.OkHttpClient;
import okhttp3.Request;

import static com.pam.rickandmortypersonajes.util.Constants.GRAPHQL_URL;

public class ApolloGenerator {

    public static ApolloClient setupApollo() {

        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .addInterceptor(chain -> {
                    Request original = chain.request()
                            .newBuilder()
                            .build();
                    return chain.proceed(original);
                })
                .build();

        return ApolloClient.builder()
                .serverUrl(GRAPHQL_URL)
                .okHttpClient(okHttpClient)
                .build();
    }
}
