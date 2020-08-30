package br.edu.ifmt.cba.gateway.app;

import br.edu.ifmt.cba.gateway.socket.Server;
import br.edu.ifmt.cba.gateway.socket.receive.MessageStore;
import br.edu.ifmt.cba.gateway.socket.send.MessageFactory;

import static br.edu.ifmt.cba.gateway.socket.MessageStream.entradaDeMensagens;
import static br.edu.ifmt.cba.gateway.socket.MessageStream.saidaDeMensagens;

/**
 * @author daohn on 14/08/2020
 * @project gateway_server
 */
public class App {

    public static void main(String... args) {
        try {
            var port = processArgument(args);
            new Thread(new MessageStore(entradaDeMensagens())).start();
            new Thread(new MessageFactory(saidaDeMensagens())).start();
            new Server().init(port);
        }
        catch(Exception e) {
            System.err.println("Erro: " + e.toString());
            System.exit(1);
        }
    }

    private static Integer processArgument(String[] args) {
        var port = -1;
        // verificando se foi informado 1 argumento de linha de comando
        if(args.length < 1) {
            System.err.println("Uso: java tcp.server <port>");
            System.exit(1);
        }
        // para garantir que somente inteiros serão atribuídos a porta
        port = Integer.parseInt(args[0]);
        if(port < 1024) {
            System.err.println("A porta deve ser maior que 1024");
            System.exit(1);
        }
        return port;
    }

}
