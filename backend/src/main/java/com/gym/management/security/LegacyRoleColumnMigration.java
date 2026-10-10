package com.gym.management.security;

import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.jdbc.core.JdbcTemplate;
import javax.sql.DataSource;

/** Safely widens the old H2 ADMIN/STAFF enum column before MEMBER accounts are used. */
@Configuration
public class LegacyRoleColumnMigration {
    @Bean @Order(Ordered.HIGHEST_PRECEDENCE)
    ApplicationRunner widenLegacyRoleColumn(DataSource dataSource, JdbcTemplate jdbc) {
        return args -> {
            try (var connection=dataSource.getConnection()) {
                if (!connection.getMetaData().getDatabaseProductName().equalsIgnoreCase("H2")) return;
                try (var columns=connection.getMetaData().getColumns(connection.getCatalog(),null,"GYM_USERS","ROLE")) {
                    if (columns.next()) {
                        String type=columns.getString("TYPE_NAME");
                        if (type!=null&&type.toUpperCase(java.util.Locale.ROOT).startsWith("ENUM")) {
                            jdbc.execute("alter table gym_users alter column role varchar(20)");
                        }
                    }
                }
            }
        };
    }
}
