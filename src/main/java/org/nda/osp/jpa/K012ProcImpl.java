package org.nda.osp.jpa;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.SqlOutParameter;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import java.math.BigDecimal;
import java.sql.CallableStatement;
import java.sql.Types;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

@RequiredArgsConstructor
@Repository
public class K012ProcImpl implements K012Proc {

    private static final String CALL_STATEMENT = "{call ? := test_pkg.insert_data(?)}";

    private final JdbcTemplate jdbcTemplate;

    private SimpleJdbcCall procCall;
    private SimpleJdbcCall funcCall;
    private SimpleJdbcCall funcCallUsingMetadata;

    @Value("${spring.datasource.hikari.schema}")
    private String schemaName;

    @PostConstruct
    private void setUp() {
        buildFuncCall();

        buildProcCall();

        buildFunctionUsingMetadata();

    }



    //This kind of call uses settings of default schema (seems hikari.schema - on some tests basis we cam
    // make this suggestion)
    //But if we try to switch off withoutProcedureColumnMetaDataAccess() we get error
    //PLS-00306: wrong number or types of arguments in call to 'INSERT_DATA_P'
    //ORA-06550: line 1, column 7:
    // This means we have to avoid the metadata using
    @Override
    public int executeProc(String name) {
        SqlParameterSource inp = new MapSqlParameterSource().addValue("P_K012_TITLE", "TITLE4");
        Map<String, Object> result = procCall.execute(inp);
        return ((BigDecimal)result.get("P_ROW_COUNT")).intValue();
    }

    @Override
    public int executeFunc(String name) {
        Map<String, Object> inParams = new HashMap<>();
        inParams.put("P_K012_TITLE", name);
        return funcCall.executeFunction(BigDecimal.class, inParams).intValue();
    }

    @Override
    public int executeFuncUsingMetadata(String name) {
        Map<String, Object> inParams = new HashMap<>();
        inParams.put("P_K012_TITLE", name);
        return funcCallUsingMetadata.executeFunction(BigDecimal.class, inParams).intValue();
    }

    @Override
    public int executeFuncUsingJdbcTemplate(String name) {
        Map<String, Object> result = jdbcTemplate.call(
                (cnn) -> {
                    CallableStatement callableStatement = cnn.prepareCall(CALL_STATEMENT);
                    callableStatement.registerOutParameter(1, Types.NUMERIC);
                    callableStatement.setString(2, name);
                    return callableStatement;
                },
                List.of(new SqlOutParameter("result", Types.NUMERIC))
        );
        return ((BigDecimal)result.get("result")).intValue();
    }

    private void buildProcCall() {
        procCall = new SimpleJdbcCall(jdbcTemplate)
                .withCatalogName("TEST_PKG")
                .withProcedureName("INSERT_DATA_P")
                .declareParameters(new SqlParameter("P_K012_TITLE", Types.VARCHAR),
                        new SqlOutParameter("P_ROW_COUNT", Types.NUMERIC))
                .withoutProcedureColumnMetaDataAccess();
    }

    private void buildFuncCall() {
        funcCall = new SimpleJdbcCall(jdbcTemplate)
                .withCatalogName("TEST_PKG")
                .withFunctionName("INSERT_DATA")
                .declareParameters(
                        //IMPORTANT !!!!! We have to declare returned results as OUT parameter FIRST !!!
                        new SqlOutParameter("result", Types.NUMERIC),
                        new SqlParameter("P_K012_TITLE", Types.VARCHAR)
                )
                .withoutProcedureColumnMetaDataAccess()
                .withReturnValue();
    }

    private void buildFunctionUsingMetadata() {
        funcCallUsingMetadata = new SimpleJdbcCall(jdbcTemplate)
                .withCatalogName("TEST_PKG")
                .withSchemaName(schemaName)
                .withFunctionName("INSERT_DATA")
                .withReturnValue();
    }

}
