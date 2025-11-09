package fr.umlp.m1info.projectvms.group2.exception;

import java.util.List;

/**
 * The SyntaxException represents an error that occurs when the parser detects
 * one or mode syntax errors in the source code
 */
public class SyntaxException extends RuntimeException {

	private final List<String> errors;

	/**
	 * Constructs a new SyntaxException with the given list of syntax errors
	 *
	 * @param errors the list of syntax error messages
	 */
	public SyntaxException(List<String> errors) {
		super(buildMessage(errors));
		this.errors = errors;
	}

	/**
	 * Returns the list of individual syntax errors that caused this exception
	 *
	 * @reutrn the list of syntax error messages
	 */
	public List<String> getErrors() {
		return errors;
	}

	/**
	 * Builds a single formatted message string from all collected syntax errors
	 *
	 * @param errors the list of syntax error messages
	 * @return a formatted error summary string
	 */
	private static String buildMessage(List<String> errors) {
		if (errors == null || errors.isEmpty()) {
			return "Unknown syntax error";
		}
		StringBuilder sb = new StringBuilder();
		for (String err : errors) {
			sb.append(err).append("\n");
		}
		sb.append(errors.size()).append(" error(s)");
		return sb.toString();
	}
}
