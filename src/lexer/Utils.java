package lexer;

public class Utils {
    static boolean isWhitespace(char ch) {
        return ch == ' ' || ch == '\n';
    }

    static boolean isValidIdentifierChar(char ch) {
        return String.valueOf(ch).matches("[a-zA-Z0-9_]");
    }
}
