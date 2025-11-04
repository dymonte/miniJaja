package fr.umlp.m1info.projectvms.group2.exception;

/**
 * Thrown to indicate that an error occurent during the free compilation of a
 * statement node in the compiler
 */
public class FreeCompilationRuntimeException extends RuntimeException {

	/**
	 * Constructs a new FreeCompilationRuntimeException with the specified detail
	 * message
	 *
	 * @param message a detailed message describing the interpretation error
	 */
	public FreeCompilationRuntimeException(String message) {
		super(message);
	}
}
