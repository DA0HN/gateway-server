package br.edu.ifmt.cba.gateway.modules.air_conditioner;

import br.edu.ifmt.cba.gateway.database.DatabaseException;
import br.edu.ifmt.cba.gateway.model.AirConditionerData;
import br.edu.ifmt.cba.gateway.protocol.send.Status;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;

import static br.edu.ifmt.cba.gateway.protocol.send.Status.NEW;
import static br.edu.ifmt.cba.gateway.protocol.send.Status.REDUNDANT;

/**
 * @author daohn on 29/10/2020
 * @project gateway_server
 */
public class AirConditionerDataService {
    private final EntityManager manager;

    public AirConditionerDataService(EntityManager manager) {
        this.manager = manager;
    }

    /**
     * @param data objeto que será persistido
     * @throws DatabaseException lança exceção caso a persistência não seja executada com sucesso
     */
    public void save(AirConditionerData data) throws DatabaseException {
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
     * Consulta o banco de dados baseado no timestamp que está contido na mensagem e é chave
     * primária do objeto no banco, caso a consulta seja nulo retorna NEW caso contrário REDUNDANT
     *
     * @param data objeto que será consultado
     * @return status de confirmação NEW se o objeto não está no banco ou REDUNDANT se o objeto já
     * foi inserido
     */
    public Status getStatus(AirConditionerData data) {
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
    public AirConditionerData findById(AirConditionerData data) {
        try {
            var query = manager.createQuery(
                    "SELECT data FROM AirConditionerData data WHERE data.timestamp =: timestamp",
                    AirConditionerData.class
            );
            query.setParameter("timestamp", data.getTimestamp());
            return query.getSingleResult();
        }
        catch(NoResultException e) {
            return null;
        }
    }
}
