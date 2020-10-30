package br.edu.ifmt.cba.gateway.modules.air_conditioner;

import br.edu.ifmt.cba.gateway.database.DatabaseException;
import br.edu.ifmt.cba.gateway.model.AirConditionerData;
import br.edu.ifmt.cba.gateway.modules.IModule;
import br.edu.ifmt.cba.gateway.protocol.receive.ProtocolException;
import br.edu.ifmt.cba.gateway.protocol.send.Status;
import br.edu.ifmt.cba.gateway.socket.MessageQueue;
import br.edu.ifmt.cba.gateway.utils.Logger;

import javax.persistence.EntityManager;

import static br.edu.ifmt.cba.gateway.protocol.send.Status.NEW;
import static br.edu.ifmt.cba.gateway.protocol.send.Status.REDUNDANT;

/**
 * @author daohn on 29/10/2020
 * @project gateway_server
 */
public class AirConditionerModule implements IModule {

    private final Logger                          logger;
    private final AirConditionerProtocol          protocol;
    private final AirConditionerDataService       service;
    private final AirConditionerBroadcaster       broadcaster;
    private final AirConditionerMessageIdentifier identifier;

    public AirConditionerModule(EntityManager manager, MessageQueue senderQueue) {
        this.logger      = new Logger();
        this.protocol    = new AirConditionerProtocol();
        this.service     = new AirConditionerDataService(manager);
        this.broadcaster = new AirConditionerBroadcaster(senderQueue);
        this.identifier  = new AirConditionerMessageIdentifier();
    }

    @Override public void execute(String message) {
        Status status = null;
        try {
            identifier.identify(message);
            var data = (AirConditionerData) protocol.parse(message);

            status = service.getStatus(data);
            if(REDUNDANT.equals(status)) {
                service.save(data);
            }

            if(NEW.equals(status)) {
                broadcaster.createReply(data);
            }
        }
        catch(DatabaseException | ProtocolException e) {
            status = Status.CORRUPTED;
            System.err.println(e.getMessage());
        }
        catch(Exception e) {
            status = Status.ERROR;
            System.err.println(e.getMessage());
        }
        finally {
            logger.log("\n\tMensagem: " + message + "\n\tStatus: " + status);
        }
    }
}
