package com.pchomond.persona.testconfig;

import jakarta.persistence.EntityManagerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DatabaseCleaner {

    @Autowired
    private EntityManagerFactory entityManagerFactory;

    /**
     * Truncate all DB tables associated with declared JPA entities.
     * <p>
     *     Note: If lookup/static tables need to be omitted, a more granular approach needs to be taken
     *     specifying which tables need to be truncated using the JdbcTemplate to write manual SQL.
     * </p>
     */
    public void clearDatabase() {
        entityManagerFactory.getSchemaManager().truncate();
    }
}
