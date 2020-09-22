package br.edu.ifmt.cba.gateway.modules;

/**
 * @author daohn on 21/09/2020
 * @project gateway_server
 */
public interface IModule {
    /**
     * executa a regra de negócio do módulo da mensagem baseado em sua implementação
     * @param message mensagem recebida
     */
    void execute(String message);
}
