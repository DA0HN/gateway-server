package br.edu.ifmt.cba.gateway.protocol.receive.projects;

import br.edu.ifmt.cba.gateway.protocol.receive.ProtocolException;

/**
 * @author daohn on 20/08/2020
 * @project gateway_server
 */
public class GenericMessageIdentifier extends AbstractMessageIdentifier {
    @Override public void identify(String data) throws ProtocolException {
        var content = data.split("!");
        defaultRecognize(content);
    }
}
