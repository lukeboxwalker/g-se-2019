package de.techfak.gse.lwalkenhorst.exceptions;

public abstract class ExitCodeException extends Exception {

    public static final long serialVersionUID = 42L;
    private final int exitCode;

    ExitCodeException(String message, final int errorCode) {
        super(message);
        this.exitCode = errorCode;
    }

    @Override
    public String getMessage() {
        return "[ERROR-" + exitCode +"] " + super.getMessage();
    }

    public int getExitCode() {
        return exitCode;
    }
}
