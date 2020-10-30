package br.edu.ifmt.cba.gateway.protocol.receive.projects;

import br.edu.ifmt.cba.gateway.protocol.receive.ProtocolException;

import java.util.Arrays;
import java.util.regex.Pattern;

/**
 * @author daohn on 20/08/2020
 * @project gateway_server
 */
public abstract class AbstractMessageIdentifier {

    private static final String regExp = "[\\x00-\\x20]*[+-]?(((((\\p{Digit}+)(\\.)?((\\p{Digit}+)?)([eE][+-]?(\\p{Digit}+))?)|(\\.((\\p{Digit}+))([eE][+-]?(\\p{Digit}+))?)|(((0[xX](\\p{XDigit}+)(\\.)?)|(0[xX](\\p{XDigit}+)?(\\.)(\\p{XDigit}+)))[pP][+-]?(\\p{Digit}+)))[fFdD]?))[\\x00-\\x20]*";
    private static final Pattern pattern = Pattern.compile(regExp);

    /**
     * @param data conteúdo que será analisado
     * @throws ProtocolException caso a mensagem contenha alguma deformação gera uma exceção
     */
    public abstract void identify(String data) throws ProtocolException;


    /**
     * realiza uma análise dos dados constantes da mensagem. O MAC de quem enviou, o MAC de quem
     * recebeu e o projeto. O resto do conteúdo da mensagem pode variar e deve ser implementado
     * separadamente
     * @param parsedContent mensagem que será analisada
     * @throws UnidentifiedMessageException caso a análise encontre algum erro, lança exceção
     */
    protected void defaultRecognize(String[] parsedContent) throws UnidentifiedMessageException {
        try {
            if(parsedContent[0].length() > 18) {
                throw new UnidentifiedMessageException("Tamanho do MAC 'from' inválido: " + parsedContent[0] + "\n");
            }
            if(parsedContent[1].length() > 18) {
                throw new UnidentifiedMessageException("Tamanho do MAC 'to' inválido: " + parsedContent[1] + "\n");
            }
            for(var value : parsedContent) {
                if(value.contains(":")) {
                    var mac = value.split(":");
                    for(var m : mac) {
                        if(!m.chars().allMatch(Character::isLetterOrDigit)) {
                            throw new UnidentifiedMessageException("MAC incorreto: " + value + "\n");
                        }
                    }
                }
            }
            //        0                1                   2     ...
            // b8:27:eb:8e:94:f2 ! b8:27:eb:8e:94:f2 ! project ! msg !...
            var msg = Arrays.copyOfRange(parsedContent, 3, parsedContent.length);
            for(var m : msg) {
                if(!m.chars().allMatch((ch) -> Character.isLetterOrDigit(ch) || pattern.matcher(String.valueOf(ch)).matches())) {
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
