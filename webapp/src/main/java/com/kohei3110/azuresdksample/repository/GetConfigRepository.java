package com.kohei3110.azuresdksample.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;

public class GetConfigRepository {

    @Autowired
    private Environment environment;

    public String getConfig() {
        return this.environment.getProperty("Weather");
    }
}
