package com.oxande.commons.oxutils.exception;

/**
 * The basic exception for an application. Includes a resolution message in addition
 * to the basic message.
 * 
 * @author wrey
 * @since 1.0
 *
 */
@SuppressWarnings("serial")
public class BusinessException extends Exception {

    private String resolution;
    
    /**
     * Constructs a new exception with {@code null} as its detail message.
     * The cause is not initialized, and may subsequently be initialized by a
     * call to {@link #initCause}.
     */
    public BusinessException() {
        super();
    }

    /**
     * Constructs a new exception with the specified detail message.  The
     * cause is not initialized, and may subsequently be initialized by
     * a call to {@link #initCause}.
     *
     * @param   message   the detail message. The detail message is saved for
     *          later retrieval by the {@link #getMessage()} method.
     */
    public BusinessException(String message) {
        super(message);
    }

    public BusinessException(String message, String resolution) {
        super(message);
        this.resolution = resolution;
    }

    public BusinessException(String message, String resolution, Throwable cause) {
        super(message, cause);
        this.resolution = resolution;
    }

    /**
     * Constructs a new exception with the specified detail message and
     * cause.  <p>Note that the detail message associated with
     * {@code cause} is <i>not</i> automatically incorporated in
     * this exception's detail message.
     *
     * @param  message the detail message (which is saved for later retrieval
     *         by the {@link #getMessage()} method).
     * @param  cause the cause (which is saved for later retrieval by the
     *         {@link #getCause()} method).  (A <tt>null</tt> value is
     *         permitted, and indicates that the cause is nonexistent or
     *         unknown.)
     */
    public BusinessException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Constructs a new exception with the specified cause and a detail
     * message of <tt>(cause==null ? null : cause.toString())</tt> (which
     * typically contains the class and detail message of <tt>cause</tt>).
     * This constructor is useful for exceptions that are little more than
     * wrappers for other throwables (for example, {@link
     * java.security.PrivilegedActionException}).
     *
     * @param  cause the cause (which is saved for later retrieval by the
     *         {@link #getCause()} method).  (A <tt>null</tt> value is
     *         permitted, and indicates that the cause is nonexistent or
     *         unknown.)
     */
    public BusinessException(Throwable cause) {
        super(cause);
    }

    public String getResolution() {
	return this.resolution;
    }
    
    public void setResolution(String message) {
	this.resolution = message;
    }
}
