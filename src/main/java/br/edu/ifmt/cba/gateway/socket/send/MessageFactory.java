package br.edu.ifmt.cba.gateway.socket.send;

import br.edu.ifmt.cba.gateway.socket.MessageQueue;

import java.util.concurrent.locks.ReentrantLock;

import static java.util.concurrent.TimeUnit.SECONDS;

/**
 * @author daohn on 24/08/2020
 * @project socket_java
 */
public class MessageFactory implements Runnable {

    private final MessageQueue mensagens;
    private final ReentrantLock lock = new ReentrantLock();

    public MessageFactory(MessageQueue mensagens) {
        this.mensagens = mensagens;
    }


    @Override public void run() {
        int i = 0;
        //noinspection InfiniteLoopStatement
        while(true) {
            if(lock.tryLock()) {
                try {
                    long timestamp = System.currentTimeMillis();
                    String msg = "b8:27:eb:8e:94:f2!3c:71:bf:5a:b3:48!Mensagem_" + (i++) + "!"
                            + timestamp;
                    //System.out.println("Mensagem criada: " + msg);
                    mensagens.enqueue(msg);
                    SECONDS.sleep(50);
                }
                catch(InterruptedException e) {
                    e.printStackTrace();
                }
                finally {
                    lock.unlock();
                }
            }
        }
    }
}

