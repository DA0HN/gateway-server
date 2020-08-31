package br.edu.ifmt.cba.gateway.socket;

import br.edu.ifmt.cba.gateway.utils.Logger;

import java.util.LinkedList;
import java.util.Queue;

/**
 * @author daohn on 26/08/2020
 * @project socket_java
 */
public class MessageQueue {

    private static final int CAPACITY = 10;

    private final Queue<String> queue;
    private boolean isEmpty = true;

    public MessageQueue() {
        this.queue = new LinkedList<>();
    }

    public synchronized void enqueue(String msg) {
        try {
            while(queue.size() == CAPACITY) {
                Logger.log(Thread.currentThread().getName() + ": Buffer is full," +
                                   " waiting...");
                wait();
            }
            Logger.log(Thread.currentThread().getName() + ": added " + msg
                               + " into queue"
            );
            queue.add(msg);
            // Signal consumer thread that, buffer has element now
            Logger.log(Thread.currentThread().getName()
                               + ": Signalling that buffer is no more empty now");
            // Communicate waiting thread that queue is not empty now
            isEmpty = false;
            notifyAll();
        }
        catch(InterruptedException e) {
            e.printStackTrace();
        }
    }

    public synchronized String dequeue() {
        String ref;
        while(isEmpty) {
            try {
                wait();
            }
            catch(InterruptedException e) {
                e.printStackTrace();
            }
        }
        // Se a fila possuir apenas 1 pair aciona a flag
        // para que os consumidores entrem em estado de espera
        if(queue.size() == 1) {
            isEmpty = true;
        }
        // Se a fila estiver vazia ou tiver terminado o processo
        // retorna null
        if(queue.size() == 0) {
            return null;
        }
        // Se houver mais de 1 elemento na fila não há necessidade
        // de acionar a flag isEmpty
        Logger.log(Thread.currentThread().getName()
                           + ": Queue size " + queue.size());
        ref = queue.remove();
        if(queue.size() == CAPACITY - 1) {
            notifyAll();
        }
        return ref;
    }

    public synchronized int size() {
        return queue.size();
    }

    public synchronized boolean isNotEmpty() {
        return !isEmpty();
    }

    public synchronized boolean isEmpty() {
        return queue.isEmpty();
    }
}
