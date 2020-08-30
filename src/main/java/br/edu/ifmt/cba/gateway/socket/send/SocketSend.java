package br.edu.ifmt.cba.gateway.socket.send;

import br.edu.ifmt.cba.gateway.socket.IOWorker;
import br.edu.ifmt.cba.gateway.socket.MessageIOException;
import br.edu.ifmt.cba.gateway.socket.MessageQueue;
import br.edu.ifmt.cba.gateway.socket.ServerMetadata;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author daohn on 24/08/2020
 * @project socket_java
 */
public class SocketSend extends Thread implements IOWorker {

    private final List<ServerMetadata> connectionsMetadata = new ArrayList<>();
    private final MessageQueue mensagens;
    private final ReentrantLock lock = new ReentrantLock();

    public SocketSend(MessageQueue mensagens) {
        this.mensagens = mensagens;
    }


    @Override public void run() {
        //noinspection InfiniteLoopStatement
        while(true) {
            if(lock.tryLock()) {
                try {
                    if(mensagens.isNotEmpty() && !connectionsMetadata.isEmpty()) {
                        var msg = mensagens.getAndRemoveFirst();
                        System.out.println("Enviando: " + msg);
                        for(var data : connectionsMetadata) {
                            try {
                                data.getWriter().write(msg + "\n");
                                data.getWriter().flush();
                            }
                            catch(IOException e) {
                                throw new MessageIOException("Erro ao enviar mensagem!",
                                                             connectionsMetadata,
                                                             data
                                );
                            }
                        }
                    }
                }
                catch(MessageIOException e) {
                    e.cancelConnection();
                    e.printStackTrace();
                }
                finally {
                    lock.unlock();
                }
            }
        }
    }

    @Override public void addMetadata(ServerMetadata metadata) {
        lock.lock();
        try {
            connectionsMetadata.add(metadata);
        }
        finally {
            lock.unlock();
        }
    }
}
