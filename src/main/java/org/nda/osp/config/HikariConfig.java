package org.nda.osp.config;

import com.zaxxer.hikari.HikariDataSource;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
@ConditionalOnProperty(value="manual", prefix = "spring.datasource.hikari", havingValue = "true")
public class HikariConfig {

    @Bean
    public DataSource dataSource() {
        HikariDataSource dataSource = new HikariDataSource();
        dataSource.setAutoCommit(false);
        dataSource.setMaximumPoolSize(1);
        dataSource.setMinimumIdle(1);
        dataSource.setSchema("TEST_USER");
        dataSource.setUsername("JAVA_TEST");
        dataSource.setPassword("JAVA_TEST");
        dataSource.setDriverClassName("oracle.jdbc.OracleDriver");
        dataSource.setJdbcUrl("jdbc:oracle:thin:@//localhost:1521/FREEPDB1");
        return dataSource;
    }
}
