package fr.umlp.m1info.projectvms.group2.styler;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;


public class MiniJajaStyler extends CodeStyler{
	private static final String[] KEYWORDS_MAIN = new String[]{
			"class", "main", "return"
	};

	private static final String[] KEYWORDS_BIS = new String[]{
			"if", "else", "while"
	};

	private static final String[] BUILTIN_FUNCTIONS = new String[]{
			"length", "write", "writeln"
	};


	private static final String KEYWORDS_MAIN_PATTERN = "\\b(" + String.join("|", KEYWORDS_MAIN) + ")\\b";
	private static final String KEYWORDS_BIS_PATTERN = "\\b(" + String.join("|", KEYWORDS_BIS) + ")\\b";
	private static final String BUILTIN_FUNCTIONS_PATTERN = "\\b(" + String.join("|", BUILTIN_FUNCTIONS) + ")\\b";

	private static final String GROUP_KEYWORD_MAIN = "KEYWORDMAIN";
	private static final String GROUP_KEYWORD_BIS = "KEYWORDBIS";
	private static final String GROUP_BUILTIN_FUNCTIONS = "BUILTINFUNCTIONS";

	private static final Map<String, String> groupToStyleClass;

	static {
		groupToStyleClass = new HashMap<>();
		groupToStyleClass.put(GROUP_KEYWORD_MAIN, "keyword_main");
		groupToStyleClass.put(GROUP_KEYWORD_BIS, "keyword_bis");
		groupToStyleClass.put(GROUP_BOOLEAN, "boolean");
		groupToStyleClass.put(GROUP_TYPES, "types");
		groupToStyleClass.put(GROUP_BUILTIN_FUNCTIONS, "builtin_functions");
		groupToStyleClass.put(GROUP_PAREN, "paren");
		groupToStyleClass.put(GROUP_BRACE, "brace");
		groupToStyleClass.put(GROUP_BRACKET, "bracket");
		groupToStyleClass.put(GROUP_SEMICOLON, "semicolon");
		groupToStyleClass.put(GROUP_STRING, "string");
		groupToStyleClass.put(GROUP_COMMENT, "comment");
	}

	private static final Pattern PATTERN = Pattern.compile(
			"(?<" + GROUP_KEYWORD_MAIN + ">" + KEYWORDS_MAIN_PATTERN + ")" +
					"|(?<" + GROUP_KEYWORD_BIS + ">" + KEYWORDS_BIS_PATTERN + ")" +
					"|(?<" + GROUP_BOOLEAN + ">" + BOOLEANS_PATTERN + ")" +
					"|(?<" + GROUP_TYPES + ">" + TYPES_PATTERN + ")" +
					"|(?<" + GROUP_BUILTIN_FUNCTIONS + ">" + BUILTIN_FUNCTIONS_PATTERN + ")" +
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

	public MiniJajaStyler(String text) {
		super(text);
	}
}