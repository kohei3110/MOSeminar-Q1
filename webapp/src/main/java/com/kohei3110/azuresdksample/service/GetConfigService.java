package com.kohei3110.azuresdksample.service;

import com.kohei3110.azuresdksample.repository.GetConfigRepository;

public class GetConfigService {

    private GetConfigRepository getConfigRepository;

    public GetConfigService(GetConfigRepository getConfigRepository) {
        this.getConfigRepository = getConfigRepository;
    }

    public String getConfig() {
        return this.getConfigRepository.getConfig();
    }
}
