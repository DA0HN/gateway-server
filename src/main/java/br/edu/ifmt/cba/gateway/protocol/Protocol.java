package br.edu.ifmt.cba.gateway.protocol;

import br.edu.ifmt.cba.gateway.model.ReceivedData;

import java.time.LocalDateTime;
import java.util.Arrays;

import static java.util.Arrays.asList;
import static java.util.Arrays.stream;

/**
 * @author daohn on 14/08/2020
 * @project gateway_server
 */
public class Protocol {

    public ReceivedData parse(String data, LocalDateTime time) throws ProtocolException {
        String[] values = data.split("!");

        validate(values);

        return ReceivedData.builder()
                .from(values[0])
                .to(values[1])
                .raw(Long.parseLong(values[2]))
                .receivedTime(time)
                .sendTime(null)
                .message(asList(Arrays.copyOfRange(values, 3, values.length)))
                .build();
    }
    // b8:27:eb:8e:94:f2!b8:27:eb:8e:94:f2!msg!...
    private void validate(String[] values) throws ProtocolException {
        var builder = new StringBuilder();
        if(values[0].length() > 18) {
            builder.append("Tamanho do MAC 'from' inválido: " + values[0] + "\n");
        }
        if(values[1].length() > 18) {
            builder.append("Tamanho do MAC 'to' inválido: " + values[1] + "\n");
        }
        for(var value : values) {
            if(value.contains(":")) {
                var mac = value.split(":");
                for(var m : mac) {
                    if(!m.chars().allMatch(Character::isLetterOrDigit)) {
                        builder.append("MAC incorreto: " + value + "\n");
                    }
                }
            }
        }
        var msg = Arrays.copyOfRange(values, 3, values.length);
        stream(msg).forEach(m -> {
            if(!m.chars().allMatch(Character::isLetterOrDigit)){
                builder.append("Mensagem possui caracteres inválidos: " + m + "\n");
            }
        });
        if(builder.length() > 0) {
            throw new ProtocolException(builder.toString());
        }
    }

}
