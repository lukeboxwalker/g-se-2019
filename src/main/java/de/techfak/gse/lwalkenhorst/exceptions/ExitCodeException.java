package de.techfak.gse.lwalkenhorst.exceptions;

/**
 * An Exception that is supposed to exit the program.
 * The Exception has an explicit exit code, which is used when
 * exiting the program via {@link #exit()}.
 */
public abstract class ExitCodeException extends Exception {

    public static final long serialVersionUID = 42L;
    private final int exitCode;

    ExitCodeException(String message, final int errorCode) {
        super(message);
        this.exitCode = errorCode;
    }

    @Override
    public String getMessage() {
        return "[ERROR-" + exitCode + "] " + super.getMessage();
    }

    public int getExitCode() {
        return exitCode;
    }

    public void exit() {
        System.err.println(this.getMessage());
        System.exit(this.getExitCode());
    }
}
