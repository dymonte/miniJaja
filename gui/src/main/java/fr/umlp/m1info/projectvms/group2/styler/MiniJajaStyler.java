package fr.umlp.m1info.projectvms.group2.styler;

import org.fxmisc.richtext.model.StyleSpans;
import org.fxmisc.richtext.model.StyleSpansBuilder;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class MiniJajaStyler {
    private static final String[] KEYWORDS_MAIN = new String[] {
            "class", "main", "return"
    };

    private static final String[] KEYWORDS_BIS = new String[] {
            "if", "else", "while"
    };

    private static final String[] BOOLEANS = new String[] {
            "true", "false"
    };

    private static final String[] TYPES = new String[] {
            "int", "bool", "void"
    };

    private static final String [] BUILTIN_FUNCTIONS = new String[] {
        "length", "write", "writeln"
    };


    private static final String KEYWORDS_MAIN_PATTERN = "\\b(" + String.join("|", KEYWORDS_MAIN) + ")\\b";
    private static final String KEYWORDS_BIS_PATTERN = "\\b(" + String.join("|", KEYWORDS_BIS) + ")\\b";
    private static final String BOOLEANS_PATTERN = "\\b(" + String.join("|", BOOLEANS) + ")\\b";
    private static final String TYPES_PATTERN = "\\b(" + String.join("|", TYPES) + ")\\b";
    private static final String BUILTIN_FUNCTIONS_PATTERN = "\\b(" + String.join("|", BUILTIN_FUNCTIONS) + ")\\b";
    private static final String PAREN_PATTERN = "\\(|\\)";
    private static final String BRACE_PATTERN = "\\{|\\}";
    private static final String BRACKET_PATTERN = "\\[|\\]";
    private static final String SEMICOLON_PATTERN = "\\;";
    private static final String STRING_PATTERN = "\"([^\"\\\\]|\\\\.)*\"";
    private static final String COMMENT_PATTERN = "//[^\n]*" + "|" + "/\\*(.|\\R)*?\\*/"   // for whole text processing (text blocks)
            + "|" + "/\\*[^\\v]*" + "|" + "^\\h*\\*([^\\v]*|/)";  // for visible paragraph processing (line by line)

    private static final String GROUP_KEYWORD_MAIN = "KEYWORDMAIN";
    private static final String GROUP_KEYWORD_BIS = "KEYWORDBIS";
    private static final String GROUP_BOOLEAN = "BOOLEAN";
    private static final String GROUP_TYPES = "TYPES";
    private static final String GROUP_BUILTIN_FUNCTIONS = "BUILTINFUNCTIONS";
    private static final String GROUP_PAREN = "PAREN";
    private static final String GROUP_BRACE = "BRACE";
    private static final String GROUP_BRACKET = "BRACKET";
    private static final String GROUP_SEMICOLON = "SEMICOLON";
    private static final String GROUP_STRING = "STRING";
    private static final String GROUP_COMMENT = "COMMENT";

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


    private final String text;
    private final Matcher matcher;
    private final StyleSpansBuilder<Collection<String>> spansBuilder;
    private int lastSpanEnd;

    public MiniJajaStyler(String text) {
        this.text = text;
        this.matcher = PATTERN.matcher(text);
        this.spansBuilder = new StyleSpansBuilder<>();
        this.lastSpanEnd = 0;
    }

    public StyleSpans<Collection<String>> style() {
        while(matcher.find()) {
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
        for (String groupName : groupToStyleClass.keySet()) {
            // If matcher found something that matches the group name, we return the associated style
            if(matcher.group(groupName) != null) {
                return groupToStyleClass.get(groupName);
            }
        }
        return null; /* never happens */
    }
}