package br.edu.ifmt.cba.gateway.server;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * @author daohn on 14/08/2020
 * @project gateway_server
 */
public class Server {

    private BufferedReader in;
    private BufferedWriter out;
    private final DateTimeFormatter formatter;
    private Socket connection;

    public Server() {
        this.formatter = DateTimeFormatter.ofPattern("HH:mm:ss.SSSS");
    }

    public void listen(int port) {
        boolean exit = false;
        String msg = null;

        try (var server = new ServerSocket(port, 10)){
            // criando um socket para ouvir a porta usando uma fila de tamanho 10
            while(!exit) {
                log(" Ouvindo na porta: " + port);
                // ficara bloqueado até um cliente se conectar
                connection = server.accept();

                log(" Conexão estabelecida com: " + connection.getInetAddress().getHostAddress());

                // obtendo os fluxos de entrada e saida
                out = new BufferedWriter(new OutputStreamWriter(connection.getOutputStream()
                ));
                in = new BufferedReader(new InputStreamReader(connection.getInputStream(),
                                                              StandardCharsets.UTF_8
                ));

                write("Conexão estabelecida com sucesso...");
                try {
                    do {
                        LocalDateTime time = LocalDateTime.now();
                        System.out.println(time.format(formatter) + " " + msg);
                        msg = in.readLine();
                        write("OK");
                    } while(!msg.equals("bye"));
                }
                catch(IOException e) {
                    System.err.println("Erro: " + e.toString());
                }
                System.out.println("Conexão encerrada pelo cliente");
                exit = true;
                close();
            }
        }
        catch(Exception e) {
            System.err.println("Erro: " + e.toString());
        }
    }

    private void close() throws IOException {
        in.close();
        out.close();
    }

    private void write(String mensagem) throws IOException {
        out.write(mensagem + "\n");
        out.flush();
    }

    private void log(String info) {
        System.out.println(LocalDateTime.now().format(formatter) + info);
    }

    private String process(String msg) {
        String[] parsed = msg.split("!");
        return parsed[1] + "!" + parsed[0] + "!" + parsed[2] + "\0";
    }

}
