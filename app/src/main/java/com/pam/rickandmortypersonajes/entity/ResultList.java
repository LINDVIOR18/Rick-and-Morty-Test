package com.pam.rickandmortypersonajes.entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ResultList {
    @SerializedName("results")
    @Expose
    public List<ResultCharacter> results;
}
