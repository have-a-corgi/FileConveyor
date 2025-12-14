package org.nda.osp.processors;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.nda.osp.storage.PackageStorage;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

import static java.util.Optional.ofNullable;

@RequiredArgsConstructor
@Slf4j
public class FileSender implements Runnable {

    private final ImmutablePair<UUID, String> fileData;

    @Override
    public void run() {
        try {
            TimeUnit.SECONDS.sleep((int) (Math.random() * 10)+1);
            UUID packageId = fileData.getLeft();
            ofNullable(PackageStorage.provideMap().get(packageId))
                    .ifPresent(
                            pk->{
                                pk.removeProcessedFile(fileData.getRight());
                                log.info("File {} of package {} is proceeded", fileData.getRight(), fileData.getLeft());
                            }
                    );
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
