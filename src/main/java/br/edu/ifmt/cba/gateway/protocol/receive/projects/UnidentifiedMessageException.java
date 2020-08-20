package br.edu.ifmt.cba.gateway.protocol.receive.projects;

import br.edu.ifmt.cba.gateway.protocol.receive.ProtocolException;

/**
 * @author daohn on 20/08/2020
 * @project gateway_server
 */
public class UnidentifiedMessageException extends ProtocolException {

    public UnidentifiedMessageException() {
    }

    public UnidentifiedMessageException(String message) {
        super(message);
    }
}
