package br.edu.ifmt.cba.gateway.socket;

import br.edu.ifmt.cba.gateway.utils.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * @author daohn on 16/09/2020
 * @project socket_java
 */
public class ClientHandler extends Thread {

    private final Logger       logger;
    private final MessageQueue senderQueue;
    private final MessageQueue receiverQueue;
    private final BufferedReader reader;
    private final PrintWriter    writer;

    public ClientHandler(String name, Logger logger,
                         MessageQueue senderQueue,
                         MessageQueue receiverQueue,
                         BufferedReader reader,
                         PrintWriter writer) {
        super(name);
        this.logger        = logger;
        this.senderQueue   = senderQueue;
        this.receiverQueue = receiverQueue;
        this.reader        = reader;
        this.writer        = writer;
    }

    @Override public void run() {
        try(this.writer; this.reader) {
            while(true) {
                // Verifica se existe mensagem para enviar
                if(senderQueue.isNotEmpty()) {
                    hasMessageToSend();
                }
                // Espera entrada de mensagens
                else if(reader.ready()) {
                    hasMessageToRead();
                }
            }
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }

    private void hasMessageToRead() throws IOException {
        String line = reader.readLine();
        if(line != null) {
            logger.log("Recv: Chegou: " + line);
            logger.log("Recv: Enviando confirmação de mensagem 'OK'");
            writer.println("OK");
            logger.log("Recv: 'OK' enviado.");

            receiverQueue.enqueue(line);
        }
    }

    private void hasMessageToSend() throws IOException {
        var message = senderQueue.dequeue();
        logger.log("Sender: Enviando mensagem...");
        writer.println(message);
        logger.log("Sender: Mensagem enviada");
        logger.log("Sender: Esperando resposta...");

        String response = reader.readLine();
        logger.log("Sender: Chegou uma resposta para '" + message + "' -> " + response);
    }
}
