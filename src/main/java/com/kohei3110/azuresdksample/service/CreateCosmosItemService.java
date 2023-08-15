package com.kohei3110.azuresdksample.service;

import com.kohei3110.azuresdksample.model.Member;
import com.kohei3110.azuresdksample.repository.CreateCosmosItemRepository;

public class CreateCosmosItemService {

    private CreateCosmosItemRepository createCosmosItemRepository;

    public CreateCosmosItemService(CreateCosmosItemRepository createCosmosItemRepository) {
        this.createCosmosItemRepository = createCosmosItemRepository;
    }

    public Member create(Member member) {
        return this.createCosmosItemRepository.create(member);
    }

    public Member createAsync(Member member) {
        return this.createCosmosItemRepository.createAsync(member);
    }
}
