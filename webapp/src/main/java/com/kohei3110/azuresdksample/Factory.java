package com.kohei3110.azuresdksample;

import com.kohei3110.azuresdksample.repository.CreateCosmosItemRepository;
import com.kohei3110.azuresdksample.repository.CreateMySQLItemRepository;
import com.kohei3110.azuresdksample.repository.GetConfigRepository;
import com.kohei3110.azuresdksample.repository.LocalStoreRepository;
import com.kohei3110.azuresdksample.repository.UploadBlobRepository;
import com.kohei3110.azuresdksample.service.CreateCosmosItemService;
import com.kohei3110.azuresdksample.service.CreateMySQLItemService;
import com.kohei3110.azuresdksample.service.GetConfigService;
import com.kohei3110.azuresdksample.service.LocalStoreService;
import com.kohei3110.azuresdksample.service.UploadBlobService;

public class Factory {

    private CreateMySQLItemRepository createMySQLItemRepository;
    private CreateMySQLItemService createMySQLItemService;
    private CreateCosmosItemRepository createCosmosItemRepository;
    private CreateCosmosItemService createCosmosItemService;
    private LocalStoreRepository localStoreRepository;
    private LocalStoreService localStoreService;
    private UploadBlobRepository uploadBlobRepository;
    private UploadBlobService uploadBlobService;
    private GetConfigRepository getConfigRepository;
    private GetConfigService getConfigService;

    public Factory() {
        this.createMySQLItemRepository = new CreateMySQLItemRepository();
        this.createMySQLItemService = new CreateMySQLItemService(this.createMySQLItemRepository);
        this.createCosmosItemRepository = new CreateCosmosItemRepository();
        this.createCosmosItemService = new CreateCosmosItemService(this.createCosmosItemRepository);
        this.localStoreRepository = new LocalStoreRepository();
        this.localStoreService = new LocalStoreService(this.localStoreRepository);
        this.uploadBlobRepository = new UploadBlobRepository();
        this.uploadBlobService = new UploadBlobService(this.uploadBlobRepository);
        this.getConfigRepository = new GetConfigRepository();
        this.getConfigService = new GetConfigService(this.getConfigRepository);
    }

    public CreateMySQLItemService injectCreateMySQLItemService() {
        return this.createMySQLItemService;
    }

    public CreateCosmosItemService injectCreateCosmosItemService() {
        return this.createCosmosItemService;
    }

    public LocalStoreService injectLocalStoreService() {
        return this.localStoreService;
    }

    public UploadBlobService injectUploadBlobService() {
        return this.uploadBlobService;
    }

    public GetConfigService injectGetConfigService() {
        return this.getConfigService;
    }
}
