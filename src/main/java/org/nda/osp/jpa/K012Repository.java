package org.nda.osp.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface K012Repository extends JpaRepository<K012Table, String> {

    @Modifying
    @Query(nativeQuery = true,
    value = "SELECT test_pkg.insert_data(:k012Id) FROM dual")
    int insertButSkipIfUniqueConstraintsViolated(@Param("k012Id") String k012Id);
}
