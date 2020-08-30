package br.edu.ifmt.cba.gateway.socket.receive;

import br.edu.ifmt.cba.gateway.socket.IOWorker;
import br.edu.ifmt.cba.gateway.socket.MessageIOException;
import br.edu.ifmt.cba.gateway.socket.MessageQueue;
import br.edu.ifmt.cba.gateway.socket.ServerMetadata;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author daohn on 24/08/2020
 * @project socket_java
 */
public class SocketReceive extends Thread implements IOWorker {

    private final List<ServerMetadata> connectionsMetadata = new ArrayList<>();
    private final MessageQueue mensagens;
    private final ReentrantLock locker = new ReentrantLock();
    private final ReentrantLock readLock = new ReentrantLock();

    public SocketReceive(MessageQueue mensagens) {
        this.mensagens = mensagens;
    }

    @Override public void addMetadata(ServerMetadata metadata) {
        locker.lock();
        try {
            connectionsMetadata.add(metadata);
        }
        finally {
            locker.unlock();
        }
    }

    @Override public void run() {
        //noinspection InfiniteLoopStatement
        while(true) {
            if(locker.tryLock()) {
                try {
                    if(!connectionsMetadata.isEmpty()) {
                        if(readLock.tryLock()) {
                            try {
                                for(var data : connectionsMetadata) {
                                    try {
                                        if(data.getReader().ready()) {
                                            String msg = data.getReader().readLine();
                                            mensagens.enqueue(msg);
                                        }
                                    }
                                    catch(Exception e) {
                                        throw new MessageIOException("Erro ao ler mensagem!",
                                                                     connectionsMetadata,
                                                                     data
                                        );
                                    }
                                }
                            }
                            finally {
                                readLock.unlock();
                            }
                        }
                    }
                }
                catch(MessageIOException e) {
                    e.cancelConnection();
                    e.printStackTrace();
                }
                finally {
                    locker.unlock();
                }
            }
        }
    }
}
