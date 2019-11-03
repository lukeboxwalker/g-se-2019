package de.techfak.gse.lwalkenhorst.exceptions;

abstract class ErrorCodeException extends Exception {

    public static final long serialVersionUID = 42L;
    private final int errorCode;

    ErrorCodeException(String message, final int errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

    @Override
    public String getMessage() {
        return "[ERROR-" + errorCode +"] " + super.getMessage();
    }

    public int getErrorCode() {
        return errorCode;
    }
}
