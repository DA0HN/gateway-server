package br.edu.ifmt.cba.gateway.protocol.send.projects;

import br.edu.ifmt.cba.gateway.model.IReceivedData;

/**
 * @author daohn on 20/08/2020
 * @project gateway_server
 */
public abstract class AbstractFactoryMessage {
    public abstract String create(IReceivedData data);
}
