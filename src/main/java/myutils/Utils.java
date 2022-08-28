package myutils;

public class Utils {
    public static boolean isWhitespace(char ch) {
        return ch == ' ' || ch == '\n';
    }

    public static boolean isValidIdentifierChar(char ch) {
        return String.valueOf(ch).matches("[a-zA-Z0-9_]");
    }

}
