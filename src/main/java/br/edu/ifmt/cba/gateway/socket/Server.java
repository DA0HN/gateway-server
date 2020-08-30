package br.edu.ifmt.cba.gateway.socket;

import br.edu.ifmt.cba.gateway.socket.receive.SocketReceive;
import br.edu.ifmt.cba.gateway.socket.send.SocketSend;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import static br.edu.ifmt.cba.gateway.socket.MessageStream.entradaDeMensagens;

/**
 * @author daohn on 09/08/2020
 * @project socket_java
 */
public class Server {
    private final List<Thread> ioWorkers = new ArrayList<>();

    public void init(int port) {
        try {
            createWorkers();
            // criando um socket para ouvir a porta usando uma fila de tamanho 10
            ServerSocket server = new ServerSocket(port, 10);
            Socket connection;
            final DateTimeFormatter formatter = DateTimeFormatter
                    .ofPattern("HH:mm:ss.SSSS");

            System.out.println(
                    LocalDateTime.now().format(formatter) + " Ouvindo na porta: " + port);
            //noinspection InfiniteLoopStatement
            while(true) {
                try {
                    // ficara bloqueado até um cliente se conectar
                    connection = server.accept();
                    var reader = new BufferedReader(
                            new InputStreamReader(connection.getInputStream(),
                                                  StandardCharsets.UTF_8
                            )
                    );
                    var writer = new BufferedWriter(
                            new OutputStreamWriter(connection.getOutputStream()));

                    for(var worker : ioWorkers)
                        ((IOWorker)worker).addMetadata(new ServerMetadata(connection, reader, writer));

                    System.out.println(LocalDateTime.now().format(formatter) +
                                               " Conexão estabelecida com: " + connection.getInetAddress().getHostAddress());
                }
                catch(Exception e) {
                    e.printStackTrace();
                }
            }
        }
        catch(Exception e) {
            System.err.println("Erro: " + e.toString());
        }
    }

    private void createWorkers() {
        var sender1 = new SocketSend(MessageStream.saidaDeMensagens());
        var receive1 = new SocketReceive(entradaDeMensagens());
//        var receive2 = new SocketReceive(entradaDeMensagens());


//        sender1.setName("Sender_1");
//        receive1.setName("Receiver_1");
//        receive2.setName("Receiver_2");
//
        ioWorkers.add(sender1);
        ioWorkers.add(receive1);
//        ioWorkers.add(receive2);

        ioWorkers.forEach(Thread::start);
    }
}
