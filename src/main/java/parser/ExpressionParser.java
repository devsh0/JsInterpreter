package parser;

import ast.*;
import ast.operator.*;
import ast.value.JSBoolean;
import ast.value.JSNumber;
import ast.value.JSString;
import lexer.Token;
import lexer.TokenStream;

import static myutils.Macro.*;

/*
 * Expression Grammar :
 * ---------------------------------------------------------------
 * Expression               ::=     AssignmentExpression | AssignmentExpression BinaryOp Expression
 * AssignmentExpression     ::=     Identifier AssignmentOp LogicalExpression | LogicalExpression
 * LogicalExpression        ::=     RelationalExpression LogicalOp RelationalExpression | RelationalExpression
 * RelationalExpression     ::=     TermExpression RelationalOp TermExpression | TermExpression
 * TermExpression           ::=     FactorExpression TermOp FactorExpression | FactorExpression
 * FactorExpression         ::=     GroupedExpression FactorOp GroupedExpression | GroupedExpression
 * GroupedExpression        ::=     '(' Expression ')' | UnaryExpression
 * UnaryExpression          ::=     PrefixedExpression | PostfixedExpression | PrimaryExpression
 * PrefixedExpression       ::=     BooleanExpression | PrefixIncrement | PrefixDecrement
 * BooleanExpression        ::=     '!' UnaryExpression
 * PrefixIncrement          ::=     '++' VariableExpression
 * PrefixDecrement          ::=     '--' VariableExpression
 * PostfixedExpression      ::=     PostfixIncrement | PostfixDecrement
 * PostfixIncrement         ::=     VariableExpression '++'
 * PostfixDecrement         ::=     VariableExpression '--'
 * PrimaryExpression        ::=     IdentifierExpression | Literal
 * IdentifierExpression     ::=     CallExpression | VariableExpression
 * CallExpression           ::=     Identifier '(' ArgumentList ')'
 * ArgumentList             ::=     BinaryExpression [',' ArgumentList] | <empty>
 * VariableExpression       ::=     Identifier
 * Literal                  ::=     Number | String | Boolean
 * AssignmentOp             ::=     '=' | '+=' | '-=' | '*=' '/=' | '%='
 * LogicalOp                ::=     '&&' | '||'
 * RelationalOp             ::=     '<' | '>' | '<=' | '>=' | '==' | '!='
 * TermOp                   ::=     '+' | '-'
 * FactorOp                 ::=     '*' | '/' | '%'
 * BinaryOp                 ::=     AssignmentOp | LogicalOp | RelationalOp | TermOp | FactorOp
 * ---------------------------------------------------------------
 */
public class ExpressionParser extends Parser {
    public ExpressionParser(TokenStream stream, ScopeManager scopeManager) {
        super(stream, scopeManager);
    }

    private Token parseOperator() {
        return stream().consumeNextToken();
    }

    private Identifier parseIdentifier() {
        var identifier = stream().consumeNextToken();
        verify(identifier.getType() == Token.Type.IdentifierT);
        return Identifier.from(identifier.getValue());
    }

    private Expression parseNumberLiteral() {
        var token = stream().consumeNextToken();
        var number = Double.parseDouble(token.getValue());
        return JSNumber.from(number);
    }

    private Expression parseStringLiteral() {
        var token = stream().consumeNextToken();
        var string = token.getValue();
        return JSString.from(string);
    }

    private Expression parseBooleanLiteral() {
        var token = stream().consumeNextToken();
        var bool = Boolean.parseBoolean(token.getValue());
        return JSBoolean.from(bool);
    }

    private Expression parseLiteral() {
        var nextToken = stream().peekNextToken();
        switch (nextToken.getType()) {
            case NumberLiteralT: return parseNumberLiteral();
            case StringLiteralT: return parseStringLiteral();
            case KeywordT: return parseBooleanLiteral();
        }
        unreachable();
        return null;
    }

    private Expression parseVariableExpression() {
        return parseIdentifier();
    }


    private Expression parseIdentifierExpression() {
        var tokens = stream().peekTokens(2);
        if (tokens.get(1).getType() == Token.Type.LeftParenT)
            // IdentifierExpression => CallExpression.
            return new CallExpressionParser(stream(), scopeManager()).parse();
        return parseVariableExpression();
    }

    private Expression parsePrimaryExpression() {
        var nextToken = stream().peekNextToken();
        if (nextToken.getType() == Token.Type.IdentifierT)
            // PrimaryExpression => IdentifierExpression.
            return parseIdentifierExpression();
        // PrimaryExpression => Literal.
        return parseLiteral();
    }

    private Expression parsePrefixIncrementOrDecrement() {
        var opToken = stream().consumeNextToken();
        var variableExpression = parseVariableExpression();
        var operator = UnaryOperator.construct(opToken, variableExpression, true);
        return new UnaryExpression().setOperator(operator);
    }

    private Expression parseBooleanExpression() {
        var opToken = stream().consumeNextToken();
        var unaryExpression = parseUnaryExpression();
        var operator = UnaryOperator.construct(opToken, unaryExpression, true);
        return new UnaryExpression().setOperator(operator);
    }


