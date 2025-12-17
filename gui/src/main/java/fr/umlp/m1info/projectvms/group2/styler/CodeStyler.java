package fr.umlp.m1info.projectvms.group2.styler;

import org.fxmisc.richtext.model.StyleSpans;
import org.fxmisc.richtext.model.StyleSpansBuilder;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class CodeStyler {
	private static final String[] BOOLEANS = new String[]{
			"true", "false"
	};

	private static final String[] TYPES = new String[]{
			"int", "boolean", "void"
	};

	protected static final String BOOLEANS_PATTERN = "\\b(" + String.join("|", BOOLEANS) + ")\\b";
	protected static final String TYPES_PATTERN = "\\b(" + String.join("|", TYPES) + ")\\b";

	protected static final String PAREN_PATTERN = "\\(|\\)";
	protected static final String BRACE_PATTERN = "\\{|\\}";
	protected static final String BRACKET_PATTERN = "\\[|\\]";
	protected static final String SEMICOLON_PATTERN = "\\;";
	protected static final String STRING_PATTERN = "\"([^\"\\\\]|\\\\.)*\"";
	protected static final String COMMENT_PATTERN = "//[^\n]*" + "|" + "/\\*(.|\\R)*?\\*/"   // for whole text processing (text blocks)
			+ "|" + "/\\*[^\\v]*" + "|" + "^\\h*\\*([^\\v]*|/)";  // for visible paragraph processing (line by line)

	protected static final String GROUP_BOOLEAN = "BOOLEAN";
	protected static final String GROUP_TYPES = "TYPES";
	protected static final String GROUP_PAREN = "PAREN";
	protected static final String GROUP_BRACE = "BRACE";
	protected static final String GROUP_BRACKET = "BRACKET";
	protected static final String GROUP_SEMICOLON = "SEMICOLON";
	protected static final String GROUP_STRING = "STRING";
	protected static final String GROUP_COMMENT = "COMMENT";

	protected final String text;
	protected final Matcher matcher;
	protected final StyleSpansBuilder<Collection<String>> spansBuilder;
	protected int lastSpanEnd;

	protected abstract Map<String, String> getGroupToStyleClass();
	protected abstract Pattern buildPattern();

	public CodeStyler(String text) {
		this.text = text;
		this.spansBuilder = new StyleSpansBuilder<>();
		this.lastSpanEnd = 0;
		this.matcher = buildPattern().matcher(text);
	}

	public StyleSpans<Collection<String>> style() {
		while (matcher.find()) {
			String styleClass = evaluateNextStyle();
			evaluateMatch(matcher.start(), matcher.end(), styleClass);
		}
		completeStyleToEnd();
		return spansBuilder.create();
	}

	private void completeStyleToEnd() {
		spansBuilder.add(Collections.emptyList(), text.length() - lastSpanEnd);
	}

	private void evaluateMatch(int start, int end, String styleClass) {
		assert styleClass != null;
		// From the last style found to the new one we apply an empty list (no style)
		spansBuilder.add(Collections.emptyList(), start - lastSpanEnd);
		// Then we apply the one found by the matcher
		spansBuilder.add(Collections.singleton(styleClass), end - start);
		// Save the end
		lastSpanEnd = end;
	}

	private String evaluateNextStyle() {
		for (String groupName : getGroupToStyleClass().keySet()) {
			if (matcher.group(groupName) != null) {
				return getGroupToStyleClass().get(groupName);
			}
		}
		return null; // never happens
	}
}
