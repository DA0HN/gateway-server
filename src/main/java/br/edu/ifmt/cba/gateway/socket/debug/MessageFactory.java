package br.edu.ifmt.cba.gateway.socket.debug;

import br.edu.ifmt.cba.gateway.socket.MessageQueue;
import br.edu.ifmt.cba.gateway.utils.Logger;

import java.util.concurrent.TimeUnit;

/**
 * @author daohn on 24/08/2020
 * @project socket_java
 */
public class MessageFactory extends Thread {

    private static int          i = 0;
    private final  MessageQueue senderQueue;
    private final  Logger       logger;

    public MessageFactory(MessageQueue senderQueue) {
        this.senderQueue = senderQueue;
        this.logger      = new Logger();
    }


    @Override public void run() {
        while(true) {
            try {
                long timestamp = System.currentTimeMillis();
                var msg = "b8:27:eb:8e:94:f2!3c:71:bf:5a:b3:48!Mensagem_" + (i++) + "!"
                        + timestamp;
                logger.log("Mensagem criada: " + msg);

                senderQueue.enqueue(msg);
                TimeUnit.SECONDS.sleep(10);
            }
            catch(InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}

