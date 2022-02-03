package lexer;

import myutils.Assertable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Token implements Assertable {
    public static enum Type {
        Colon,
        Comma,
        Dot,
        Identifier,
        Keyword,
        LeftBracket,
        LeftCurly,
        LeftParen,
        LineComment,
        NumberLiteral,
        Operator,
        RightBracket,
        RightCurly,
        RightParen,
        SemiColon,
        StringLiteral,
    }

    public static final List<String> keywordLexemes = new ArrayList<>();
    public static final List<String> operatorLexemes = new ArrayList<>();

    static {
        final String keywordsStr = "let function for while true false if else break continue return switch";
        keywordLexemes.addAll(Arrays.asList(keywordsStr.split(" ")));
        final String operatorsStr = "+ - * / % = ! | & > <";
        operatorLexemes.addAll(Arrays.asList(operatorsStr.split(" ")));
    }

    public Token(Type type, String value) {
        this.type = type;
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public Type getType() {
        return type;
    }

    public void dump() {
        System.out.println("{ type: " + type + ", value: '" + value + "' }");
    }

    final private String value;
    final private Type type;
}
