package org.nda.osp;


import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;

import java.math.BigDecimal;

@SpringBootTest
public class DbTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Test
    public void callProc() {
        SimpleJdbcCall call = new SimpleJdbcCall(jdbcTemplate);
        call.withCatalogName("TEST_PKG")
                .withFunctionName("INSERT_DATA");

        SqlParameterSource inp = new MapSqlParameterSource().addValue("P_K012_TITLE", "TITLE2");
        System.out.println(call.executeFunction(BigDecimal.class, inp));
    }
}
