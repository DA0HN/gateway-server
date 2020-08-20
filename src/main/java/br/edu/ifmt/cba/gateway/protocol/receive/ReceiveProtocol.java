package br.edu.ifmt.cba.gateway.protocol.receive;

import br.edu.ifmt.cba.gateway.model.GenericReceivedData;
import br.edu.ifmt.cba.gateway.model.IReceivedData;
import br.edu.ifmt.cba.gateway.protocol.receive.projects.AbstractMessageIdentifier;

import java.time.LocalDateTime;
import java.util.Arrays;

import static java.util.Arrays.asList;

/**
 * @author daohn on 14/08/2020
 * @project gateway_server
 */
public class ReceiveProtocol implements IReceiveProtocol {

    private final AbstractMessageIdentifier messageIdentifier;

    public ReceiveProtocol(AbstractMessageIdentifier messageIdentifier) {
        this.messageIdentifier = messageIdentifier;
    }
    
    public IReceivedData parse(String data, LocalDateTime time) throws ProtocolException {
        String[] values = data.split("!");
        long sendTime = Long.parseLong(values[2]);

        // b8:27:eb:8e:94:f2!b8:27:eb:8e:94:f2!msg!...
        messageIdentifier.identify(values);

        return GenericReceivedData.builder()
                .from(values[0])
                .to(values[1])
                .raw(sendTime)
                .receivedTime(time)
                .sendTime(null)
                .message(asList(Arrays.copyOfRange(values, 3, values.length)))
                .build();
    }

}
