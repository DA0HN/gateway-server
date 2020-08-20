package br.edu.ifmt.cba.gateway.protocol.send;

/**
 * @author daohn on 16/08/2020
 * @project gateway_server
 */
public class Response implements IMessageToSend {
    private Status status;
    private String confirmMessage;
    private String message;

    @Override public String toString() {
        return "Response{" +
                "status=" + status +
                ", confirmMessage='" + confirmMessage + '\'' +
                ", message='" + message + '\'' +
                '}';
    }

    @Override public Status getStatus() {
        return status;
    }

    @Override public String getConfirmMessage() {
        return confirmMessage;
    }

    @Override public String getMessage() {
        return message;
    }

    public void setConfirmMessage(String confirmMessage) {
        this.confirmMessage = confirmMessage;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setStatus(Status status) {
        this.status = status;
    }
}
