package br.edu.ifmt.cba.gateway.modules.air_conditioner;

import br.edu.ifmt.cba.gateway.model.AirConditionerData;
import br.edu.ifmt.cba.gateway.model.IReceivedData;
import br.edu.ifmt.cba.gateway.protocol.receive.IReceiveProtocol;
import br.edu.ifmt.cba.gateway.protocol.receive.ProtocolException;
import br.edu.ifmt.cba.gateway.utils.Logger;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.TimeZone;

/**
 * @author daohn on 29/10/2020
 * @project gateway_server
 */
public class AirConditionerProtocol implements IReceiveProtocol {

    private final Logger logger = new Logger();

    private String messageStatistics;

    @Override public IReceivedData parse(String data) throws ProtocolException {

        this.messageStatistics = "";

        var formatter = DateTimeFormatter.ofPattern("HH:mm:ss.SSSS");
        var values = data.split("!");

        long rawSendTime = Long.parseLong(values[3]);
        var sendTime = LocalDateTime.ofInstant(
                Instant.ofEpochSecond(rawSendTime),
                TimeZone.getDefault().toZoneId()
        ).plusHours(4);
        //        var now = LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant().getEpochSecond();
        long elapsedTime = System.currentTimeMillis() - sendTime.atZone(
                ZoneId.systemDefault()).toInstant().toEpochMilli();

        var elapsedTimeFormatted = String.format("%.4f", (double) elapsedTime / 1000);

        this.messageStatistics += "\tFoi gerada em: " + sendTime.format(formatter) + "\n";
        this.messageStatistics += "\t\t\t\t\tDemorou: " + elapsedTimeFormatted
                + "s para" + " chegar" + "\n";

        return AirConditionerData.builder()
                .source(values[0])
                .destiny(values[1])
                .timestamp(rawSendTime)
                .elapsedTime(elapsedTime)
                .created(sendTime)
                .temperature(Double.parseDouble(values[4]))
                .humidity(Double.parseDouble(values[5]))
                .current(Double.parseDouble(values[6]))
                .power(Double.parseDouble(values[7]))
                .energyConsumption(Double.parseDouble(values[8]))
                .build();
    }

    public String getMessageStatistics() {
        return messageStatistics;
    }
}
