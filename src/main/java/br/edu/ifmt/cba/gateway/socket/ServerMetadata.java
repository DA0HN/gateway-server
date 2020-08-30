package br.edu.ifmt.cba.gateway.socket;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author daohn on 27/08/2020
 * @project socket_java
 */
public class ServerMetadata {
    private final Socket clientConnection;
    private final ReentrantLock lock = new ReentrantLock();
    private final BufferedReader reader;
    private final BufferedWriter writer;

    public ServerMetadata(Socket clientConnection, BufferedReader reader, BufferedWriter writer) {
        this.clientConnection = clientConnection;
        this.reader = reader;
        this.writer = writer;
    }

    public void closeConnection() throws IOException {
        lock.lock();
        try {
            if(!clientConnection.isClosed()) {
                this.reader.close();
                this.writer.close();
                this.clientConnection.close();
                System.out.println("Reader: " + clientConnection.isInputShutdown());
                System.out.println("Writer: " + clientConnection.isOutputShutdown());
                System.out.println("Connection: " + clientConnection.isClosed());
            }
        }
        finally {
            lock.unlock();
        }
    }

    public BufferedReader getReader() {
        lock.lock();
        try {
            return reader;
        }
        finally {
            lock.unlock();
        }
    }

    public BufferedWriter getWriter() {
        lock.lock();
        try {
            return writer;
        }
        finally {
            lock.unlock();
        }
    }

    public Socket getClientConnection() {
        lock.lock();
        try {
            return clientConnection;
        }
        finally {
            lock.unlock();
        }
    }
}
