package fr.umlp.m1info.projectvms.group2.exception;

/**
 * Thronw to indicate that an error occurent during the compilation of a
 * statement node in the compilation
 */
public class CompilationRuntimeException extends RuntimeException {

	/**
	 * Constructs a new CompilationRuntimeException with the specified detail
	 * message
	 *
	 * @param message a detailed message describing the compilation error
	 */
	public CompilationRuntimeException(String message) {
		super(message);
	}

}
