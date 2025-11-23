package fr.umlp.m1info.projectvms.group2.checker;

import java.util.ArrayList;
import java.util.List;

public class Diagnostic {
	public enum Severity {
		ERROR,
		WARNING,
		NOTE,
	}

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

	public record Issue(Severity severity, ErrorCode errorCode, String msg, int line, int col) {
		@Override
		public String toString() {
			return "%s %s [%d:%d]: %s".formatted(severity, errorCode, line, col, msg);
		}
	}

	private final List<Issue> list = new ArrayList<>();

	public void error(ErrorCode code, String msg, int line, int col) {
		list.add(new Issue(Severity.ERROR, code, msg, line, col));
	}

	public void warning(ErrorCode code, String msg, int line, int col) {
		list.add(new Issue(Severity.WARNING, code, msg, line, col));
	}

	public void note(ErrorCode code, String msg, int line, int col) {
		list.add(new Issue(Severity.NOTE, code, msg, line, col));
	}

	public List<Issue> issues() {
		return List.copyOf(list);
	}
}
