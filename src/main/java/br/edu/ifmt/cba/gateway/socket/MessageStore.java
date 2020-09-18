package br.edu.ifmt.cba.gateway.socket;

import br.edu.ifmt.cba.gateway.utils.Logger;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.TimeZone;

/**
 * @author daohn on 24/08/2020
 * @project socket_java
 */
public class MessageStore extends Thread {

    private static int               i = 0;
    private final  DateTimeFormatter formatter;
    private final  Logger            logger;
    private final  MessageQueue      senderQueue;
    private final  MessageQueue      receiverQueue;

    public MessageStore(String name,
                        Logger logger,
                        MessageQueue senderQueue,
                        MessageQueue receiverQueue
    ) {
        super(name);
        this.formatter     = DateTimeFormatter.ofPattern("HH:mm:ss.SSSS");
        this.logger        = logger;
        this.senderQueue   = senderQueue;
        this.receiverQueue = receiverQueue;
    }

    @Override public void run() {
        //noinspection InfiniteLoopStatement
        while(true) {
            try {
                recvMessage();
            }
            catch(InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void recvMessage() throws InterruptedException {
        LocalDateTime time = LocalDateTime.now();

        String msg = receiverQueue.dequeue();
        logger.log("Mensagem: " + msg);
        //        var timestamp = Long.parseLong(msg.split("!")[2]);
        var timestamp = Long.parseLong(msg);
        long now = Timestamp.valueOf(time).getTime();
        var triggerTime = LocalDateTime
                .ofInstant(Instant.ofEpochMilli(timestamp),
                           TimeZone.getDefault().toZoneId()
                );
        logger.log("\t\tFoi gerada em: " + triggerTime.format(formatter));
        //        Logger.log("\t\tDemorou: " + ((now / 1000) - timestamp) + "ms para " +
        //                           "chegar");
        logger.log("\t\tDemorou: " + (System.currentTimeMillis() - timestamp) + "ms para " +
                           "chegar");

        var toSend = "b8:27:eb:8e:94:f2!3c:71:bf:5a:b3:48!Mensagem_" + (i++) + "!"
                + System.currentTimeMillis();
        senderQueue.enqueue(toSend);
    }

}

