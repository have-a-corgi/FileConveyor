package org.nda.osp.processors;

import lombok.extern.slf4j.Slf4j;
import org.nda.osp.storage.PackageStorage;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;

@Slf4j
@Controller
public class FileAcceptor {

    @Scheduled(fixedRate = 8000)
    public void proceedFilesTo() {
        //Здесь эмулируется прием, распаковка, сохранение и трейсинг
        //пакетов (в памяти)
        int packageNumber = (int) (Math.random() * 10);
        for (int i=1; i<=packageNumber; i++) {
            PackageStorage.putPackage();
        }

    }
}
