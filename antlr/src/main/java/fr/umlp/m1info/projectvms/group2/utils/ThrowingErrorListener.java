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

public class ThrowingErrorListener extends BaseErrorListener {

	public static final ThrowingErrorListener INSTANCE = new ThrowingErrorListener();

	private final List<String> errors = new ArrayList<>();

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
	 * Returns collected errors. Throws exception if any exist.
	 * 
	 * @throws MiniJajaSyntaxException if errors occurs while parsing
	 */
	public void throwIfErrors() throws SyntaxException {
		if (errors.isEmpty()) {
			return;
		}
		List<String> copy = new ArrayList<>(errors);
		errors.clear();
		throw new SyntaxException(copy);
	}

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
