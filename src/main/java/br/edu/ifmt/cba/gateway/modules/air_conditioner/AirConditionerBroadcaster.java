package br.edu.ifmt.cba.gateway.modules.air_conditioner;

import br.edu.ifmt.cba.gateway.model.AirConditionerData;
import br.edu.ifmt.cba.gateway.socket.MessageQueue;

/**
 * @author daohn on 29/10/2020
 * @project gateway_server
 */
public class AirConditionerBroadcaster {

    private final MessageQueue senderQueue;

    public AirConditionerBroadcaster(MessageQueue senderQueue) {
        this.senderQueue = senderQueue;
    }

    public void createReply(AirConditionerData data) {

    }
}
