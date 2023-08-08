package com.kohei3110.azuresdksample.repository;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Properties;
import java.util.logging.Logger;

import org.springframework.web.multipart.MultipartFile;

public class LocalStoreRepository {

    Logger logger = Logger.getLogger(LocalStoreRepository.class.getName());
    private Properties properties;
    private Path path;

    public LocalStoreRepository() {
        this.properties = new Properties();
        try {
            this.properties.load(this.getClass().getResourceAsStream("/application.properties"));
        } catch (Exception e) {
            logger.warning(e.getMessage());
        }
        this.path = Paths.get(this.properties.getProperty("tmp.dir"));
    }

    public void init() throws Exception {
        try {
            Files.createDirectories(path);
        } catch (Exception e) {
            logger.warning(e.getMessage());
            throw new Exception("Create tmp directory has failed.");
        }
    }

    public void store(MultipartFile file) throws Exception {
        if (file.isEmpty()) {
            throw new Exception("Upload file is empty.");
        }
        Path destinationFile = this.path.resolve(
                Paths.get(file.getOriginalFilename()))
                .normalize()
                .toAbsolutePath();
        try (InputStream inputStream = file.getInputStream()) {
            Files.copy(inputStream, destinationFile,
                    StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new Exception("Failed to store file.", e);
        }
    }

}
