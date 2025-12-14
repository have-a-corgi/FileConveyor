package org.nda.osp.processors;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.nda.osp.storage.PackageStorage;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Controller;

import java.util.UUID;

@Slf4j
@Controller
public class QueueController {

    private final ThreadPoolTaskExecutor executor;

    public QueueController() {
        executor = new ThreadPoolTaskExecutor();
        executor.setQueueCapacity(4);
        executor.setMaxPoolSize(2);
        executor.setCorePoolSize(1);
        executor.setKeepAliveSeconds(0);
        executor.setThreadNamePrefix("FileSender");
        executor.initialize();
    }

    public void submit(ImmutablePair<UUID, String> fileData) {
        UUID left = fileData.getLeft();
        try {
            executor.submit(new FileSender(fileData));
            System.out.printf("Выбран файл %s из пакета %s%n", left, fileData.getRight());
        } catch(Exception e) {
            log.error("No submit: {}", e.getMessage());
            PackageStorage.provideMap().get(left).backToList(fileData.getRight());
        }
    }
}
