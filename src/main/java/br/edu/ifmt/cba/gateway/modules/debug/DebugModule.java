package br.edu.ifmt.cba.gateway.modules.debug;

import br.edu.ifmt.cba.gateway.database.DatabaseException;
import br.edu.ifmt.cba.gateway.model.DebugData;
import br.edu.ifmt.cba.gateway.modules.IModule;
import br.edu.ifmt.cba.gateway.protocol.receive.ProtocolException;
import br.edu.ifmt.cba.gateway.protocol.send.Status;
import br.edu.ifmt.cba.gateway.socket.MessageQueue;
import br.edu.ifmt.cba.gateway.utils.Logger;

import javax.persistence.EntityManager;

import static br.edu.ifmt.cba.gateway.protocol.send.Status.NEW;

/**
 * @author daohn on 21/09/2020
 * @project gateway_server
 */
public class DebugModule implements IModule {

    private final Logger           logger;
    private final DebugProtocol    protocol;
    private final DebugDataService service;
    private final DebugBroadcaster broadcaster;

    public DebugModule(EntityManager manager, MessageQueue senderQueue) {
        this.broadcaster = new DebugBroadcaster(senderQueue);
        this.protocol    = new DebugProtocol(new DebugMessageIdentifier());
        this.service     = new DebugDataService(manager);
        this.logger      = new Logger();
    }

    @Override public void execute(String message) {
        Status status = null;
        try {
            var data = (DebugData) protocol.parse(message);
            status = service.getStatus(data);
            service.save(data);
            if(NEW.equals(status)) {
                broadcaster.createReply(data);
            }
        }
        catch(DatabaseException | ProtocolException e) {
            status = Status.CORRUPTED;
            System.err.println(e.getMessage());
        }
        finally {
            logger.log("\n\tMensagem: " + message + "\n\tStatus: " + status);
        }
    }
}
