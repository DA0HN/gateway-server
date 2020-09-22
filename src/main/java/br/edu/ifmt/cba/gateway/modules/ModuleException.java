package br.edu.ifmt.cba.gateway.modules;

/**
 * @author daohn on 21/09/2020
 * @project gateway_server
 */
public class ModuleException extends Exception {
    public ModuleException() {
    }

    public ModuleException(String message) {
        super(message);
    }

    public ModuleException(String message, Throwable cause) {
        super(message, cause);
    }
}
