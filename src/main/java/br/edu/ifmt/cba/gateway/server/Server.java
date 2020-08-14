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

    private final DateTimeFormatter formatter;
    private BufferedReader in;
    private BufferedWriter out;
    private Socket connection;

    public Server() throws IOException {
        this.formatter = DateTimeFormatter.ofPattern("HH:mm:ss.SSSS");
    }

    public void listen(int port) {
        boolean exit = false;

        try (var server = new ServerSocket(port, 10)){
            // criando um socket para ouvir a porta usando uma fila de tamanho 10
            while(!exit) {
                log(" Ouvindo na porta: " + port);
                // ficara bloqueado até um cliente se conectar
                connection = server.accept();

                // obtendo os fluxos de entrada e saida
                this.out = new BufferedWriter(new OutputStreamWriter(connection.getOutputStream()
                ));
                this.in = new BufferedReader(new InputStreamReader(connection.getInputStream(),
                                                                   StandardCharsets.UTF_8
                ));

                log(" Conexão estabelecida com: " + connection.getInetAddress().getHostAddress());
                write("Conexão estabelecida com sucesso...");

                receiveDataFromGateway();

                System.out.println("Conexão encerrada pelo cliente");
                exit = true;
                close();
            }
        }
        catch(Exception e) {
            System.err.println("Erro: " + e.toString());
        }
    }

    private void receiveDataFromGateway() {
        String msg;
        try {
            do {
                LocalDateTime time = LocalDateTime.now();
                msg = in.readLine();
                System.out.println(time.format(formatter) + " " + msg);
                write("OK");
            } while(!msg.equals("bye"));
        }
        catch(IOException e) {
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
