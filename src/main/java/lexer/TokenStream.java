package lexer;

import java.util.ArrayList;
import java.util.List;

import static myutils.Macro.todo_report_syntax_error;
import static myutils.Macro.verify;

public class TokenStream {
    private int cursor = 0;
    private String text;
    private StringBuilder accumulator;

    public TokenStream(String text) {
        verify(text != null && !text.isEmpty());
        this.text = text.trim();
    }

    char peek() {
        return peek(1).charAt(0);
    }

    String peek(int count) {
        if ((cursor + count) > text.length())
            todo_report_syntax_error();
        int oldCursor = cursor;
        StringBuilder builder = new StringBuilder();
        while (count != 0) {
            builder.append(consumeNextChar());
            count -= 1;
        }
        cursor = oldCursor;
        return builder.toString();
    }

    char consumeNextChar() {
        if (eof())
            todo_report_syntax_error();
        return text.charAt(cursor++);
    }

    public boolean eof() {
        return cursor == text.length();
    }

    char accumulate() {
        char ch = consumeNextChar();
        accumulator.append(ch);
        return ch;
    }

    private void consumeWhitespaces() {
        while (Utils.isWhitespace(peek()))
            consumeNextChar();
    }

    String accumulatedString() {
        return accumulator.toString();
    }

    private void resetAccumulator() {
        accumulator = new StringBuilder();
    }

