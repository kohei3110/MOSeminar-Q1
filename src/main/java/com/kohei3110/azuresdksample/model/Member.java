package com.kohei3110.azuresdksample.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class Member {
    private String id;
    private String name;
    private String email;
}
