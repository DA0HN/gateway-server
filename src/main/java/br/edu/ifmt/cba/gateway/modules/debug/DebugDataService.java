package br.edu.ifmt.cba.gateway.modules.debug;

import br.edu.ifmt.cba.gateway.database.DatabaseException;
import br.edu.ifmt.cba.gateway.model.DebugData;
import br.edu.ifmt.cba.gateway.protocol.send.Status;
import br.edu.ifmt.cba.gateway.utils.Logger;

import javax.persistence.EntityManager;

import static br.edu.ifmt.cba.gateway.protocol.send.Status.NEW;
import static br.edu.ifmt.cba.gateway.protocol.send.Status.REDUNDANT;

/**
 * @author daohn on 14/08/2020
 * @project gateway_server
 */
public class DebugDataService {

    private final EntityManager manager;
    private final Logger        logger = new Logger();

    public DebugDataService(EntityManager manager) {
        this.manager = manager;
    }

    public void save(DebugData data) throws DatabaseException {
        try {
            if(findById(data) != null) {
                manager.getTransaction().begin();
                manager.persist(data);
            }
            else {
                manager.getTransaction().begin();
                manager.merge(data);
            }
            manager.getTransaction().commit();
        }
        catch(Exception e) {
            manager.getTransaction().rollback();
            throw new DatabaseException("Não foi possível persistir");
        }
    }

    public Status getStatus(DebugData data) {
        if(findById(data) == null) {
            return NEW;
        }
        else {
            return REDUNDANT;
        }
    }

    public DebugData findById(DebugData debugData) {

        var query = manager.createQuery(
                "SELECT data FROM DebugData data WHERE data.raw =: raw",
                DebugData.class
        );
        query.setParameter("raw", debugData.getRaw());
        var result = query.getResultList();
        if(result.isEmpty()) {
            return null;
        }
        return result.get(0);
    }
}
