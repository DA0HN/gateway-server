package br.edu.ifmt.cba.gateway.modules.air_conditioner;

import br.edu.ifmt.cba.gateway.protocol.receive.ProtocolException;
import br.edu.ifmt.cba.gateway.protocol.receive.projects.AbstractMessageIdentifier;

/**
 * @author daohn on 29/10/2020
 * @project gateway_server
 */
public class AirConditionerMessageIdentifier extends AbstractMessageIdentifier {
    @Override public void identify(String data) throws ProtocolException {
        var content = data.split("!");
        defaultRecognize(content);
    }
}
