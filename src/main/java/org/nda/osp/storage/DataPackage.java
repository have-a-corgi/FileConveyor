package org.nda.osp.storage;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.ImmutablePair;

import java.util.*;

@Slf4j
public class DataPackage {
    private static Random RANDOM = new Random();
    private final UUID uuid;
    private final Set<String> files;
    private final Set<String> filesInProgress;

    public DataPackage() {
        uuid = UUID.randomUUID();
        files = new HashSet<>();
        filesInProgress = new HashSet<>();
        int fileNumber = (int)(Math.random()*10)+1;
        for(int i = 1; i<=fileNumber; i++) {
            files.add(String.format("file%s",UUID.randomUUID()));
        }
        log.info("Unzip {} files", fileNumber);
    }

    public int getSize() {
        return files.size();
    }

    public UUID getIdentity() {
        return uuid;
    }

    public synchronized ImmutablePair<UUID, String> selectAnyFile() {
        if (files.isEmpty()) {
            return null;
        }
        ArrayList<String> list = new ArrayList<>(files);
        Collections.shuffle(list);
        String fileToSend = list.get(0);
        files.remove(fileToSend);
        filesInProgress.add(fileToSend);
        return ImmutablePair.of(uuid, fileToSend);
    }

    public synchronized void backToList(String file) {
        filesInProgress.remove(file);
        files.add(file);
    }

    public synchronized void removeProcessedFile(String file) {
        filesInProgress.remove(file);
    }

    public synchronized boolean isToBeDeleted() {
        return (files.size() + filesInProgress.size()) == 0;
    }

}
