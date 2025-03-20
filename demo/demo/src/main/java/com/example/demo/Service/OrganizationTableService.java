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

    @Transactional
    public void createOrganizationTable(String organizationName) {

        String tableName = generateTableName(organizationName);

        String sql = "CREATE TABLE IF NOT EXISTS " + tableName + " (" +
                "id BIGINT PRIMARY KEY, " +
                "username VARCHAR(255), " +
                "organization VARCHAR(255))";

        entityManager.createNativeQuery(sql).executeUpdate();
    }

    @Transactional
    public boolean insertIntoOrganization(String organizationName, String username, Long id) {
        String tableName = generateTableName(organizationName);

        createOrganizationTable(organizationName);

        try {
            String sql = "INSERT INTO " + tableName + " (id, username, organization) " +
                    "VALUES (:id, :username, :organization)";

            entityManager.createNativeQuery(sql)
                    .setParameter("id", id)
                    .setParameter("username", username)
                    .setParameter("organization", organizationName)
                    .executeUpdate();

            return true;
        } catch (Exception e) {
            System.err.println("❌ Error inserting into " + tableName + ": " + e.getMessage());
            return false;
        }
    }
    public List fetchAllUsersByOrganization(String organizationName) {
        String tableName = generateTableName(organizationName);

        try {
            String sql = "SELECT id, username FROM " + tableName;
            System.out.println(1111);
            List<Object[]> results = entityManager.createNativeQuery(sql).getResultList();
            System.out.println("🔹 Raw SQL Results:");
            for (Object[] row : results) {
                System.out.println("ID: " + row[0] + ", Username: " + row[1]);
            }
            return entityManager.createNativeQuery(sql).getResultList();
        } catch (Exception e) {
            System.err.println("❌ Error fetching users from " + tableName + ": " + e.getMessage());
            return List.of();
        }
    }


    @Transactional
    public List<String> getAllOrganizationTables() {
        String sql = "SELECT table_name FROM information_schema.tables " +
                "WHERE table_schema = DATABASE() AND table_name LIKE 'org_%'";

        return entityManager.createNativeQuery(sql).getResultList();
    }

    private String generateTableName(String organizationName) {
        if (organizationName == null || organizationName.trim().isEmpty()) {
            throw new IllegalArgumentException("Organization name cannot be null or empty");
        }


        String sanitized = organizationName.trim().toLowerCase();

        sanitized = sanitized.replaceAll("[^a-z0-9_]", "_");

        sanitized = sanitized.replaceAll("_+$", "");


        if (sanitized.startsWith("org_")) {
            return sanitized;
        }

        return "org_" + sanitized;
    }

}
