package com.example.demo.Service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrganizationTableService {

    @PersistenceContext
    private EntityManager entityManager;

    /**
     * Creates (if doesn't exist) a new org table for the given base name.
     */
    @Transactional
    public void createOrganizationTable(String organizationName) {
        // Make sure we generate the actual table name with only one "org_" prefix
        String tableName = generateTableName(organizationName);

        String sql = "CREATE TABLE IF NOT EXISTS " + tableName + " (" +
                "id BIGINT PRIMARY KEY, " +
                "username VARCHAR(255), " +
                "organization VARCHAR(255))";

        entityManager.createNativeQuery(sql).executeUpdate();
    }

    /**
     * Inserts (id, username, organizationName) into the correct "org_xxx" table.
     */
    @Transactional
    public boolean insertIntoOrganization(String organizationName, String username, Long id) {
        String tableName = generateTableName(organizationName);

        // Ensure the table exists before inserting data
        createOrganizationTable(organizationName);

        try {
            String sql = "INSERT INTO " + tableName + " (id, username, organization) " +
                    "VALUES (:id, :username, :organization)";

            entityManager.createNativeQuery(sql)
                    .setParameter("id", id)
                    .setParameter("username", username)
                    .setParameter("organization", organizationName)  // store the "clean" base name or original input
                    .executeUpdate();

            return true;
        } catch (Exception e) {
            System.err.println("❌ Error inserting into " + tableName + ": " + e.getMessage());
            return false;
        }
    }

    /**
     * Fetches (id, username) from the given "org_xxx" table.
     */
    public List<Object[]> fetchAllUsersByOrganization(String organizationName) {
        String tableName = generateTableName(organizationName);

        try {
            String sql = "SELECT id, username FROM " + tableName;
            return entityManager.createNativeQuery(sql).getResultList();
        } catch (Exception e) {
            System.err.println("❌ Error fetching users from " + tableName + ": " + e.getMessage());
            return List.of(); // Return empty if table doesn't exist
        }
    }

    /**
     * Returns all "org_*" table names in the database.
     */
    @Transactional
    public List<String> getAllOrganizationTables() {
        String sql = "SELECT table_name FROM information_schema.tables " +
                "WHERE table_schema = DATABASE() AND table_name LIKE 'org_%'";

        return entityManager.createNativeQuery(sql).getResultList();
    }

    /**
     * Ensures we only ever have one "org_" prefix.
     */
    private String generateTableName(String organizationName) {
        if (organizationName == null || organizationName.trim().isEmpty()) {
            throw new IllegalArgumentException("Organization name cannot be null or empty");
        }

        // 1) Convert to lowercase & trim standard spaces
        String sanitized = organizationName.trim().toLowerCase();

        // 2) Replace all non-alphanumeric (or underscore) chars with '_'
        //    e.g. "man," -> "man_"
        sanitized = sanitized.replaceAll("[^a-z0-9_]", "_");

        // 3) Remove trailing underscores
        //    e.g. "man_" -> "man"
        sanitized = sanitized.replaceAll("_+$", "");

        // 4) If it already starts with "org_", return as is
        if (sanitized.startsWith("org_")) {
            return sanitized;
        }

        // 5) Otherwise, prepend "org_"
        return "org_" + sanitized;
    }

}
