package br.edu.ifmt.cba.gateway.modules.debug;

import br.edu.ifmt.cba.gateway.protocol.receive.ProtocolException;
import br.edu.ifmt.cba.gateway.protocol.receive.projects.AbstractMessageIdentifier;

/**
 * @author daohn on 21/09/2020
 * @project gateway_server
 */
public class DebugMessageIdentifier extends AbstractMessageIdentifier {
    @Override public void identify(String[] content) throws ProtocolException {
        defaultRecognize(content);
    }
}
