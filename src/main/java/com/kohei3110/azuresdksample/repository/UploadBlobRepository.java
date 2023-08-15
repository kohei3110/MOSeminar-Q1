package com.kohei3110.azuresdksample.repository;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;
import java.util.logging.Logger;

import org.springframework.web.multipart.MultipartFile;

import com.azure.storage.blob.BlobAsyncClient;
import com.azure.storage.blob.BlobClientBuilder;

import reactor.core.scheduler.Schedulers;

public class UploadBlobRepository {

    Logger logger = Logger.getLogger(UploadBlobRepository.class.getName());

    private Properties properties;
    private BlobAsyncClient blobAsyncClient;
    private Path tmpDir;
    private String blobConnectionString;

    public UploadBlobRepository() {
        this.properties = new Properties();
        try {
            this.properties.load(this.getClass().getResourceAsStream("/application.properties"));
        } catch (Exception e) {
            logger.warning(e.getMessage());
        }
        this.tmpDir = Paths.get(this.properties.getProperty("tmp.dir"));
        this.blobConnectionString = this.properties.getProperty("blob.endpoint");
    }

    public void uploadBlob(MultipartFile file) throws Exception {
        try {
            this.blobAsyncClient = buildBlobClient(Paths.get(file.getOriginalFilename().toString()).toString(),
                    this.blobConnectionString);
        } catch (Exception e) {
            logger.warning(e.getMessage());
            throw new Exception("Blob Upload operation has failed.");
        }
        Path destinationFile = this.tmpDir.resolve(
                Paths.get(file.getOriginalFilename()))
                .normalize()
                .toAbsolutePath();
        blobAsyncClient.uploadFromFile(destinationFile.toString())
                .publishOn(Schedulers.parallel())
                .subscribe(response -> {
                    veryCpuIntensiveWork();
                });
    }

    private BlobAsyncClient buildBlobClient(String blobName, String connectionString) throws Exception {
        try {
            BlobAsyncClient blobAsyncClient = new BlobClientBuilder()
                    .connectionString(connectionString)
                    .containerName(this.properties.getProperty("blob.container.name"))
                    .blobName(blobName)
                    .buildAsyncClient();

            return blobAsyncClient;
        } catch (Exception e) {
            logger.warning("Build blobAsyncClient has failed.");
            throw new Exception(e.toString());
        }
    }

    private void veryCpuIntensiveWork() {
        logger.info("veryCpuIntensiveWork executing....");
    }
}
