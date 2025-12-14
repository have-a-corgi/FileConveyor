package org.nda.osp.storage;

import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
public class PackageStorage {

    private static int STORAGE_LIMIT = 100;
    private static volatile boolean noAction = false;

    private static final Map<UUID, DataPackage> DATA_MAP = new ConcurrentHashMap<>();

    public static void putPackage() {
        if (!noAction && DATA_MAP.size() < STORAGE_LIMIT) {
            DataPackage dataPackage = new DataPackage();
            log.info("appended {} package", dataPackage.getIdentity());
            DATA_MAP.put(dataPackage.getIdentity(), dataPackage);
        } else {
            log.info("Storage limit reached");
            noAction = true;
        }
    }

    public static Map<UUID, DataPackage> provideMap() {
        //Очистка от пустых
        Set<UUID> uuids = DATA_MAP.keySet();
        uuids.forEach(key->{
            if (DATA_MAP.get(key).isToBeDeleted()) {
                DATA_MAP.remove(key);
                log.info("Удален пустой пакет {}", key);
            }
        });
        return DATA_MAP;
    }

}
