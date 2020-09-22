package br.edu.ifmt.cba.gateway.modules.debug;

import br.edu.ifmt.cba.gateway.database.DatabaseException;
import br.edu.ifmt.cba.gateway.model.DebugData;
import br.edu.ifmt.cba.gateway.protocol.send.Status;

import javax.persistence.EntityManager;

import static br.edu.ifmt.cba.gateway.protocol.send.Status.NEW;
import static br.edu.ifmt.cba.gateway.protocol.send.Status.REDUNDANT;

/**
 * @author daohn on 14/08/2020
 * @project gateway_server
 */
public class DebugDataService {

    private final EntityManager manager;

    public DebugDataService(EntityManager manager) {
        this.manager = manager;
    }

    /**
     * @param data objeto que será persistido
     * @throws DatabaseException lança exceção caso a persistência não seja executada com sucesso
     */
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
            throw new DatabaseException("Não foi possível persistir: " + e.getMessage());
        }
    }

    /**
     * Consulta o banco de dados baseado no timestamp que está contido na mensagem e é
     * chave primária do objeto no banco, caso a consulta seja nulo retorna NEW caso contrário
     * REDUNDANT
     * @param data objeto que será consultado
     * @return status de confirmação NEW se o objeto não está no banco
     * ou REDUNDANT se o objeto já foi inserido
     */
    public Status getStatus(DebugData data) {
        if(findById(data) == null) {
            return NEW;
        }
        else {
            return REDUNDANT;
        }
    }

    /**
     * @param data objeto a ser consultado
     * @return retorna nulo ou o objeto encontrado
     */
    public DebugData findById(DebugData data) {
        var query = manager.createQuery(
                "SELECT data FROM DebugData data WHERE data.raw =: raw",
                DebugData.class
        );
        query.setParameter("raw", data.getRaw());
        var result = query.getResultList();
        if(result.isEmpty()) {
            return null;
        }
        return result.get(0);
    }
}