    private Expression parsePrefixedExpression() {
        var nextToken = stream().peekNextToken();
        if (nextToken.getValue().equals("!"))
            // PrefixedExpression => BooleanExpression.
            return parseBooleanExpression();

        // PrefixedExpression => PrefixIncrement | PrefixDecrement.
        return parsePrefixIncrementOrDecrement();
    }

    private Expression parseUnaryExpression() {
        var tokens = stream().peekTokens(2);
        if (tokens.get(0).getType() == Token.Type.UnaryOperatorT)
            return parsePrefixedExpression();

        if (tokens.get(1).getType() == Token.Type.UnaryOperatorT) {
            // UnaryExpression => PostfixedExpression.
            unimplemented();
            return null;
        }

        return parsePrimaryExpression();
    }

    private Expression parseGroupedExpression() {
        var nextToken = stream().peekNextToken();
        if (nextToken.getType() == Token.Type.LeftParenT) {
            // GroupedExpression => '(' Expression ')'.
            stream().consumeAndMatch("(");
            var binaryExpression = (BinaryExpression) parse();
            stream().consumeAndMatch(")");
            return binaryExpression;
        }
        // GroupedExpression => PrimaryExpression.
        return parseUnaryExpression();
    }

    private Expression parseFactorExpression() {
        var tokens = stream().peekTokens(2);
        if (BinaryOperator.isFactorOperator(tokens.get(1))) {
            // FactorExpression => GroupedExpression FactorOp GroupedExpression.
            var lhs = parseGroupedExpression();
            var opToken = parseOperator();
            var rhs = parseGroupedExpression();
            var operator = BinaryOperator.construct(opToken, lhs, rhs);
            return new BinaryExpression().setOperator(operator);
        }
        // FactorExpression => GroupedExpression.
        return parseGroupedExpression();
    }

    private Expression parseTermExpression() {
        var tokens = stream().peekTokens(2);
        if (BinaryOperator.isTermOperator(tokens.get(1))) {
            // TermExpression => FactorExpression TermOp FactorExpression.
            var lhs = parseFactorExpression();
            var opToken = parseOperator();
            var rhs = parseFactorExpression();
            var operator = BinaryOperator.construct(opToken, lhs, rhs);
            return new BinaryExpression().setOperator(operator);
        }
        // TermExpression => FactorExpression.
        return parseFactorExpression();
    }

    private Expression parseRelationalExpression() {
        var tokens = stream().peekTokens(2);
        if (BinaryOperator.isRelationalOperator(tokens.get(1))) {
            // RelationalExpression => TermExpression RelationalOp TermExpression.
            var lhs = parseTermExpression();
            var opToken = parseOperator();
            var rhs = parseTermExpression();
            var operator = BinaryOperator.construct(opToken, lhs, rhs);
            return new BinaryExpression().setOperator(operator);
        }
        // RelationalExpression => TermExpression.
        return parseTermExpression();
    }

    private Expression parseLogicalExpression() {
        var tokens = stream().peekTokens(2);
        if (BinaryOperator.isLogicalOperator(tokens.get(1))) {
            // LogicalExpression => RelationalExpression LogicalOp RelationalExpression.
            var lhs = parseRelationalExpression();
            var opToken = parseOperator();
            var rhs = parseRelationalExpression();
            var operator = BinaryOperator.construct(opToken, lhs, rhs);
            return new BinaryExpression().setOperator(operator);
        }
        // LogicalExpression => RelationalExpression.
        return parseRelationalExpression();
    }

    private Expression parseAssignmentExpression() {
        var tokens = stream().peekTokens(2);
        if (BinaryOperator.isAssignmentOperator(tokens.get(1))) {
            // AssignmentExpression => Identifier AssignmentOp LogicalExpression.
            var lhs = parseIdentifier();
            var opToken = parseOperator();
            var rhs = parseLogicalExpression();
            var operator = BinaryOperator.construct(opToken, lhs, rhs);
            return new BinaryExpression().setOperator(operator);
        }
        // AssignmentExpression => LogicalExpression.
        return parseLogicalExpression();
    }

    @Override
    public Expression parse() {
        var lhs = parseAssignmentExpression();
        var nextToken = stream().peekNextToken();
        if (BinaryOperator.isBinaryOperator(nextToken)) {
            // BinaryExpression => AssignmentExpression BinaryOp BinaryExpression.
            var opToken = stream().consumeNextToken();
            var precedence = BinaryOperator.getPrecedenceOf(opToken);
            var rhs = (Expression) parse();

            if (rhs instanceof BinaryExpression binaryExpression) {
                var opTokenTemp = new Token(Token.Type.BinaryOperatorT, binaryExpression.getOperator().toString());
                var precedenceTemp = BinaryOperator.getPrecedenceOf(opTokenTemp);
                if (precedence > precedenceTemp) {
                    // We need to decompose the RHS and rearrange our expression.
                    var lhsTemp = binaryExpression.getLHS();
                    var operatorTmp = BinaryOperator.construct(opToken, lhs, lhsTemp);

                    lhs = new BinaryExpression().setOperator(operatorTmp);
                    opToken = opTokenTemp;
                    rhs = binaryExpression.getRHS();
                }
            }

            var operator = BinaryOperator.construct(opToken, lhs, rhs);
            return new BinaryExpression().setOperator(operator);
        }
        // BinaryExpression => AssignmentExpression.
        return lhs;
    }
}
