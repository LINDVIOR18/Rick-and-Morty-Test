package com.pam.rickandmortypersonajes.entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ResultLastLocation {

    @SerializedName("id")
    @Expose
    public int id;

    @SerializedName("name")
    @Expose
    public String name;

    @SerializedName("residents")
    @Expose
    public List<String> residents;

    @SerializedName("url")
    @Expose
    public String url;
}


