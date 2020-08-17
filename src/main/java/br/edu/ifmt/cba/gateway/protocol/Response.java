package br.edu.ifmt.cba.gateway.protocol;

/**
 * @author daohn on 16/08/2020
 * @project gateway_server
 */
public class Response {
    public Status status;
    public String confirmMessage;
    public String message;

    @Override public String toString() {
        return "Response{" +
                "status=" + status +
                ", confirmMessage='" + confirmMessage + '\'' +
                ", message='" + message + '\'' +
                '}';
    }
}
