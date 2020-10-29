package br.edu.ifmt.cba.gateway.socket;

import br.edu.ifmt.cba.gateway.utils.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * @author daohn on 16/09/2020
 * @project socket_java
 */
public class ClientHandler extends Thread {

    private final Logger         logger;
    private final MessageQueue   senderQueue;
    private final MessageQueue   receiverQueue;
    private final BufferedReader reader;
    private final PrintWriter    writer;
    private final boolean        debug;

    public ClientHandler(String name, Logger logger,
                         MessageQueue senderQueue,
                         MessageQueue receiverQueue,
                         BufferedReader reader,
                         PrintWriter writer, boolean debug) {
        super(name);
        this.logger        = logger;
        this.senderQueue   = senderQueue;
        this.receiverQueue = receiverQueue;
        this.reader        = reader;
        this.writer        = writer;
        this.debug         = debug;
    }

    public ClientHandler(String name, Logger logger,
                         MessageQueue senderQueue,
                         MessageQueue receiverQueue,
                         BufferedReader reader,
                         PrintWriter writer) {
        this(name, logger, senderQueue, receiverQueue, reader, writer, false);
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
            log("Recv: Chegou: " + line);
            log("Recv: Enviando confirmação de mensagem 'OK'");
            writer.println("OK");
            log("Recv: 'OK' enviado.");

            receiverQueue.enqueue(line);
        }
    }

    private void hasMessageToSend() throws IOException {
        var message = senderQueue.dequeue();
        log("Sender: Enviando mensagem...");
        writer.println(message);
        log("Sender: Mensagem enviada");
        log("Sender: Esperando resposta...");

        String response = reader.readLine();
        if(response == null) throw new IOException("O cliente não respondeu");
        log("Sender: Chegou uma resposta para '" + message + "' -> " + response);
    }

    private void log(String message) {
        if(debug) {
            logger.log(message);
        }
    }
}
