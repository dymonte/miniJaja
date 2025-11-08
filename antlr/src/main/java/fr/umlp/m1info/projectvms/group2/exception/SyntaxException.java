package fr.umlp.m1info.projectvms.group2.exception;

import java.util.List;

public class SyntaxException extends RuntimeException {

	private final List<String> errors;

	public SyntaxException(List<String> errors) {
		super(buildMessage(errors));
		this.errors = errors;
	}

	/**
	 * Returns the list of individual errors
	 */
	public List<String> getErrors() {
		return errors;
	}

	/**
	 * Builds a single string from all errors
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
