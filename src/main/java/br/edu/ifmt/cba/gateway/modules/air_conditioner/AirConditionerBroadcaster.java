package br.edu.ifmt.cba.gateway.modules.air_conditioner;

import br.edu.ifmt.cba.gateway.model.AirConditionerData;
import br.edu.ifmt.cba.gateway.socket.MessageQueue;

import java.util.StringJoiner;

import static br.edu.ifmt.cba.gateway.modules.ModuleId.AIR_CONDITIONER;
import static br.edu.ifmt.cba.gateway.utils.Constants.GATEWAY_MAC;

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
        StringJoiner message = new StringJoiner("!");
        message.add(GATEWAY_MAC.getData());
        message.add(data.getSource());
        message.add(AIR_CONDITIONER);
        message.add(data.getTimestamp().toString());
        message.add("OK");
        this.senderQueue.enqueue(message.toString());
    }
}
