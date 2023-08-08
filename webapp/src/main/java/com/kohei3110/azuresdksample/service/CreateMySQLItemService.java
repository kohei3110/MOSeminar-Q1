package com.kohei3110.azuresdksample.service;

import com.kohei3110.azuresdksample.model.Member;
import com.kohei3110.azuresdksample.repository.CreateMySQLItemRepository;

public class CreateMySQLItemService {

    private CreateMySQLItemRepository repository;

    public CreateMySQLItemService(CreateMySQLItemRepository repository) {
        this.repository = repository;
    }

    public Member create(Member member) {
        return this.repository.create(member);
    }
}
