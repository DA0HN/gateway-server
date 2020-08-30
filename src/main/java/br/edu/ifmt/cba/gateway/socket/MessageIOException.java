package br.edu.ifmt.cba.gateway.socket;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author daohn on 28/08/2020
 * @project socket_java
 */
public class MessageIOException extends Exception {

    private ReentrantLock lock = new ReentrantLock();
    private List<ServerMetadata> connectionsMetadata;
    private ServerMetadata metadata;

    public MessageIOException(String message) {
        super(message);
    }

    public MessageIOException(String message, List<ServerMetadata> connectionsMetadata,
                              ServerMetadata metadata) {
        super(message);
        this.connectionsMetadata = connectionsMetadata;
        this.metadata = metadata;
    }

    public void cancelConnection() {
        lock.lock();
        try {
            connectionsMetadata.remove(metadata);
            metadata.closeConnection();
        }
        catch(IOException e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }
}
