package br.edu.ifmt.cba.gateway.utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * @author daohn on 14/08/2020
 * @project gateway_server
 */
public class Logger {
    private static final DateTimeFormatter formatter;

    static {
        formatter = DateTimeFormatter.ofPattern("HH:mm:ss.SSSS");
    }

    public static void log(String info) {
        System.out.println(LocalDateTime.now().format(formatter) + " " + info);
    }

}
