package br.edu.ifmt.cba.gateway.modules;

import br.edu.ifmt.cba.gateway.database.DatabaseConnection;
import br.edu.ifmt.cba.gateway.database.DatabaseException;
import br.edu.ifmt.cba.gateway.modules.air_conditioner.AirConditionerModule;
import br.edu.ifmt.cba.gateway.modules.debug.DebugModule;
import br.edu.ifmt.cba.gateway.socket.MessageQueue;

import static br.edu.ifmt.cba.gateway.modules.ModuleId.AIR_CONDITIONER;
import static br.edu.ifmt.cba.gateway.modules.ModuleId.DEBUG;


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

    /**
     *
     * @param project id do projeto
     * @return módulo que possui as regras para tratar a mensagem que chegou
     * @throws ModuleException lança exceção caso o projeto não seja reconhecido ou caso
     * ocorra algum erro durante a instanciação do módulo
     */
    public IModule createProtocol(String project) throws ModuleException {
        try {
            switch(project) {
                case DEBUG:
                    return new DebugModule(DatabaseConnection.getEntityManager(), senderQueue);
                case AIR_CONDITIONER:
                    return new AirConditionerModule(DatabaseConnection.getEntityManager(), senderQueue);
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
