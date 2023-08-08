package com.kohei3110.azuresdksample.repository;

import java.io.IOException;
import java.util.Properties;
import java.util.logging.Logger;

import com.azure.cosmos.CosmosAsyncClient;
import com.azure.cosmos.CosmosAsyncContainer;
import com.azure.cosmos.CosmosAsyncDatabase;
import com.azure.cosmos.CosmosClient;
import com.azure.cosmos.CosmosClientBuilder;
import com.azure.cosmos.CosmosContainer;
import com.azure.cosmos.CosmosDatabase;
import com.azure.cosmos.models.CosmosItemResponse;
import com.kohei3110.azuresdksample.model.Member;

import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

public class CreateCosmosItemRepository {

    Logger logger = Logger.getLogger(CreateCosmosItemRepository.class.getName());

    private Properties properties;
    private CosmosClient cosmosClient;
    private CosmosDatabase cosmosDatabase;
    private CosmosContainer cosmosContainer;
    private CosmosAsyncClient cosmosAsyncClient;
    private CosmosAsyncDatabase cosmosAsyncDatabase;
    private CosmosAsyncContainer cosmosAsyncContainer;

    public CreateCosmosItemRepository() {
        this.properties = new Properties();
        try {
            this.properties.load(this.getClass().getResourceAsStream("/application.properties"));
        } catch (IOException e) {
            logger.warning(e.getMessage());
        }
        this.cosmosClient = new CosmosClientBuilder()
                .endpoint(properties.getProperty("cosmos.endpoint"))
                .key(properties.getProperty("cosmos.key"))
                .buildClient();
        this.cosmosDatabase = cosmosClient.getDatabase(properties.getProperty("cosmos.database"));
        this.cosmosContainer = cosmosDatabase.getContainer(properties.getProperty("cosmos.container"));

        this.cosmosAsyncClient = new CosmosClientBuilder()
                .endpoint(properties.getProperty("cosmos.endpoint"))
                .key(properties.getProperty("cosmos.key"))
                .buildAsyncClient();
        this.cosmosAsyncDatabase = cosmosAsyncClient.getDatabase(properties.getProperty("cosmos.database"));
        this.cosmosAsyncContainer = cosmosAsyncDatabase.getContainer(properties.getProperty("cosmos.container"));
    }

    public Member create(Member member) {
        CosmosItemResponse<Member> response = this.cosmosContainer.createItem(member);
        return response.getItem();
    }

    public Member createAsync(Member member) {
        Mono<CosmosItemResponse<Member>> mono = this.cosmosAsyncContainer.createItem(member);
        mono
                .publishOn(Schedulers.parallel())
                .subscribe(
                        response -> {
                            // this is now executed on reactor scheduler's parallel thread.
                            // reactor scheduler's
                            veryCpuIntensiveWork();
                        });
        return member;
    }

    private void veryCpuIntensiveWork() {
        logger.info("veryCpuIntensiveWork executing....");
    }
}
