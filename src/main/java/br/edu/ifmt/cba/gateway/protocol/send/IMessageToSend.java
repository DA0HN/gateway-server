package br.edu.ifmt.cba.gateway.protocol.send;

/**
 * @author daohn on 20/08/2020
 * @project gateway_server
 */
public interface IMessageToSend {
    Status getStatus();
    String getConfirmMessage();
    String getMessage();
}
