package br.edu.ifmt.cba.gateway.modules.debug;

import br.edu.ifmt.cba.gateway.model.DebugData;
import br.edu.ifmt.cba.gateway.socket.MessageQueue;

import static br.edu.ifmt.cba.gateway.utils.Constants.GATEWAY_MAC;

/**
 * @author daohn on 21/09/2020
 * @project gateway_server
 */
public class DebugBroadcaster {

    private final MessageQueue senderQueue;

    public DebugBroadcaster(MessageQueue senderQueue) {
        this.senderQueue = senderQueue;
    }

    /**
     * cria uma resposta para o gateway baseado na mensagem que recebeu,
     * a primeira é uma confirmação para o dispositivo que enviou e o segundo
     * para fins de teste gera uma mensagem para um dispositivo em especifico
     * @param debugData objeto que contém os dados da mensagem que chegou
     */
    public void createReply(DebugData debugData) {
        var confirmMessage = debugData.getTo() + "!" + debugData.getFrom() + "!" +  "OK";
        long raw = System.currentTimeMillis();
        var anotherMessage = GATEWAY_MAC + "!" + "3c:71:bf:5a:b3:48!b8:27" + "!" + raw;
        senderQueue.enqueue(confirmMessage);
        senderQueue.enqueue(anotherMessage);
    }
}
