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

    private final Logger                 logger;
    private final DebugProtocol          protocol;
    private final DebugDataService       service;
    private final DebugBroadcaster       broadcaster;
    private final DebugMessageIdentifier identifier;

    public DebugModule(EntityManager manager, MessageQueue senderQueue) {
        this.broadcaster = new DebugBroadcaster(senderQueue);
        this.identifier  = new DebugMessageIdentifier();
        this.protocol    = new DebugProtocol();
        this.service     = new DebugDataService(manager);
        this.logger      = new Logger();
    }

    /**
     * @param message mensagem recebida
     */
    @Override public void execute(String message) {
        // b8:27:eb:8e:94:f2 ! b8:27:eb:8e:94:f2 ! project ! msg !...
        Status status = null;
        try {
            // analisa se a mensagem não possui nenhuma deformação
            // caso a mensagem contenha alguma deformação
            // gera uma exceção e muda o status para CORRUPTED
            identifier.identify(message);
            // cria um objeto DebugData com os dados contidos na string
            var data = (DebugData) protocol.parse(message);
            // checa se a mensagem já foi inserida no banco e armazena seu status
            status = service.getStatus(data);
            // insere a mensagem no banco
            service.save(data);
            // caso o status seja NEW cria um reply para o gateway
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
