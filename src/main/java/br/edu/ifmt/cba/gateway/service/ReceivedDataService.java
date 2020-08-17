package br.edu.ifmt.cba.gateway.service;

import br.edu.ifmt.cba.gateway.model.ReceivedData;
import br.edu.ifmt.cba.gateway.protocol.Protocol;
import br.edu.ifmt.cba.gateway.protocol.ProtocolException;
import br.edu.ifmt.cba.gateway.protocol.Response;
import br.edu.ifmt.cba.gateway.protocol.Status;
import br.edu.ifmt.cba.gateway.utils.Logger;

import javax.persistence.EntityManager;
import java.time.LocalDateTime;
import java.util.Optional;

import static br.edu.ifmt.cba.gateway.protocol.Status.CORRUPTED;
import static br.edu.ifmt.cba.gateway.protocol.Status.NEW;
import static br.edu.ifmt.cba.gateway.protocol.Status.REDUNDANT;

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

    public Response save(String data, LocalDateTime time) {
        Response response;
        try {
            ReceivedData receivedData = protocol.parse(data, time);

            if(findById(receivedData).isPresent()) {
                response = generateResponseMessage(NEW, receivedData);
            } else {
                response = generateResponseMessage(REDUNDANT, receivedData);
            }
            manager.getTransaction().begin();
            manager.merge(receivedData);
            manager.getTransaction().commit();
        }
        catch(ProtocolException e) {
            Logger.log(e.getMessage());
            response = generateResponseMessage(CORRUPTED, null);
        }
        return response;
    }

    public Optional<ReceivedData> findById(ReceivedData receivedData) {
        return Optional.ofNullable(manager.find(receivedData.getClass(), receivedData.getRaw()));
    }

    private Response generateResponseMessage(Status status, ReceivedData receivedData) {
        var response = new Response();
        switch(status) {
            case NEW:
                response.confirmMessage = receivedData.getTo() + "!" + receivedData.getFrom() + "!" + "OK";
                long raw = System.currentTimeMillis();
                response.message = "b8:27:eb:8e:94:f2" + "!" + "3c:71:bf:5a:b3:48!b8:27" + "!" + raw;
                response.status = NEW;
                break;
            case CORRUPTED:
                response.message = "CORROMPIDO";
                response.status = CORRUPTED;
            case REDUNDANT:
                response.message = "REDUNDANTE";
                response.status = REDUNDANT;
        }
        return response;
    }
}
