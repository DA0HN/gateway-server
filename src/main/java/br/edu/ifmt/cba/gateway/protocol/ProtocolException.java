package br.edu.ifmt.cba.gateway.protocol;

/**
 * @author daohn on 14/08/2020
 * @project gateway_server
 */
public class ProtocolException extends Exception {
    public ProtocolException() {
    }

    public ProtocolException(String message) {
        super(message);
    }
}
