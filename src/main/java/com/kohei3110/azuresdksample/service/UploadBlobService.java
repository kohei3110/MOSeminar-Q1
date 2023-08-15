package com.kohei3110.azuresdksample.service;

import org.springframework.web.multipart.MultipartFile;

import com.kohei3110.azuresdksample.repository.UploadBlobRepository;

public class UploadBlobService {

    private UploadBlobRepository uploadBlobRepository;

    public UploadBlobService(UploadBlobRepository uploadBlobRepository) {
        this.uploadBlobRepository = uploadBlobRepository;
    }

    public void upload(MultipartFile file) throws Exception {
        if (file.isEmpty()) {
            throw new Exception("Blob is empty.");
        }
        this.uploadBlobRepository.uploadBlob(file);
    }
}
