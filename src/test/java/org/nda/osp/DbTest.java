package org.nda.osp;


import oracle.jdbc.OracleTypes;
import oracle.sql.OracleSQLOutput;
import org.junit.jupiter.api.Test;
import org.nda.osp.jpa.K012Proc;
import org.nda.osp.jpa.K012Repository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.SqlOutParameter;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.core.SqlReturnType;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.orm.jpa.JpaSystemException;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.sql.Types;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class DbTest {

    @Autowired
    private K012Repository k012Repository;
    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Value("${spring.datasource.hikari.schema}")
    private String schemaName;

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
        //IF we use metadata we have to specify owner schema explicitly
        SimpleJdbcCall call = new SimpleJdbcCall(jdbcTemplate);
        call.withCatalogName("TEST_PKG")
                .withSchemaName(schemaName)
                .withFunctionName("INSERT_DATA")
                //.declareParameters(
                //        new SqlParameter("P_K012_TITLE", Types.VARCHAR)
                //        )
                //.withoutProcedureColumnMetaDataAccess()
                .setReturnValueRequired(true);
        Map<String, Object> inParams = new HashMap<>();
        inParams.put("P_K012_TITLE", "TITLE5");
        assertDoesNotThrow(()->call.executeFunction(BigDecimal.class, inParams));
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
