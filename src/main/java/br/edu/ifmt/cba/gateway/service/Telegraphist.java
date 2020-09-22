package br.edu.ifmt.cba.gateway.service;

import br.edu.ifmt.cba.gateway.modules.ModuleException;
import br.edu.ifmt.cba.gateway.modules.ModuleFactory;
import br.edu.ifmt.cba.gateway.socket.MessageQueue;

/**
 * @author daohn on 21/09/2020
 * @project gateway_server
 */

/**
 * Thread responsável por receber todas as mensagens que chegam
 * e são inseridas na fila de recebimento (receiverQueue)
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
                // retira a mensagem a da fila
                String message = queue.dequeue();
                // extrai o id do projeto da mensagem
                String project = message.split("!")[2];
                // de acordo com o projeto extraido gera um protocolo para tratar a mensagem
                var module = factory.createProtocol(project);
                // executa o tratamento
                module.execute(message);
            }
            catch(ModuleException e) {
                e.printStackTrace();
            }
        }
    }
}
