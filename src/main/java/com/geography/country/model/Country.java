package com.geography.country.model;

import java.io.Serializable;
import java.util.List;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@EqualsAndHashCode
public class Country implements Serializable{
    private String name;
    private String capital;
    private String region;
    private Integer population;
    private List<String> languages;
    private String flagURL; 
    private String wikiURL;
    private String audioURL;

}
