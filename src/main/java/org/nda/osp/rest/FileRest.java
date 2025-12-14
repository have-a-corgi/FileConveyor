package org.nda.osp.rest;

import org.nda.osp.storage.DataPackage;
import org.nda.osp.storage.PackageStorage;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/size")
@RestController
public class FileRest {

    @GetMapping("/package")
    public Integer psize() {
        return PackageStorage.provideMap().size();
    }

    @GetMapping("/file")
    public Integer fsize() {
        return PackageStorage.provideMap()
                .values().stream().map(DataPackage::getSize)
                .mapToInt(t->t).sum();
    }


}
