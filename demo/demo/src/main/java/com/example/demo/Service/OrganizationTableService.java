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
                "organization VARCHAR(255))";  // Fix: Closing parenthesis

        entityManager.createNativeQuery(sql).executeUpdate();
    }

    @Transactional
    public boolean insertIntoOrganization(String organizationName, String username, Long id) {
        String tableName = generateTableName(organizationName);

        // Ensure the table exists before inserting data
        createOrganizationTable(organizationName);

        String sql = "INSERT INTO " + tableName + " (id, username, organization) VALUES (:id, :username, :organization)";

        entityManager.createNativeQuery(sql)
                .setParameter("id", id)
                .setParameter("username", username)
                .setParameter("organization", organizationName)
                .executeUpdate();

        return true;
    }

//    public List<Object[]> fetchOrganizationData(String organizationName) {
//        String tableName = generateTableName(organizationName);
//
//        try {
//            String sql = "SELECT * FROM " + tableName;
//            return entityManager.createNativeQuery(sql).getResultList();
//        } catch (Exception e) {
//            System.err.println("Error fetching data from " + tableName + ": " + e.getMessage());
//            return List.of();
//        }
//    }


    private String generateTableName(String organizationName) {
        return "org_" + organizationName.replaceAll("[^a-zA-Z0-9_]", "_").toLowerCase();  // Fix: Sanitize input
    }

    public List<Object[]> fetchAllUsersByOrganization(String organizationName) {
        String tableName = generateTableName(organizationName);

        try {
            String sql = "SELECT id, username FROM " + tableName;
            return entityManager.createNativeQuery(sql).getResultList();
        } catch (Exception e) {
            System.err.println("Error fetching users from " + tableName + ": " + e.getMessage());
            return List.of();  // Return empty list if table does not exist
        }
    }
}
