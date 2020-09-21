package br.edu.ifmt.cba.gateway.socket.debug;

import br.edu.ifmt.cba.gateway.socket.MessageQueue;
import br.edu.ifmt.cba.gateway.utils.Logger;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.StringJoiner;
import java.util.TimeZone;

/**
 * @author daohn on 24/08/2020
 * @project socket_java
 */
public class MessageDebug extends Thread {

    private final DateTimeFormatter formatter;
    private final Logger            logger;
    private final MessageQueue      senderQueue;
    private final MessageQueue      receiverQueue;

    public MessageDebug(String name,
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

        var metadata = msg.split("!");

        logger.log("Mensagem: " + msg);
        var timestamp = Long.parseLong(metadata[2]);

        elapsedTime(time, timestamp);

        // gera uma mensagem de OK confirmação da chegada da mensagem
        String finalMessage = generateResponse(metadata[0]);

        senderQueue.enqueue(finalMessage);
    }

    private String generateResponse(String metadatum) {
        var from = "b8:27:eb:8e:94:f2";
        var to = metadatum;
        var content = "OK";
        var finalMessage = new StringJoiner("!")
                .add(from)
                .add(to)
                .add(content)
                .toString();
        logger.log("Confirmação da mensagem gerada '" + finalMessage + "' e enfileirada.");
        return finalMessage;
    }

    private void elapsedTime(LocalDateTime time, long timestamp) {
        long now = Timestamp.valueOf(time).getTime();
        var triggerTime = LocalDateTime
                .ofInstant(Instant.ofEpochMilli(timestamp),
                           TimeZone.getDefault().toZoneId()
                );
        logger.log("\t\tFoi gerada em: " + triggerTime.format(formatter));
        logger.log("\t\tDemorou: " + (System.currentTimeMillis() - timestamp) + "ms para chegar");
        //        logger.log("\t\tDemorou: " + (now - timestamp) + "ms para chegar");
        //        logger.log("\t\tDemorou: " + ((now/1000) - timestamp) + "ms para chegar");
        //        logger.log("\t\tDemorou: " + (timestamp - (now / 1000)) + "s para chegar");
        //        logger.log("\t\tDemorou: " + (timestamp*1000 - now) + "ms para chegar");
        //        logger.log("\t\tDemorou: " + (now - timestamp*1000) + "ms para chegar");
        //        logger.log("\t\tDemorou: " + (double)((timestamp*1000 - now)/1000) + "ms para chegar");
        //        logger.log("\t\tDemorou: " + (double)((now - timestamp*1000)/1000) + "ms para chegar");
    }

}

