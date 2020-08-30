package br.edu.ifmt.cba.gateway.socket;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author daohn on 26/08/2020
 * @project socket_java
 */
public class MessageQueue {

    private final Queue<String> fila;
    private final ReentrantLock lock;

    public MessageQueue() {
        this.fila = new LinkedList<>();
        this.lock = new ReentrantLock(false);
    }

    public synchronized void enqueue(String msg) {
        lock.lock();
        try {
            fila.add(msg);
        } finally {
            lock.unlock();
        }
    }

    public String getAndRemoveFirst() {
        lock.lock();
        try {
            return fila.poll();
        } finally {
            lock.unlock();
        }
    }

    public synchronized int size() {
        return fila.size();
    }

    public synchronized boolean isEmpty() {
        return fila.isEmpty();
    }

    public synchronized boolean isNotEmpty() {
        return !isEmpty();
    }
}
