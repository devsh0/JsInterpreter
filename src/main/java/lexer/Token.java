package lexer;

import myutils.Macro;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Token implements Macro {
    public static enum Type {
        ColonT,
        CommaT,
        DotT,
        EOF,
        IdentifierT,
        KeywordT,
        LeftBracketT,
        LeftCurlyT,
        LeftParenT,
        LineCommentT,
        NumberLiteralT,
        UnaryOperatorT,
        UnaryOrBinaryOperatorT,
        BinaryOperatorT,
        RightBracketT,
        RightCurlyT,
        RightParenT,
        SemiColonT,
        StringLiteralT,
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
