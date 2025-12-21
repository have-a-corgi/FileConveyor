package org.nda.osp;


import org.junit.jupiter.api.Test;
import org.nda.osp.jpa.K012Proc;
import org.nda.osp.jpa.K012Repository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.orm.jpa.JpaSystemException;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class DbTest {

    @Autowired
    private K012Repository k012Repository;
    @Autowired
    private JdbcTemplate jdbcTemplate;


    @Autowired
    private K012Proc k012Proc;

    @Test
    void countTest() {
        assertTrue(k012Repository.findAll().size() > 0);
    }

    @Transactional
    @Test
    void testInsertUsingRepo() {
        //No way to execute cause this is prohibited in Oracle to perform DML inside select
        assertThrows(JpaSystemException.class,
                ()->k012Repository.insertButSkipIfUniqueConstraintsViolated("Johns"));
    }

    @Test
    @Transactional
    public void callFunc_using_schema_and_metadata() {
        assertDoesNotThrow(()->k012Proc.executeFuncUsingMetadata("TITLE5"));
    }

    @Test
    @Transactional
    public void callFunc_without_metadata() {
        assertDoesNotThrow(()->k012Proc.executeFunc("TITLE5"));
    }

    @Test
    @Transactional
    public void callProc() {
        assertDoesNotThrow(()->k012Proc.executeProc("TITLE5"));
    }
}
