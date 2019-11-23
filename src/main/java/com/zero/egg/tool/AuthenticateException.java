package com.zero.egg.tool;

/**
 * @author lym
 */
public class AuthenticateException extends RuntimeException {

    private static final long serialVersionUID = -2217431435166121737L;

    private int code;

    public AuthenticateException(String message) {
        super(message);
    }

    public AuthenticateException(int code, String message) {
        super(message);
        this.code = code;
    }

    /**
     * @return the code
     */
    public int getCode() {
        return code;
    }

    /**
     * @param code the code to set
     */
    public void setCode(int code) {
        this.code = code;
    }
}
