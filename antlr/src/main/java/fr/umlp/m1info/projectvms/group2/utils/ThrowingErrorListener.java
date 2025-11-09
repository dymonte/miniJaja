package fr.umlp.m1info.projectvms.group2.utils;

import fr.umlp.m1info.projectvms.group2.exception.SyntaxException;

import java.util.ArrayList;
import java.util.List;

import org.antlr.v4.runtime.BaseErrorListener;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.Parser;
import org.antlr.v4.runtime.RecognitionException;
import org.antlr.v4.runtime.Recognizer;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.TokenStream;

/**
 * The ThrowingErrorListener is a custom ANTLR error listener that collects
 * syntax errors during parsing and throws a SyntaxException once parsing
 * completes if any errors were detected
 */
public class ThrowingErrorListener extends BaseErrorListener {

	/**
	 * Singleton instance of the error listener
	 */
	public static final ThrowingErrorListener INSTANCE = new ThrowingErrorListener();

	private final List<String> errors = new ArrayList<>();

	/**
	 * Handles syntax errors detected by the parser and recors them with line and
	 * column information
	 *
	 * @param recognizer         the parser or lexer that detected the error
	 * @param offendingSymbol    the token that caused the error
	 * @param line               the line number where the error occured
	 * @param charPositionInLine the position of the offending symbol in the line
	 * @param msh                the raw ANTLR error message
	 * @param e                  the recognition exception, if any
	 */
	@Override
	public void syntaxError(Recognizer<?, ?> recognizer, Object offendingSymbol, int line, int charPositionInLine,
			String msg, RecognitionException e) {
		String offending = "";
		if (offendingSymbol instanceof Token token) {
			offending = token.getText();
		}

		String cleanMsg = formatMessage(msg, offending);

		StringBuilder sb = new StringBuilder();
		sb.append("[" + line + "," + charPositionInLine + "]")
				.append(" error: ")
				.append(cleanMsg)
				.append("\n");

		String codeLine = getLineFromTokens(recognizer, line);
		if (codeLine != null) {
			sb.append("    ").append(codeLine).append("\n");
			sb.append("    ").append(" ".repeat(Math.max(0, charPositionInLine))).append("^");
		}

		errors.add(sb.toString());
	}

	/**
	 * Throws a SyntaxException if any syntax errors were recorded.
	 * Clears the internal error list after throwing.
	 *
	 * @throws SyntaxException if one or more syntax errors occurred during parsing
	 */
	public void throwIfErrors() throws SyntaxException {
		if (errors.isEmpty()) {
			return;
		}
		List<String> copy = new ArrayList<>(errors);
		errors.clear();
		throw new SyntaxException(copy);
	}

	/**
	 * Retrieves the original line of source code where a syntax error occurred.
	 *
	 * @param recognizer the parser used to obtain token information
	 * @param line       the line number of the syntax error
	 * @return the source line as a string, or {@code null} if unavailable
	 */
	private String getLineFromTokens(Recognizer<?, ?> recognizer, int line) {
		if (recognizer instanceof Parser parser) {
			TokenStream tokens = parser.getInputStream();
			if (tokens instanceof CommonTokenStream stream) {
				List<Token> all = stream.getTokens();

				StringBuilder sb = new StringBuilder();
				for (Token t : all) {
					if (t.getLine() == line) {
						sb.append(t.getText());
						sb.append(" ");
					}
				}
				return sb.toString().trim();
			}
		}
		return null;
	}

	/**
	 * Formats and simplifies raw ANTLR error messages into more user-friendly
	 * forms.
	 *
	 * @param msg       the raw ANTLR error message
	 * @param offending the text of the offending token
	 * @return a cleaned-up and human-readable error message
	 */
	private String formatMessage(String msg, String offending) {
		msg = msg.toLowerCase();

		if (msg.contains("missing"))
			return msg.replace("missing", "").trim() + " expected";
		if (msg.contains("mismatched input"))
			return "illegal start of statement near '" + offending + "'";
		if (msg.contains("no viable alternative"))
			return "invalid syntax near '" + offending + "'";
		return msg;
	}
}
