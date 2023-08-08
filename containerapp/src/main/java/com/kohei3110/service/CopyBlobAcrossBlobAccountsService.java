package com.kohei3110.service;

import com.kohei3110.repository.CopyBlobAcrossBlobAccountsRepository;

public class CopyBlobAcrossBlobAccountsService {

    private CopyBlobAcrossBlobAccountsRepository repository;

    public CopyBlobAcrossBlobAccountsService(CopyBlobAcrossBlobAccountsRepository repository) {
        this.repository = repository;
    }

    public void copyBlob() throws Exception {
        this.repository.copyBlob();
    }
}
