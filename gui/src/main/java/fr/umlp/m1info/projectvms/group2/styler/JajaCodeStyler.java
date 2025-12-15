package fr.umlp.m1info.projectvms.group2.styler;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;


public class JajaCodeStyler extends CodeStyler{
	private static final String[] KEYWORDS_INSTRS = new String[]{
			"init", "swap", "new", "newarray", "invoke",  "length", "return", "write", "writeln", "push",
			"pop", "load", "aload", "store", "astore", "if", "goto", "inc", "ainc", "nop", "jcstop"
};

	private static final String[] KEYWORDS_OPERS = new String[]{
			"neg", "not", "add", "sub", "mul", "div", "cmp", "sup", "or", "and"
	};

	private static final String[] KINDS = new String[]{
			"var", "cst", "arr", "meth"
	};


	private static final String KEYWORDS_INSTRS_PATTERN = "\\b(" + String.join("|", KEYWORDS_INSTRS) + ")\\b";
	private static final String KEYWORDS_OPERS_PATTERN = "\\b(" + String.join("|", KEYWORDS_OPERS) + ")\\b";
	private static final String KINDS_PATTERN = "\\b(" + String.join("|", KINDS) + ")\\b";

	private static final String GROUP_KEYWORD_INSTRS = "KEYWORDINSTRS";
	private static final String GROUP_KEYWORD_OPERS = "KEYWORDOPERS";
	private static final String GROUP_KINDS = "KINDS";

	private static final Map<String, String> groupToStyleClass;

	static {
		groupToStyleClass = new HashMap<>();
		groupToStyleClass.put(GROUP_KEYWORD_INSTRS, "keyword_main");
		groupToStyleClass.put(GROUP_KEYWORD_OPERS, "keyword_bis");
		groupToStyleClass.put(GROUP_BOOLEAN, "boolean");
		groupToStyleClass.put(GROUP_TYPES, "types");
		groupToStyleClass.put(GROUP_KINDS, "builtin_functions");
		groupToStyleClass.put(GROUP_PAREN, "paren");
		groupToStyleClass.put(GROUP_BRACE, "brace");
		groupToStyleClass.put(GROUP_BRACKET, "bracket");
		groupToStyleClass.put(GROUP_SEMICOLON, "semicolon");
		groupToStyleClass.put(GROUP_STRING, "string");
		groupToStyleClass.put(GROUP_COMMENT, "comment");
	}

	private static final Pattern PATTERN = Pattern.compile(
			"(?<" + GROUP_KEYWORD_INSTRS + ">" + KEYWORDS_INSTRS_PATTERN + ")" +
					"|(?<" + GROUP_KEYWORD_OPERS + ">" + KEYWORDS_OPERS_PATTERN + ")" +
					"|(?<" + GROUP_BOOLEAN + ">" + BOOLEANS_PATTERN + ")" +
					"|(?<" + GROUP_TYPES + ">" + TYPES_PATTERN + ")" +
					"|(?<" + GROUP_KINDS + ">" + KINDS_PATTERN + ")" +
					"|(?<" + GROUP_PAREN + ">" + PAREN_PATTERN + ")" +
					"|(?<" + GROUP_BRACE + ">" + BRACE_PATTERN + ")" +
					"|(?<" + GROUP_BRACKET + ">" + BRACKET_PATTERN + ")" +
					"|(?<" + GROUP_SEMICOLON + ">" + SEMICOLON_PATTERN + ")" +
					"|(?<" + GROUP_STRING + ">" + STRING_PATTERN + ")" +
					"|(?<" + GROUP_COMMENT + ">" + COMMENT_PATTERN + ")"
	);

	@Override
	protected Map<String, String> getGroupToStyleClass() {
		return groupToStyleClass;
	}

	@Override
	protected Pattern buildPattern() {
		return PATTERN;
	}

	public JajaCodeStyler(String text) {
		super(text);
	}
}