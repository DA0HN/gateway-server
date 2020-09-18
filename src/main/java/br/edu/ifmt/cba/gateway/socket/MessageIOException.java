package br.edu.ifmt.cba.gateway.socket;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author daohn on 28/08/2020
 * @project socket_java
 */
public class MessageIOException extends RuntimeException {
    public MessageIOException(String message) {
        super(message);
    }
}
