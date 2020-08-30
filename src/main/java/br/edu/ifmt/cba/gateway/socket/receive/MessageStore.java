package br.edu.ifmt.cba.gateway.socket.receive;

import br.edu.ifmt.cba.gateway.socket.MessageQueue;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author daohn on 24/08/2020
 * @project socket_java
 */
public class MessageStore implements Runnable {

    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss.SSSS");
    private final ReentrantLock lock = new ReentrantLock();
    private final MessageQueue mensagens;

    public MessageStore(MessageQueue mensagens) {
        this.mensagens = mensagens;
    }

    @Override public void run() {
        //noinspection InfiniteLoopStatement
        while(true) {
            if(lock.tryLock()) {
                try {
                    recvMessage();
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

    private void recvMessage() throws InterruptedException {
        LocalDateTime time = LocalDateTime.now();

        if(mensagens.isNotEmpty()) {
            String msg = mensagens.getAndRemoveFirst();
            System.out.println(time.format(formatter) + " Mensagem: " + msg);
            var timestamp = Long.parseLong(msg.split("!")[2]);
            long now = Timestamp.valueOf(time).getTime();
            var triggerTime = LocalDateTime
                    .ofInstant(Instant.ofEpochSecond(timestamp),
                               TimeZone.getDefault().toZoneId()
                    );
            System.out.println("\t\tFoi gerada em: " + triggerTime.format(formatter));
            System.out.println("\t\tDemorou: " + (double) ((now/1000) - timestamp) + "ms para " +
                                       "chegar");
        }
        TimeUnit.MILLISECONDS.sleep(1);
    }

}

