package br.edu.ifmt.cba.gateway.service;

import br.edu.ifmt.cba.gateway.model.GenericReceivedData;
import br.edu.ifmt.cba.gateway.protocol.receive.IReceiveProtocol;
import br.edu.ifmt.cba.gateway.protocol.receive.ProtocolException;
import br.edu.ifmt.cba.gateway.protocol.send.IMessageToSend;
import br.edu.ifmt.cba.gateway.protocol.send.Response;
import br.edu.ifmt.cba.gateway.protocol.send.Status;
import br.edu.ifmt.cba.gateway.utils.Logger;

import javax.persistence.EntityManager;
import java.time.LocalDateTime;
import java.util.Optional;

import static br.edu.ifmt.cba.gateway.protocol.send.Status.CORRUPTED;
import static br.edu.ifmt.cba.gateway.protocol.send.Status.NEW;
import static br.edu.ifmt.cba.gateway.protocol.send.Status.REDUNDANT;

/**
 * @author daohn on 14/08/2020
 * @project gateway_server
 */
public class ReceivedDataService {

    private final EntityManager manager;
    private final IReceiveProtocol protocol;

    public ReceivedDataService(EntityManager manager, IReceiveProtocol protocol) {
        this.manager = manager;
        this.protocol = protocol;
    }

    public IMessageToSend save(String data, LocalDateTime time) {
        IMessageToSend response;
        try {
            GenericReceivedData receivedData = (GenericReceivedData) protocol.parse(data, time);

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

    public Optional<GenericReceivedData> findById(GenericReceivedData genericReceivedData) {
        return Optional.ofNullable(manager.find(genericReceivedData.getClass(), genericReceivedData.getRaw()));
    }

    private IMessageToSend generateResponseMessage(Status status, GenericReceivedData genericReceivedData) {
        var response = new Response();
        switch(status) {
            case NEW:
                response.setConfirmMessage(genericReceivedData.getTo() + "!" + genericReceivedData.getFrom() +
                        "!" + "OK");
                long raw = System.currentTimeMillis();
                response.setMessage("b8:27:eb:8e:94:f2" + "!" + "3c:71:bf:5a:b3:48!b8:27" + "!" + raw);
                response.setStatus(NEW);
                break;
            case CORRUPTED:
                response.setMessage("CORROMPIDO");
                response.setStatus(CORRUPTED);
            case REDUNDANT:
                response.setMessage("REDUNDANTE");
                response.setStatus(REDUNDANT);
        }
        return response;
    }
}
