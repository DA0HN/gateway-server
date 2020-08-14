package br.edu.ifmt.cba.gateway.database;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 * @author daohn on 14/08/2020
 * @project gateway_server
 */
public class DatabaseConnection {
    private static EntityManagerFactory factory = null;

    static {
        factory = Persistence.createEntityManagerFactory("Gateway");
    }

    private DatabaseConnection() {
    }

    public static EntityManager getEntityManager() throws DatabaseException {
        if(factory == null) {
            throw new DatabaseException("Unidade de persistência não iniciada");
        }
        return factory.createEntityManager();
    }
}
