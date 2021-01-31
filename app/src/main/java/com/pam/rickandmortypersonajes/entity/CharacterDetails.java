package com.pam.rickandmortypersonajes.entity;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class CharacterDetails {
    public int id;
    public String name;
    public String status;
    public String species;
    public String type;
    public String gender;
    public ResultLastLocation location;
    public String image;
    public List<String> episode;
    public String url;
    public String created;
}
