package com.kohei3110;

import com.kohei3110.repository.CopyBlobAcrossBlobAccountsRepository;
import com.kohei3110.service.CopyBlobAcrossBlobAccountsService;

public class Factory {

    private CopyBlobAcrossBlobAccountsRepository repository;
    private CopyBlobAcrossBlobAccountsService service;

    public Factory() {
        this.repository = new CopyBlobAcrossBlobAccountsRepository();
        this.service = new CopyBlobAcrossBlobAccountsService(this.repository);
    }

    public CopyBlobAcrossBlobAccountsService injectService() {
        return this.service;
    }
}
