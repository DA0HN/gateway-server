package br.edu.ifmt.cba.gateway.modules.debug;

import br.edu.ifmt.cba.gateway.model.DebugData;
import br.edu.ifmt.cba.gateway.socket.MessageQueue;

/**
 * @author daohn on 21/09/2020
 * @project gateway_server
 */
public class DebugBroadcaster {

    private final MessageQueue senderQueue;

    public DebugBroadcaster(MessageQueue senderQueue) {
        this.senderQueue = senderQueue;
    }

    public void createReply(DebugData debugData) {
        var confirmMessage = debugData.getTo() + "!" + debugData.getFrom() + "!" +  "OK";
        long raw = System.currentTimeMillis();
        var anotherMessage = "b8:27:eb:8e:94:f2" + "!" + "3c:71:bf:5a:b3:48!b8:27" + "!" + raw;
        senderQueue.enqueue(confirmMessage);
        senderQueue.enqueue(anotherMessage);
    }
}
