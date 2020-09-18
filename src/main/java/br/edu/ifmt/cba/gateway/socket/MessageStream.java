package br.edu.ifmt.cba.gateway.socket;

import br.edu.ifmt.cba.gateway.utils.Logger;

/**
 * @author daohn on 27/08/2020
 * @project socket_java
 */
public class MessageStream {

    private final static MessageQueue saidaDeMensagens = new MessageQueue(new Logger());
    private final static MessageQueue entradaDeMensagens = new MessageQueue(new Logger());

    private MessageStream(){}

    public static synchronized MessageQueue saidaDeMensagens() {
        return saidaDeMensagens;
    }

    public static synchronized MessageQueue entradaDeMensagens() {
        return entradaDeMensagens;
    }

}
