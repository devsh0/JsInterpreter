package parser;

import ast.BinaryExpression;
import ast.Expression;
import ast.Identifier;
import ast.UnaryExpression;
import ast.operator.BinaryOperator;
import ast.operator.UnaryOperator;
import ast.value.JSBoolean;
import ast.value.JSNumber;
import ast.value.JSObject;
import ast.value.JSString;
import lexer.Token;
import lexer.TokenStream;

import java.util.HashMap;

import static myutils.Macro.unreachable;
import static myutils.Macro.verify;

/*
 * Expression Grammar :
 * ---------------------------------------------------------------
 * Expression               ::=     AssignmentExpression
 * AssignmentExpression     ::=     LogicalExpression [AssignmentExpressionTail]
 * AssignmentExpressionTail ::=     AssignmentOp LogicalExpression [BinaryExpressionTail]
 * LogicalExpression        ::=     RelationalExpression [LogicalExpressionTail]
 * LogicalExpressionTail    ::=     LogicalOp RelationalExpression [BinaryExpressionTail]
 * RelationalExpression     ::=     TermExpression RelationalExpressionTail
 * RelationalExpressionTail ::=     RelationalOp TermExpression [BinaryExpressionTail]
 * TermExpression           ::=     FactorExpression TermExpressionTail
 * TermExpressionTail       ::=     TermOp FactorExpression [BinaryExpressionTail]
 * FactorExpression         ::=     GroupedExpression FactorExpressionTail | GroupedExpression
 * FactorExpressionTail     ::=     FactorOp GroupedExpression [BinaryExpressionTail]
 * BinaryExpressionTail     ::=     AssignmentExpressionTail | LogicalExpressionTail | RelationalExpressionTail | TermExpressionTail | FactoExpressionTail
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

    private Expression parseObjectLiteral() {
        stream().consumeAndMatch("{");
        var map = new HashMap<Identifier, Expression>();
        var nextToken = stream().peekNextToken();
        while (nextToken.getType() != Token.Type.RightCurlyT) {
            var idToken = stream().consumeIdentifier();
            var propId = Identifier.from(idToken.getValue());
            stream().consumeAndMatch(":");
            var propValue = parse();
            map.put(propId, propValue);
            nextToken = stream().peekNextToken();
            if (nextToken.getType() == Token.Type.CommaT) {
                stream().consumeNextToken();
                nextToken = stream().peekNextToken();
            }
        }
        stream().consumeAndMatch("}");
        return JSObject.from(map);
    }

    private Expression parseLiteral() {
        var nextToken = stream().peekNextToken();
        switch (nextToken.getType()) {
            case NumberLiteralT: return parseNumberLiteral();
            case StringLiteralT: return parseStringLiteral();
            case KeywordT: return parseBooleanLiteral();
            case LeftCurlyT: return parseObjectLiteral();
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

    private Expression parsePostfixIncrementOrDecrement() {
        var variableExpression = parseVariableExpression();
        var opToken = stream().consumeNextToken();
        var operator = UnaryOperator.construct(opToken, variableExpression, false);
        return new UnaryExpression().setOperator(operator);
    }

    private Expression parsePostfixedExpression() {
        var nextToken = stream().peekNextToken();
        if (nextToken.getValue().equals("!"))
            // PostfixedExpression => BooleanExpression.
            return parseBooleanExpression();

        return parsePostfixIncrementOrDecrement();
    }

    private Expression parseUnaryExpression() {
        var tokens = stream().peekTokens(2);
        if (tokens.get(0).getType() == Token.Type.UnaryOperatorT)
            return parsePrefixedExpression();

        if (tokens.get(1).getType() == Token.Type.UnaryOperatorT) {
            // UnaryExpression => PostfixedExpression.
            return parsePostfixedExpression();
        }

        return parsePrimaryExpression();
    }

    private Expression parseGroupedExpression() {
        var nextToken = stream().peekNextToken();
        if (nextToken.getType() == Token.Type.LeftParenT) {
            // GroupedExpression => '(' Expression ')'.
            stream().consumeAndMatch("(");
            var expression = parse();
            stream().consumeAndMatch(")");
            return expression;
        }
        // GroupedExpression => PrimaryExpression.
        return parseUnaryExpression();
    }

    private Expression parseFactorExpressionTail(Expression lhs) {
        var opToken = parseOperator();
        var rhs = parseGroupedExpression();
        var operator = BinaryOperator.construct(opToken, lhs, rhs);
        var expression = new BinaryExpression().setOperator(operator);
        return maybeParseBinaryExpressionTail(expression);
    }

    private Expression parseFactorExpression() {
        var expression = parseGroupedExpression();
        var nextToken = stream().peekNextToken();
        if (BinaryOperator.isFactorOperator(nextToken))
            expression = parseFactorExpressionTail(expression);
        return expression;
    }

    private Expression parseTermExpressionTail(Expression lhs) {
        var opToken = parseOperator();
        var rhs = parseFactorExpression();
        var operator = BinaryOperator.construct(opToken, lhs, rhs);
        var expression = new BinaryExpression().setOperator(operator);
        return maybeParseBinaryExpressionTail(expression);
    }

    private Expression parseTermExpression() {
        var expression = parseFactorExpression();
        var nextToken = stream().peekNextToken();
        if (BinaryOperator.isTermOperator(nextToken))
            expression = parseTermExpressionTail(expression);
        return expression;
    }

    private Expression parseRelationalExpressionTail(Expression lhs) {
        var opToken = parseOperator();
        var rhs = parseTermExpression();
        var operator = BinaryOperator.construct(opToken, lhs, rhs);
        var expression = new BinaryExpression().setOperator(operator);
        return maybeParseBinaryExpressionTail(expression);
    }

    private Expression parseRelationalExpression() {
        var expression = parseTermExpression();
        var nextToken = stream().peekNextToken();
        if (BinaryOperator.isRelationalOperator(nextToken))
            expression = parseRelationalExpressionTail(expression);
        return expression;
    }

    private Expression parseLogicalExpressionTail(Expression lhs) {
        var opToken = parseOperator();
        var rhs = parseRelationalExpression();
        var operator = BinaryOperator.construct(opToken, lhs, rhs);
        var expression = new BinaryExpression().setOperator(operator);
        return maybeParseBinaryExpressionTail(expression);
    }

    private Expression parseLogicalExpression() {
        var expression = parseRelationalExpression();
        var nextToken = stream().peekNextToken();
        if (BinaryOperator.isLogicalOperator(nextToken))
            expression = parseLogicalExpressionTail(expression);
        return expression;
    }

    private Expression parseAssignmentExpressionTail(Expression lhs) {
        var opToken = parseOperator();
        var rhs = parseLogicalExpression();
        var operator = BinaryOperator.construct(opToken, lhs, rhs);
        var expression = new BinaryExpression().setOperator(operator);
        return maybeParseBinaryExpressionTail(expression);
    }

    private Expression parseAssignmentExpression() {
        var expression = parseLogicalExpression();
        var nextToken = stream().peekNextToken();
        if (BinaryOperator.isAssignmentOperator(nextToken))
            expression = parseAssignmentExpressionTail(expression);
        return expression;
    }

    public Expression maybeParseBinaryExpressionTail(Expression lhs) {
        var nextToken = stream().peekNextToken();
        var binaryExpression = lhs;
        if (BinaryOperator.isAssignmentOperator(nextToken))
            binaryExpression = parseAssignmentExpressionTail(lhs);
        else if (BinaryOperator.isLogicalOperator(nextToken))
            binaryExpression = parseLogicalExpressionTail(lhs);
        else if (BinaryOperator.isRelationalOperator(nextToken))
            binaryExpression = parseRelationalExpressionTail(lhs);
        else if (BinaryOperator.isTermOperator(nextToken))
            binaryExpression = parseTermExpressionTail(lhs);
        else if (BinaryOperator.isFactorOperator(nextToken))
            binaryExpression = parseFactorExpressionTail(lhs);
        return binaryExpression;
    }

    @Override
    public Expression parse() {
        return parseAssignmentExpression();
    }
}
