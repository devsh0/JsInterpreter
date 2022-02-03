package lexer;

import myutils.Assertable;

public class TokenStream implements Assertable {
    private int cursor = 0;
    private String text;
    private StringBuilder accumulator;

    public TokenStream(String text) {
        Assert(text != null && !text.isEmpty());
        this.text = text;
    }

    char peek() {
        return peek(1).charAt(0);
    }

    String peek(int count) {
        if ((cursor + count) >= text.length())
            FixmeReportSyntaxError();
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
            FixmeReportSyntaxError();
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
        while (true) {
            accumulate();
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
                            return new Token(Token.Type.Keyword, keyword);
                    }
                }
            }

            if (Token.operatorLexemes.contains(accumulatedString())) {
                // Operator.
                switch (accumulatedString()) {
                    case "+":
                        char next = peek();
                        if (next == '=' || next == '+')
                            accumulate();
                        return new Token(Token.Type.Operator, accumulatedString());
                    case "-":
                        next = peek();
                        if (next == '=' || next == '-')
                            accumulate();
                        return new Token(Token.Type.Operator, accumulatedString());
                    case "*":
                    case "/":
                    case "%":
                    case ">":
                    case "<":
                    case "=":
                    case "!":
                        next = peek();
                        if (next == '=')
                            accumulate();
                        return new Token(Token.Type.Operator, accumulatedString());

                    case "|":
                        next = accumulate();
                        if (next != '|')
                            FixmeReportSyntaxError();
                        return new Token(Token.Type.Operator, accumulatedString());

                    case "&":
                        next = accumulate();
                        if (next != '&')
                            FixmeReportSyntaxError();
                        return new Token(Token.Type.Operator, accumulatedString());
                    default:
                        FixmeReportSyntaxError();
                        return null;
                }
            }

            if (accumulatedString().matches("^[a-zA-Z_][a-zA-Z_0-9]*$")) {
                // Identifier.
                while (Utils.isValidIdentifierChar(peek()))
                    accumulate();
                return new Token(Token.Type.Identifier, accumulatedString());
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
                                FixmeReportSyntaxError();
                        }
                    }
                }
                consumeNextChar(); // Consume the '"'
                return new Token(Token.Type.StringLiteral, accumulatedString().substring(1));
            }

            if (accumulatedString().matches("[.0-9]")) {
                String str = accumulatedString();
                if (str.equals(".")) {
                    if (!peek(1).matches("[0-9]"))
                        return new Token(Token.Type.Dot, str);
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
                            } else FixmeReportSyntaxError();
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
                            } else FixmeReportSyntaxError();
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
                            } else FixmeReportSyntaxError();
                            break;
                        }
                    }
                }

                return new Token(Token.Type.NumberLiteral, accumulatedString());
            }

            if (accumulatedString().equals(":"))
                return new Token(Token.Type.Colon, accumulatedString());
            if (accumulatedString().equals(","))
                return new Token(Token.Type.Comma, accumulatedString());
            if (accumulatedString().equals("["))
                return new Token(Token.Type.LeftBracket, accumulatedString());
            if (accumulatedString().equals("]"))
                return new Token(Token.Type.RightBracket, accumulatedString());
            if (accumulatedString().equals("("))
                return new Token(Token.Type.LeftParen, accumulatedString());
            if (accumulatedString().equals(")"))
                return new Token(Token.Type.RightParen, accumulatedString());
            if (accumulatedString().equals("{"))
                return new Token(Token.Type.LeftCurly, accumulatedString());
            if (accumulatedString().equals("}"))
                return new Token(Token.Type.RightCurly, accumulatedString());
            if (accumulatedString().equals("."))
                return new Token(Token.Type.Dot, accumulatedString());
            if (accumulatedString().equals(";"))
                return new Token(Token.Type.SemiColon, accumulatedString());
        }
    }

    public void dump() {
        while (!eof())
            consumeNextToken().dump();
    }
}
