package br.edu.ifmt.cba.gateway.utils;

import java.io.PrintStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * @author daohn on 14/08/2020
 * @project gateway_server
 */
public class Logger {
    private final DateTimeFormatter formatter;
    private final PrintStream       stream;

    public Logger(PrintStream stream) {
        this.stream    = stream;
        this.formatter = DateTimeFormatter.ofPattern("HH:mm:ss.SSSS");
    }

    public Logger() {
        this(System.out);
    }

    private String loggerString() {
        return "> " + LocalDateTime.now().format(formatter) + " ";
    }

    public void log(String message) {
        this.stream.println(loggerString() + message);
    }

    public void log(String message, PrintStream stream) {
        stream.println(loggerString() + message);
    }
}
