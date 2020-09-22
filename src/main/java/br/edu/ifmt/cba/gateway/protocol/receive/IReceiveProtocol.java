package br.edu.ifmt.cba.gateway.protocol.receive;

import br.edu.ifmt.cba.gateway.model.IReceivedData;

/**
 * @author daohn on 20/08/2020
 * @project gateway_server
 */
public interface IReceiveProtocol {
    IReceivedData parse(String data) throws ProtocolException;
}
