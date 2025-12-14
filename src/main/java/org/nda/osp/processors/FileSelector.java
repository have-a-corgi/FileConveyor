package org.nda.osp.processors;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.nda.osp.storage.DataPackage;
import org.nda.osp.storage.PackageStorage;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;

import java.util.*;

import static java.util.Optional.ofNullable;

@RequiredArgsConstructor
@Slf4j
@Controller
public class FileSelector {

    private final QueueController queueController;

    @Scheduled(fixedRate = 10000)
    public void selectAndReserve() {
        Map<UUID, DataPackage> uuidDataPackageMap = PackageStorage.provideMap();
        ArrayList<UUID> list = new ArrayList<>(uuidDataPackageMap.keySet());
        Collections.shuffle(list);

        if (!list.isEmpty()) {
            list.subList(0, Math.min(3, list.size()))
                    .forEach(p -> {
                        ofNullable(uuidDataPackageMap.get(p))
                                .map(DataPackage::selectAnyFile)
                                .filter(Objects::nonNull)
                                .ifPresent(queueController::submit);
                    });
        }
    }
}
