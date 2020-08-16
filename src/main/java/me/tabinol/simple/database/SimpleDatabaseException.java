package me.tabinol.simple.database;

/**
 * Simple database exception.
 */
public class SimpleDatabaseException extends Exception {

    /**
     * Simple database exception constructor.
     *
     * @param message the message
     */
    public SimpleDatabaseException(final String message) {
        super(message);
    }

    /**
     * Simple database exception constructor.
     *
     * @param message the message
     * @param cause   the cause
     */
    public SimpleDatabaseException(final String message, final Throwable cause) {
        super(message, cause);
    }
}
