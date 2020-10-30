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


    // flags
    private boolean newStatus = true;
    private boolean redundantStatus = false;
    private boolean allStatus = false;

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

            var elapsedTimeInSecond = (double)data.getElapsedTime()/1000;
            if(elapsedTimeInSecond > 30 || elapsedTimeInSecond < -30)
                throw new ProtocolException("A mensagem: "+ message +" não possui um timestamp " +
                                                    "valido!");

            if(NEW.equals(status)) {
                service.save(data);
                broadcaster.createReply(data);
            }
        }
        catch(DatabaseException | ProtocolException e) {
            status = Status.CORRUPTED;
            logger.log("\t" + e.getMessage(), System.err);
        }
        catch(Exception e) {
            status = Status.ERROR;
            logger.log("\t" + e.getMessage(), System.err);
        }
        finally {
            logMessage(message, status);
        }
    }


    private void logMessage(String message, Status status) {
        if((newStatus && NEW.equals(status)) || allStatus) {
            logger.log(protocol.getMessageStatistics());
            logger.log("\tMensagem: " + message + "\n\t\t\t\t\tStatus: " + status);
        }

        if((redundantStatus && REDUNDANT.equals(status)) || allStatus) {
            logger.log(protocol.getMessageStatistics());
            logger.log("\tMensagem: " + message + "\n\t\t\t\t\tStatus: " + status);
        }
    }
}
