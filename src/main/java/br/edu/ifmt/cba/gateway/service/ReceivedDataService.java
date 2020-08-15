package br.edu.ifmt.cba.gateway.service;

import br.edu.ifmt.cba.gateway.model.ReceivedData;
import br.edu.ifmt.cba.gateway.utils.Logger;

import javax.persistence.EntityManager;
import java.time.LocalDateTime;

/**
 * @author daohn on 14/08/2020
 * @project gateway_server
 */
public class ReceivedDataService {

    private final EntityManager manager;
    private final Protocol protocol;

    public ReceivedDataService(EntityManager manager, Protocol protocol) {
        this.manager = manager;
        this.protocol = protocol;
    }

    public boolean save(String data, LocalDateTime time) {
        try {
            ReceivedData receivedData = protocol.parse(data, time);
            manager.getTransaction().begin();
            manager.persist(receivedData);
            manager.getTransaction().commit();
            return true;
        }
        catch(ProtocolException e) {
            Logger.log(e.getMessage());
            return false;
        }
    }
}
