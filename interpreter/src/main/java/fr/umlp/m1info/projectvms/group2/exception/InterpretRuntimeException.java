package fr.umlp.m1info.projectvms.group2.exception;

/**
 * Thrown to indicate that an error occurred during the interpretation of a
 * statement node in the interpreter
 */
public class InterpretRuntimeException extends RuntimeException {

	/**
	 * Constructs a new InterpretRuntimeException with the specified detail message
	 *
	 * @param message a detailed meesage describing the interpretation error
	 */
	public InterpretRuntimeException(String message) {
		super(message);
	}
}
