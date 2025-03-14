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
     * Creates a new table for a unique organization.
     */
    @Transactional
    public void createOrganizationTable(String organizationName) {
        String tableName = generateTableName(organizationName);

        String sql = "CREATE TABLE IF NOT EXISTS " + tableName + " (" +
                "id BIGINT AUTO_INCREMENT PRIMARY KEY, " +
                "position VARCHAR(255), " +
                "balance DOUBLE)";

        entityManager.createNativeQuery(sql).executeUpdate();
    }

    /**
     * Inserts a new record into the organization's table.
     */
    @Transactional
    public void insertIntoOrganization(String organizationName, String position, Double balance) {
        String tableName = generateTableName(organizationName);

        String sql = "INSERT INTO " + tableName + " (position, balance) VALUES (:position, :balance)";

        entityManager.createNativeQuery(sql)
                .setParameter("position", position)
                .setParameter("balance", balance)
                .executeUpdate();
    }

    /**
     * Retrieves all records from a specific organization's table.
     */
    public List<Object[]> fetchOrganizationData(String organizationName) {
        String tableName = generateTableName(organizationName);

        String sql = "SELECT * FROM " + tableName;

        return entityManager.createNativeQuery(sql).getResultList();
    }

    /**
     * Helper method to sanitize and generate valid table names.
     */
    private String generateTableName(String organizationName) {
        return "org_" + organizationName.replaceAll("\\s", "_").toLowerCase();
    }
}
