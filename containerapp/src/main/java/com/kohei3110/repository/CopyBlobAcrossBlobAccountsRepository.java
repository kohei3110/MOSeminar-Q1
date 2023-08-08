package com.kohei3110.repository;

import java.time.Duration;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.logging.Logger;

import com.azure.core.http.rest.PagedResponse;
import com.azure.core.util.polling.LongRunningOperationStatus;
import com.azure.core.util.polling.PollResponse;
import com.azure.core.util.polling.SyncPoller;
import com.azure.storage.blob.BlobClient;
import com.azure.storage.blob.BlobClientBuilder;
import com.azure.storage.blob.BlobContainerClient;
import com.azure.storage.blob.BlobContainerClientBuilder;
import com.azure.storage.blob.models.BlobCopyInfo;
import com.azure.storage.blob.models.BlobItem;
import com.azure.storage.blob.sas.BlobSasPermission;
import com.azure.storage.blob.sas.BlobServiceSasSignatureValues;
import com.azure.storage.blob.specialized.BlobLeaseClient;
import com.azure.storage.blob.specialized.BlobLeaseClientBuilder;
import com.azure.storage.blob.specialized.BlockBlobClient;

public class CopyBlobAcrossBlobAccountsRepository {

    Logger logger = Logger.getLogger(CopyBlobAcrossBlobAccountsRepository.class.getName());

    private Properties properties;

    public CopyBlobAcrossBlobAccountsRepository() {
        this.properties = new Properties();
        try {
            this.properties.load(this.getClass().getResourceAsStream("/application.properties"));
        } catch (Exception e) {
            logger.warning(e.getMessage());
        }
    }

    public void copyBlob() throws Exception {
        // コピー処理
        List<String> blobNames = getBlobNames();
        for (String blobName : blobNames) {
            BlobClient sourceBlob = buildBlobClient(blobName);
            BlobLeaseClient lease = new BlobLeaseClientBuilder()
                    .blobClient(sourceBlob)
                    .buildClient();
            BlockBlobClient destinationBlob = buildBlockBlobClient(buildTargetBlobContainerClient(), blobName);
            String sasToken = generateOneDayReadSASToken(sourceBlob);
            String sourceBlobSasUrl = sourceBlob.getBlobUrl() + "?" + sasToken;
            try {
                lease.acquireLease(-1);
                final SyncPoller<BlobCopyInfo, Void> poller = destinationBlob.beginCopy(
                        sourceBlobSasUrl,
                        Duration.ofSeconds(2));
                PollResponse<BlobCopyInfo> response = poller
                        .waitUntil(LongRunningOperationStatus.SUCCESSFULLY_COMPLETED);
            } finally {
                lease.releaseLease();
            }
        }
    }

    private BlobClient buildBlobClient(String blobName) throws Exception {
        try {
            BlobClient blobClient = new BlobClientBuilder()
                    .connectionString(this.properties.getProperty("blob.container.source.connectionString"))
                    .containerName(this.properties.getProperty("blob.container.name"))
                    .blobName(blobName)
                    .buildClient();

            return blobClient;
        } catch (Exception e) {
            logger.warning("Build blobClient has failed.");
            throw new Exception(e.toString());
        }
    }

    private BlobContainerClient buildSourceBlobContainerClient() throws Exception {
        try {
            BlobContainerClient blobContainerClient = new BlobContainerClientBuilder()
                    .connectionString(this.properties.getProperty("blob.container.source.connectionString"))
                    .containerName(this.properties.getProperty("blob.container.name"))
                    .buildClient();

            return blobContainerClient;
        } catch (Exception e) {
            logger.warning("Build blobContainerClient has failed.");
            throw new Exception(e.toString());
        }
    }

    private BlobContainerClient buildTargetBlobContainerClient() throws Exception {
        try {
            BlobContainerClient blobContainerClient = new BlobContainerClientBuilder()
                    .connectionString(this.properties.getProperty("blob.container.target.connectionString"))
                    .containerName(this.properties.getProperty("blob.container.name"))
                    .buildClient();

            return blobContainerClient;
        } catch (Exception e) {
            logger.warning("Build blobContainerClient has failed.");
            throw new Exception(e.toString());
        }
    }

    private BlockBlobClient buildBlockBlobClient(BlobContainerClient blobContainerClient, String blobName) {
        BlockBlobClient blockBlobClient = blobContainerClient.getBlobClient(blobName).getBlockBlobClient();
        return blockBlobClient;
    }

    private List<String> getBlobNames() throws Exception {
        List<String> blobNames = new ArrayList<>();
        BlobContainerClient blobContainerClient = buildSourceBlobContainerClient();
        Iterable<PagedResponse<BlobItem>> blobPages = blobContainerClient.listBlobs().iterableByPage();
        for (PagedResponse<BlobItem> page : blobPages) {
            page.getElements().forEach(blob -> {
                blobNames.add(blob.getName());
            });
        }
        return blobNames;
    }

    // 1日有効な SAS トークンを生成する
    // 読み取り権限を付与する
    private String generateOneDayReadSASToken(BlobClient blobClient) {
        OffsetDateTime expiryTime = OffsetDateTime.now().plusDays(1);
        BlobSasPermission sasPermission = new BlobSasPermission().setReadPermission(true);
        BlobServiceSasSignatureValues sasSignatureValues = new BlobServiceSasSignatureValues(expiryTime, sasPermission)
                .setStartTime(OffsetDateTime.now().minusMinutes(5));
        String sasToken = blobClient.generateSas(sasSignatureValues);
        return sasToken;
    }
}
