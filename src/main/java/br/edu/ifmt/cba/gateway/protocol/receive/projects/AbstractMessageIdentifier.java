package br.edu.ifmt.cba.gateway.protocol.receive.projects;

import br.edu.ifmt.cba.gateway.protocol.receive.ProtocolException;

import java.util.Arrays;

/**
 * @author daohn on 20/08/2020
 * @project gateway_server
 */
public abstract class AbstractMessageIdentifier {
    public abstract void identify(String[] content) throws ProtocolException;

    protected void defaultRecognize(String[] content) throws UnidentifiedMessageException {
        try {
            if(content[0].length() > 18) {
                throw new UnidentifiedMessageException("Tamanho do MAC 'from' inválido: " + content[0] + "\n");
            }
            if(content[1].length() > 18) {
                throw new UnidentifiedMessageException("Tamanho do MAC 'to' inválido: " + content[1] + "\n");
            }
            for(var value : content) {
                if(value.contains(":")) {
                    var mac = value.split(":");
                    for(var m : mac) {
                        if(!m.chars().allMatch(Character::isLetterOrDigit)) {
                            throw new UnidentifiedMessageException("MAC incorreto: " + value + "\n");
                        }
                    }
                }
            }
            // b8:27:eb:8e:94:f2 ! b8:27:eb:8e:94:f2 ! project ! raw ! msg !...
            var msg = Arrays.copyOfRange(content, 4, content.length);
            for(var m : msg) {
                if(!m.chars().allMatch(Character::isLetterOrDigit)) {
                    throw new UnidentifiedMessageException(
                            "Mensagem possui caracteres inválidos: " + m + "\n");
                }
            }
        }
        catch(Exception e) {
            e.printStackTrace();
            throw new UnidentifiedMessageException(e.getMessage());
        }
    }
}