    public Token consumeNextToken() {
        resetAccumulator();
        consumeWhitespaces();
        if (eof())
            return new Token(Token.Type.EOF, null);

        while (true) {
            accumulate();

            // Deal with line comments first.
            if (accumulatedString().equals("/")) {
                char next = peek();
                if (next == '/') {
                    // This is a line comment.
                    while (!eof() && accumulate() != '\n') ;
                    return new Token(Token.Type.LineCommentT, accumulatedString());
                }
            }

            if (accumulatedString().matches("[a-z]")) {
                for (var keyword : Token.keywordLexemes) {
                    // Maybe keyword.
                    char nextChar = peek();
                    while (keyword.contains(accumulatedString() + nextChar)) {
                        accumulate();
                        nextChar = peek();
                    }
                    if (keyword.length() == accumulatedString().length()) {
                        if (!Utils.isValidIdentifierChar(peek()))
                            return new Token(Token.Type.KeywordT, keyword);
                    }
                }
            }

            if (Token.operatorLexemes.contains(accumulatedString())) {
                // Operator.
                switch (accumulatedString()) {
                    case "+":
                        Token.Type operatorType;
                        char next = peek();
                        if (next == '=' || next == '+')
                            accumulate();
                        operatorType = next == '+' ? Token.Type.UnaryOperatorT : Token.Type.UnaryOrBinaryOperatorT;
                        return new Token(operatorType, accumulatedString());
                    case "-":
                        next = peek();
                        if (next == '=' || next == '-')
                            accumulate();
                        operatorType = next == '-' ? Token.Type.UnaryOperatorT : Token.Type.UnaryOrBinaryOperatorT;
                        return new Token(operatorType, accumulatedString());
                    case "!":
                        operatorType = Token.Type.UnaryOperatorT;
                        next = peek();
                        if (next == '=') {
                            accumulate();
                            operatorType = Token.Type.BinaryOperatorT;
                        }
                        return new Token(operatorType, accumulatedString());
                    case "/":
                    case "*":
                    case "%":
                    case ">":
                    case "<":
                    case "=":
                        next = peek();
                        if (next == '=')
                            accumulate();
                        return new Token(Token.Type.BinaryOperatorT, accumulatedString());

                    case "|":
                        next = accumulate();
                        if (next != '|')
                            todo_report_syntax_error();
                        return new Token(Token.Type.BinaryOperatorT, accumulatedString());

                    case "&":
                        next = accumulate();
                        if (next != '&')
                            todo_report_syntax_error();
                        return new Token(Token.Type.BinaryOperatorT, accumulatedString());
                    default:
                        todo_report_syntax_error();
                        return null;
                }
            }

            if (accumulatedString().matches("^[a-zA-Z_][a-zA-Z_0-9]*$")) {
                // Identifier.
                while (Utils.isValidIdentifierChar(peek()))
                    accumulate();
                return new Token(Token.Type.IdentifierT, accumulatedString());
            }

            if (accumulatedString().equals("\"")) {
                // String literal.
                while (peek() != '"') {
                    char next = accumulate();
                    if (next == '\\') {
                        switch (accumulate()) {
                            case '\\':
                            case 'n':
                            case '"':
                                break;
                            default:
                                todo_report_syntax_error();
                        }
                    }
                }
                consumeNextChar(); // Consume the '"'
                return new Token(Token.Type.StringLiteralT, accumulatedString().substring(1));
            }

            if (accumulatedString().matches("[.0-9]")) {
                String str = accumulatedString();
                if (str.equals(".")) {
                    if (!peek(1).matches("[0-9]"))
                        return new Token(Token.Type.DotT, str);
                }

                // Number literal.
                enum States {
                    BEGIN,
                    SEEN_DECIMAL,
                    SEEN_DIGIT_AFTER_DECIMAL,
                    SEEN_E,
                    SEEN_ESIGN,
                    SEEN_DIGIT_AFTER_E,
                    SEEN_DIGIT_AFTER_ESIGN,
                    END
                }

                var state = accumulatedString().equals(".") ? States.SEEN_DECIMAL : States.BEGIN;
                while (state != States.END) {
                    switch (state) {
                        case BEGIN: {
                            String nextChar = peek(1).toLowerCase();
                            if (nextChar.matches("[0-9]"))
                                accumulate();
                            else if (nextChar.equals(".")) {
                                state = States.SEEN_DECIMAL;
                                accumulate();
                            } else if (nextChar.equals("e")) {
                                state = States.SEEN_E;
                                accumulate();
                            } else state = States.END;
                            break;
                        }

                        case SEEN_DECIMAL: {
                            String nextChar = peek(1).toLowerCase();
                            if (nextChar.matches("[0-9]")) {
                                accumulate();
                                state = States.SEEN_DIGIT_AFTER_DECIMAL;
                            } else todo_report_syntax_error();
                            break;
                        }

                        case SEEN_DIGIT_AFTER_DECIMAL: {
                            String nextChar = peek(1).toLowerCase();
                            if (nextChar.matches("[0-9]"))
                                accumulate();
                            else if (nextChar.equals("e")) {
                                accumulate();
                                state = States.SEEN_E;
                            } else state = States.END;
                        }

                        case SEEN_E: {
                            String nextChar = peek(1).toLowerCase();
                            if (nextChar.matches("[+-]")) {
                                state = States.SEEN_ESIGN;
                                accumulate();
                            } else if (nextChar.matches("[0-9]")) {
                                state = States.SEEN_DIGIT_AFTER_E;
                                accumulate();
                            } else todo_report_syntax_error();
                            break;
                        }

                        case SEEN_DIGIT_AFTER_E:
                        case SEEN_DIGIT_AFTER_ESIGN: {
                            String nextChar = peek(1);
                            if (nextChar.matches("[0-9]"))
                                accumulate();
                            else state = States.END;
                            break;
                        }

                        case SEEN_ESIGN: {
                            String nextChar = peek(1);
                            if (nextChar.matches("[0-9]")) {
                                state = States.SEEN_DIGIT_AFTER_ESIGN;
                                accumulate();
                            } else todo_report_syntax_error();
                            break;
                        }
                    }
                }

                return new Token(Token.Type.NumberLiteralT, accumulatedString());
            }

            if (accumulatedString().equals(":"))
                return new Token(Token.Type.ColonT, accumulatedString());
            if (accumulatedString().equals(","))
                return new Token(Token.Type.CommaT, accumulatedString());
            if (accumulatedString().equals("["))
                return new Token(Token.Type.LeftBracketT, accumulatedString());
            if (accumulatedString().equals("]"))
                return new Token(Token.Type.RightBracketT, accumulatedString());
            if (accumulatedString().equals("("))
                return new Token(Token.Type.LeftParenT, accumulatedString());
            if (accumulatedString().equals(")"))
                return new Token(Token.Type.RightParenT, accumulatedString());
            if (accumulatedString().equals("{"))
                return new Token(Token.Type.LeftCurlyT, accumulatedString());
            if (accumulatedString().equals("}"))
                return new Token(Token.Type.RightCurlyT, accumulatedString());
            if (accumulatedString().equals("."))
                return new Token(Token.Type.DotT, accumulatedString());
            if (accumulatedString().equals(";"))
                return new Token(Token.Type.SemiColonT, accumulatedString());
        }
    }

    public List<Token> peekTokens(int count) {
        List<Token> tokens = new ArrayList<>();
        int oldCursor = cursor;
        while (count != 0) {
            tokens.add(consumeNextToken());
            count -= 1;
        }
        cursor = oldCursor;
        return tokens;
    }

    public Token peekNextToken() {
        return peekTokens(1).get(0);
    }

    public Token consumeIdentifier() {
        return consumeAndMatch(Token.Type.IdentifierT);
    }

    private Token consumeAndMatch(Token.Type type) {
        var nextToken = consumeNextToken();
        if (nextToken.getType() != type)
            todo_report_syntax_error();
        return nextToken;
    }

    public Token consumeAndMatch(String tokenValue) {
        var nextToken = consumeNextToken();
        if (!nextToken.getValue().equals(tokenValue))
            todo_report_syntax_error();
        return nextToken;
    }

    public void dump() {
        while (!eof())
            consumeNextToken().dump();
    }
}
