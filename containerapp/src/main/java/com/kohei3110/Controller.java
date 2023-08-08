package com.kohei3110;

import com.kohei3110.service.CopyBlobAcrossBlobAccountsService;
import com.microsoft.azure.functions.ExecutionContext;
import com.microsoft.azure.functions.annotation.FunctionName;
import com.microsoft.azure.functions.annotation.TimerTrigger;

public class Controller {

    Factory factory = new Factory();

    @FunctionName("SyncBlobs")
    public void syncBlobs(
            @TimerTrigger(name = "timerInfo", schedule = "0 */1 * * * *") String timerInfo,
            ExecutionContext context) {
        CopyBlobAcrossBlobAccountsService service = factory.injectService();
        try {
            service.copyBlob();
        } catch (Exception e) {
            context.getLogger().warning(e.getMessage());
        }
    }
}
