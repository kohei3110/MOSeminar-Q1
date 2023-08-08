package com.kohei3110.azuresdksample.service;

import java.util.logging.Logger;

import org.springframework.web.multipart.MultipartFile;

import com.kohei3110.azuresdksample.repository.LocalStoreRepository;

public class LocalStoreService {

    Logger logger = Logger.getLogger(LocalStoreService.class.getName());

    private LocalStoreRepository localStoreRepository;

    public LocalStoreService(LocalStoreRepository localStoreRepository) {
        this.localStoreRepository = localStoreRepository;
    }

    public void init() throws Exception {
        localStoreRepository.init();
    }

    public void store(MultipartFile file) throws Exception {
        localStoreRepository.store(file);
    }
}
