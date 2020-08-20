package br.edu.ifmt.cba.gateway.protocol.receive;

import br.edu.ifmt.cba.gateway.model.IReceivedData;

import java.time.LocalDateTime;

/**
 * @author daohn on 20/08/2020
 * @project gateway_server
 */
public interface IReceiveProtocol {
    IReceivedData parse(String data, LocalDateTime time) throws ProtocolException;
}
