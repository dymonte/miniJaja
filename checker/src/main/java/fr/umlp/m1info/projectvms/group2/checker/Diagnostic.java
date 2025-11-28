package fr.umlp.m1info.projectvms.group2.checker;

import java.util.ArrayList;
import java.util.List;

/**
 * Diagnostic utility class for reporting issues (errors, warnings, notes)
 * during
 * type checking or compilation phases
 */
public class Diagnostic {
	/**
	 * Severity levels for diagnostic messages.
	 */
	public enum Severity {
		ERROR,
		WARNING,
		NOTE,
	}

	/**
	 * Error codes representing specific types of issues
	 */
	public enum ErrorCode {
		TYPE_MISMATCH("E1001"),
		KIND_MISMATCH("E1002"),

		UNKNOWN_VARIABLE("E1011"),
		UNKNOWN_FUNCTION("E1012"),

		RETURN_TYPE_MISMATCH("E1021"),
		MISSING_RETURN_STATEMENT("E1022"),

		ARGUMENT_MISMATCH("E1031"),

		METHOD_ALREADY_EXISTS("E1041"),
		;

		public final String code;

		ErrorCode(String code) {
			this.code = code;
		}

		@Override
		public String toString() {
			return code;
		}
	}

	/**
	 * Represents a single diagnostic issue with severity, code, message, and
	 * location
	 *
	 * @param severity  the severity of the issue
	 * @param errorCode the specific error code
	 * @param msg       descriptive message
	 * @param line      line number in source code (1-based)
	 * @param col       column number in source code (1-based)
	 */
	public record Issue(Severity severity, ErrorCode errorCode, String msg, int line, int col) {
		@Override
		public String toString() {
			return "%s %s [%d:%d]: %s".formatted(severity, errorCode, line, col, msg);
		}
	}

	private final List<Issue> list = new ArrayList<>();

	/**
	 * Adds an error issue to the diagnostic list
	 *
	 * @param code the error code
	 * @param msg  descriptive message
	 * @param line line number where the error occurred
	 * @param col  column number where the error occurred
	 */
	public void error(ErrorCode code, String msg, int line, int col) {
		list.add(new Issue(Severity.ERROR, code, msg, line, col));
	}

	/**
	 * Adds a warning issue to the diagnostic list
	 *
	 * @param code the warning code
	 * @param msg  descriptive message
	 * @param line line number where the warning occurred
	 * @param col  column number where the warning occurred
	 */
	public void warning(ErrorCode code, String msg, int line, int col) {
		list.add(new Issue(Severity.WARNING, code, msg, line, col));
	}

	/**
	 * Adds an informational note to the diagnostic list
	 *
	 * @param code the note code
	 * @param msg  descriptive message
	 * @param line line number where the note applies
	 * @param col  column number where the note applies
	 */
	public void note(ErrorCode code, String msg, int line, int col) {
		list.add(new Issue(Severity.NOTE, code, msg, line, col));
	}

	/**
	 * Returns a copy of all diagnostic issues collected so far.
	 * The returned list is immutable
	 *
	 * @return list of issues
	 */
	public List<Issue> issues() {
		return List.copyOf(list);
	}
}
