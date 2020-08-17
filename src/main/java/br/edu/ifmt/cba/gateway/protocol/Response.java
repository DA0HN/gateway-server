package br.edu.ifmt.cba.gateway.service;

/**
 * @author daohn on 16/08/2020
 * @project gateway_server
 */
public class Response {
    protected Status status;
    protected String confirmMessage;
    protected String message;

    @Override public String toString() {
        return "Response{" +
                "status=" + status +
                ", confirmMessage='" + confirmMessage + '\'' +
                ", message='" + message + '\'' +
                '}';
    }
}
