package br.edu.ifmt.cba.gateway.socket;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * @author daohn on 06/10/2020
 * @project gateway_server
 */
public class Client {
    protected final BufferedReader reader;
    protected final PrintWriter    writer;
    protected final Socket         connection;

    public Client(Socket connection, BufferedReader reader, PrintWriter writer) {
        this.reader     = reader;
        this.writer     = writer;
        this.connection = connection;
    }
}
