package br.edu.ifmt.cba.gateway.service;

import br.edu.ifmt.cba.gateway.protocol.send.IMessageToSend;
import br.edu.ifmt.cba.gateway.utils.Logger;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;

import static br.edu.ifmt.cba.gateway.protocol.send.Status.CORRUPTED;
import static br.edu.ifmt.cba.gateway.protocol.send.Status.NEW;
import static br.edu.ifmt.cba.gateway.protocol.send.Status.REDUNDANT;

/**
 * @author daohn on 14/08/2020
 * @project gateway_server
 */
public class GatewayCommunication {
    private final ReceivedDataService service;
    private BufferedReader in;
    private BufferedWriter out;

    public GatewayCommunication(ReceivedDataService service) {
        this.service = service;
    }

    public void listen(int port) {

        try(var server = new ServerSocket(port, 10)) {
            // criando um socket para ouvir a porta usando uma fila de tamanho 10
            while(true) {
                Logger.log("Ouvindo na porta: " + port);
                // ficara bloqueado até um cliente se conectar
                Socket connection = server.accept();

                // obtendo os fluxos de entrada e saida
                this.out = new BufferedWriter(new OutputStreamWriter(connection.getOutputStream()
                ));
                this.in = new BufferedReader(new InputStreamReader(connection.getInputStream(),
                                                                   StandardCharsets.UTF_8
                ));

                Logger.log(
                        "Conexão estabelecida com: " + connection.getInetAddress().getHostAddress());
                write("Conexão estabelecida com sucesso...");

                awaitDataFromGateway();

                System.out.println("Conexão encerrada pelo cliente");

                close(connection);
            }
        }
        catch(Exception e) {
            System.err.println("Erro: " + e.toString());
        }
    }

    private void awaitDataFromGateway() {
        String msg;
        try {
            do {
                LocalDateTime time = LocalDateTime.now();
                msg = in.readLine();
                Logger.log("Chegou..: " + msg);

                // encerra a conexão atual
                if(msg.equals("bye")) break;

                IMessageToSend response = service.save(msg, time);

                if(NEW.equals(response.getStatus())) {
                    Logger.log("Enviando: " + response.getConfirmMessage());
                    write(response.getConfirmMessage());

                    Logger.log("Enviando: " + response.getMessage());
                    write(response.getMessage());
                }
                else if(CORRUPTED.equals(response.getStatus()) || REDUNDANT.equals(
                        response.getStatus())) {
                    write(response.getMessage());
                }
            } while(true);
        }
        catch(IOException e) {
            System.err.println("Erro: " + e.toString());
        }
    }

    private void close(Socket connection) throws IOException {
        in.close();
        out.close();
        connection.close();
    }

    protected void write(String mensagem) throws IOException {
        out.write(mensagem + "\n");
        out.flush();
    }
}
