package fr.umlp.m1info.projectvms.group2.exception;

/**
 * Thrown to indicate that an error occurred during the evaluation of an
 * expression node in the interpreter.
 */
public class EvalRuntimeException extends RuntimeException {

	/**
	 * Constructs a new EvalRuntimeException with the specified detail message.
	 *
	 * @param message a detailed message describing the evaluation error
	 */
	public EvalRuntimeException(String message) {
		super(message);
	}

	/**
	 * Constructs a new EvalRuntimeException with the specified detail message
	 * and cause.
	 *
	 * @param message a detailed message describing the evaluation error
	 * @param cause   the underlying cause of the error
	 */
	public EvalRuntimeException(String message, Throwable cause) {
		super(message, cause);
	}
}
