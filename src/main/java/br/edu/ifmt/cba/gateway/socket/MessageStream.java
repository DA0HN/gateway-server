package br.edu.ifmt.cba.gateway.socket;

/**
 * @author daohn on 27/08/2020
 * @project socket_java
 */
public class MessageStream {

    private final static MessageQueue saidaDeMensagens = new MessageQueue();
    private final static MessageQueue entradaDeMensagens = new MessageQueue();

    private MessageStream(){}

    public static synchronized MessageQueue saidaDeMensagens() {
        return saidaDeMensagens;
    }

    public static synchronized MessageQueue entradaDeMensagens() {
        return entradaDeMensagens;
    }

}
