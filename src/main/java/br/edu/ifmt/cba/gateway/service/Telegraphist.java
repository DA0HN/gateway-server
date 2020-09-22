package br.edu.ifmt.cba.gateway.service;

import br.edu.ifmt.cba.gateway.modules.ModuleException;
import br.edu.ifmt.cba.gateway.modules.ModuleFactory;
import br.edu.ifmt.cba.gateway.socket.MessageQueue;

/**
 * @author daohn on 21/09/2020
 * @project gateway_server
 */
public class Telegraphist extends Thread {

    private final MessageQueue  queue;
    private final MessageQueue  senderQueue;
    private final ModuleFactory factory;

    public Telegraphist(MessageQueue receiverQueue, MessageQueue senderQueue,
                        ModuleFactory factory) {
        this.queue       = receiverQueue;
        this.senderQueue = senderQueue;
        this.factory     = factory;
    }

    @Override public void run() {
        //noinspection InfiniteLoopStatement
        while(true) {
            try {
                String message = queue.dequeue();
                String project = message.split("!")[2];
                var module = factory.createProtocol(project);
                module.execute(message);
            }
            catch(ModuleException e) {
                e.printStackTrace();
            }
        }
    }
}
