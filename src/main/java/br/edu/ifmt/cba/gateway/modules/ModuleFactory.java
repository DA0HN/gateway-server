package br.edu.ifmt.cba.gateway.modules;

import br.edu.ifmt.cba.gateway.database.DatabaseConnection;
import br.edu.ifmt.cba.gateway.database.DatabaseException;
import br.edu.ifmt.cba.gateway.modules.debug.DebugModule;
import br.edu.ifmt.cba.gateway.socket.MessageQueue;
import com.google.protobuf.Message;

/**
 * @author daohn on 21/09/2020
 * @project gateway_server
 */
public class ModuleFactory {

    private final MessageQueue receiverQueue;
    private final MessageQueue senderQueue;

    public ModuleFactory(MessageQueue receiverQueue, MessageQueue senderQueue) {
        this.receiverQueue = receiverQueue;
        this.senderQueue   = senderQueue;
    }

    public IModule createProtocol(String project) throws ModuleException {
        try {
            switch(project) {
                case "dbg":
                    return new DebugModule(DatabaseConnection.getEntityManager(), senderQueue);
                default:
                    throw new ModuleException("Project not recognized");
            }
        }
        catch(DatabaseException e) {
            e.printStackTrace();
            throw new ModuleException(e.getMessage());
        }
    }
}
