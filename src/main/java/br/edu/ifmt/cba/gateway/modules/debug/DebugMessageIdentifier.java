package br.edu.ifmt.cba.gateway.modules.debug;

import br.edu.ifmt.cba.gateway.protocol.receive.ProtocolException;
import br.edu.ifmt.cba.gateway.protocol.receive.projects.AbstractMessageIdentifier;

/**
 * @author daohn on 21/09/2020
 * @project gateway_server
 */
public class DebugMessageIdentifier extends AbstractMessageIdentifier {
    /**
     * faz a análise padrão da mensagem e analisa o tamanho do raw (timestamp)
     * @param data conteúdo que será analisado
     * @throws ProtocolException caso o tamanho do timestamp tenha tamanho diferente de 13 lança
     * exceção
     */
    @Override public void identify(String data) throws ProtocolException {
        var content = data.split("!");
        defaultRecognize(content);
        // 1600798060423
        var raw = content[3];
        if(raw.length() != 13) {
            throw new ProtocolException("Tamanho do timestamp não é válido");
        }
    }
}
