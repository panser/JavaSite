package ua.org.gostroy.internal.security.acl;

import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.util.FileCopyUtils;

import java.io.IOException;

/**
 * Created by panser on 5/31/2014.
 */
public class DatabaseSeeder {
    public DatabaseSeeder(JdbcTemplate jdbcTemplate) throws IOException {
        String sql = new String(
                FileCopyUtils.copyToByteArray(
                        new ClassPathResource("db/spring-security-acl.sql").getInputStream()
                )
        );
        jdbcTemplate.execute(sql);
    }
}