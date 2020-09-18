package br.edu.ifmt.cba.gateway.socket;

import br.edu.ifmt.cba.gateway.utils.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * @author daohn on 09/08/2020
 * @project socket_java
 */
public class Server extends Thread {

    private final Logger       logger;
    private final ServerSocket server;
    private final MessageQueue senderQueue;
    private final MessageQueue receiverQueue;

    public Server(int port,
                  MessageQueue senderQueue,
                  MessageQueue receiverQueue) throws IOException {
        super("Servidor");
        this.logger        = new Logger();
        this.server        = new ServerSocket(port);
        this.senderQueue   = senderQueue;
        this.receiverQueue = receiverQueue;
        logger.log("Conexão criada na porta " + port);
    }

    @Override public void run() {
        while(true) {
            try{
                Socket client = server.accept();
                logger.log("Novo cliente conectado " + client.getInetAddress().getHostAddress());

                var reader = new BufferedReader(
                        new InputStreamReader(client.getInputStream()));
                var writer = new PrintWriter(client.getOutputStream(), true);

                new Thread(new ClientHandler("Cliente", logger,
                                             senderQueue, receiverQueue, reader, writer
                )).start();
            }
            catch(Exception e) {
                System.err.println("Erro: " + e.toString());
            }
        }
    }
}
