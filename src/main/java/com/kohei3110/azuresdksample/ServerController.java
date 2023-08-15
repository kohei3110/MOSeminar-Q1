package com.kohei3110.azuresdksample;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.kohei3110.azuresdksample.model.Member;
import com.kohei3110.azuresdksample.service.LocalStoreService;
import com.kohei3110.azuresdksample.service.UploadBlobService;

@SpringBootApplication
@RestController
public class ServerController {

	public static void main(String[] args) {
		SpringApplication.run(ServerController.class, args);
	}

	Factory factory = new Factory();

	@PostMapping("/items/mysql")
	public Member createMySQLItem(@RequestBody Member member) {
		return this.factory.injectCreateMySQLItemService().create(member);
	}

	@PostMapping("/items/cosmos/sync")
	public Member createCosmosItem(@RequestBody Member member) {
		return this.factory.injectCreateCosmosItemService().create(member);
	}

	@PostMapping("/items/cosmos/async")
	public Member createCosmosItemAsync(@RequestBody Member member) {
		return this.factory.injectCreateCosmosItemService().createAsync(member);
	}

	@PostMapping("/items/blob")
	public void uploadBlob(@RequestBody MultipartFile file) throws Exception {
		LocalStoreService localStoreService = factory.injectLocalStoreService();
		UploadBlobService uploadBlobService = factory.injectUploadBlobService();
		localStoreService.init();
		localStoreService.store(file);
		uploadBlobService.upload(file);
	}
}
