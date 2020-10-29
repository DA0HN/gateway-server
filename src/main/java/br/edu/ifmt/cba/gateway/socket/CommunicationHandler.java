package br.edu.ifmt.cba.gateway.socket;

import br.edu.ifmt.cba.gateway.utils.Logger;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @author daohn on 06/10/2020
 * @project gateway_server
 */
public class CommunicationHandler extends Thread {

    private final MessageQueue senderQueue;
    private final MessageQueue receiverQueue;
    private final List<Client> clients;
    private final boolean      debug;
    private final Logger       logger;

    public CommunicationHandler(String name,
                                Logger logger,
                                MessageQueue senderQueue,
                                MessageQueue receiverQueue,
                                List<Client> clients,
                                boolean debug) {
        super(name);
        this.senderQueue   = senderQueue;
        this.receiverQueue = receiverQueue;
        this.clients       = clients;
        this.debug         = debug;
        this.logger        = logger;
    }

    public CommunicationHandler(String name,
                                Logger logger,
                                MessageQueue senderQueue,
                                MessageQueue receiverQueue,
                                List<Client> clients) {
        this(name, logger, senderQueue, receiverQueue, clients, false);
    }

    @Override public void run() {
        while(true) {
            if(senderQueue.isNotEmpty()) {
                synchronized(clients) {
                    var message = senderQueue.dequeue();
                    var it = new CopyOnWriteArrayList<>(clients).iterator();
                    while(it.hasNext()) {
                        var client = it.next();
                        try {
                            hasMessageToSend(message, client);
                        }
                        catch(IOException e) {
                            this.clients.remove(client);
                            //                                client.connection.close();
                            e.printStackTrace();
                        }
                    }
                    System.out.println("-------------------------------------");
                    System.out.println(clients.size());
                    System.out.println("-------------------------------------");
                }
            }
            else {
                synchronized(clients) {
                    var it = new CopyOnWriteArrayList<>(clients).iterator();
                    while(it.hasNext()) {
                        var client = it.next();
                        try(client.connection) {
                            if(client.reader.ready()) {
                                hasMessageToRead(client);
                            }
                        }
                        catch(IOException e) {
                            //                                client.connection.close();
                            this.clients.remove(client);
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
    }

    private void hasMessageToSend(String message, Client client) throws IOException {
        log("Sender: Enviando mensagem...");
        client.writer.println(message);
        log("Sender: Mensagem enviada");
        log("Sender: Esperando resposta...");
        String response;
        if((response = client.reader.readLine()) != null) {
            log("Sender: Chegou uma resposta para '" + message + "' -> " + response);
        }
        //        if(response == null) throw new IOException("O cliente não respondeu");
    }

    private void hasMessageToRead(Client client) throws IOException {
        String line = client.reader.readLine();

        if(line != null) {

            log("Recv: Chegou: " + line);
            log("Recv: Enviando confirmação de mensagem 'OK'");

            client.writer.println("OK");

            log("Recv: 'OK' enviado.");

            receiverQueue.enqueue(line);
        }
    }


    private void log(String message) {
        if(debug) {
            logger.log(message);
        }
    }
}
