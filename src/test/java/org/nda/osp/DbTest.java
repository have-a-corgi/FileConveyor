package org.nda.osp;


import oracle.jdbc.OracleTypes;
import org.junit.jupiter.api.Test;
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
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.math.BigDecimal;
import java.sql.Types;
import java.util.HashMap;
import java.util.Map;

@SpringBootTest
public class DbTest {

    @Autowired
    private K012Repository k012Repository;
    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private EntityManager entityManager;
    //@Value("${spring.jpa.}")

    @Test
    void counttest() {
        System.out.println(k012Repository.findAll().size());
    }

    @Test
    @Transactional
    public void callFunc() {
        SimpleJdbcCall call = new SimpleJdbcCall(jdbcTemplate);
        call.withCatalogName("TEST_PKG")
                .withSchemaName("TEST_USER")
                .withFunctionName("INSERT_DATA")
                //.declareParameters(
                 //       new SqlParameter("P_K012_TITLE", Types.VARCHAR)
                        //)
                //.withoutProcedureColumnMetaDataAccess()
                .setReturnValueRequired(true);
        Map<String, Object> inParams = new HashMap<>();
        inParams.put("P_K012_TITLE", "TITLE5");
        System.out.println(call.executeFunction(BigDecimal.class, inParams));
    }

    @Test
    @Transactional
    public void callProc() {
        //This kind of call uses settings of default schema (seems hikari.schema - on some tests basis we cam
        // make this suggestion)
        //But if we try to switch off withoutProcedureColumnMetaDataAccess() we get error
        //PLS-00306: wrong number or types of arguments in call to 'INSERT_DATA_P'
        //ORA-06550: line 1, column 7:
        // This means we have to avoid
        SimpleJdbcCall call = new SimpleJdbcCall(jdbcTemplate);
        call.withCatalogName("TEST_PKG")
                .withProcedureName("INSERT_DATA_P")
                .declareParameters(new SqlParameter("P_K012_TITLE", Types.VARCHAR),
                        new SqlOutParameter("P_ROW_COUNT", Types.NUMERIC))
                .withoutProcedureColumnMetaDataAccess();
        SqlParameterSource inp = new MapSqlParameterSource().addValue("P_K012_TITLE", "TITLE4");
        Map<String, Object> result = call.execute(inp);
        BigDecimal pRowCount = (BigDecimal)result.get("P_ROW_COUNT");
        System.out.println(pRowCount.intValue());
    }
}
