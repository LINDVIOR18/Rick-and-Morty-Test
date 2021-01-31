package com.pam.rickandmortypersonajes.entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class ResultCharacter {

    @SerializedName("id")
    @Expose
    public Integer id;

    @SerializedName("name")
    @Expose
    public String name;

    @SerializedName("status")
    @Expose
    public String status;

    @SerializedName("species")
    @Expose
    public String species;

    @SerializedName("type")
    @Expose
    public String type;

    @SerializedName("gender")
    @Expose
    public String gender;

    @SerializedName("location")
    @Expose
    public ResultLastLocation location;

    @SerializedName("image")
    @Expose
    public String image;

    @SerializedName("episode")
    @Expose
    public List<String> episode;

    @SerializedName("url")
    @Expose
    public String url;



}