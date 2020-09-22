package br.edu.ifmt.cba.gateway.modules.debug;

import br.edu.ifmt.cba.gateway.model.DebugData;
import br.edu.ifmt.cba.gateway.model.IReceivedData;
import br.edu.ifmt.cba.gateway.protocol.receive.IReceiveProtocol;
import br.edu.ifmt.cba.gateway.utils.Logger;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.TimeZone;

import static java.util.Arrays.asList;

/**
 * @author daohn on 21/09/2020
 * @project gateway_server
 */
public class DebugProtocol implements IReceiveProtocol {

    private final Logger logger = new Logger();

    public IReceivedData parse(String data) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss.SSSS");
        String[] values = data.split("!");

        long rawSendTime = Long.parseLong(values[3]);
        var sendTime = LocalDateTime.ofInstant(
                Instant.ofEpochMilli(rawSendTime),
                TimeZone.getDefault().toZoneId()
        );
        long elapsedTime = System.currentTimeMillis() - rawSendTime;
        logger.log("\t\tFoi gerada em: " + sendTime.format(formatter));
        logger.log("\t\tDemorou: " + elapsedTime + "ms para chegar");

        return DebugData.builder()
                .from(values[0])
                .to(values[1])
                .elapsedTime(elapsedTime)
                .raw(rawSendTime)
                .receivedTime(LocalDateTime.now())
                .sendTime(sendTime)
                .message(asList(Arrays.copyOfRange(values, 4, values.length)))
                .build();
    }
}
