package fr.umlp.m1info.projectvms.group2.exception;

/**
 * Exception thrown when an error occurs while building the AST (Abstract Syntax
 * Tree).
 */
public class VisitorBuildException extends RuntimeException {

	/**
	 * Constructs a new ASTBuildException with the specified detail message.
	 *
	 * @param message the detail message
	 */
	public VisitorBuildException(String message) {
		super(message);
	}
}
