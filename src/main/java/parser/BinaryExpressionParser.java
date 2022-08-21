package parser;

import ast.ASTNode;
import ast.BinaryExpression;
import ast.Expression;
import ast.Identifier;
import ast.operator.*;
import ast.value.JSBoolean;
import ast.value.JSNumber;
import ast.value.JSString;
import lexer.Token;
import lexer.TokenStream;

/*
 * Precedence aware BNF :
 * ---------------------------------------------------------------
 * BinaryExpression         ::=     AssignmentExpression | AssignmentExpression BinaryOp BinaryExpression
 * AssignmentExpression     ::=     Identifier AssignmentOp LogicalExpression | LogicalExpression
 * LogicalExpression        ::=     RelationalExpression LogicalOp RelationalExpression | RelationalExpression
 * RelationalExpression     ::=     TermExpression RelationalOp TermExpression | TermExpression
 * TermExpression           ::=     FactorExpression TermOp FactorExpression | FactorExpression
 * FactorExpression         ::=     GroupedExpression FactorOp GroupedExpression | GroupedExpression
 * GroupedExpression        ::=     '(' BinaryExpression ')' | PrimaryExpression
 * PrimaryExpression        ::=     Identifier | Literal
 * Literal                  ::=     Number | String | Boolean
 * AssignmentOp             ::=     '=' | '+=' | '-=' | '*=' '/=' | '%='
 * LogicalOp                ::=     '&&' | '||'
 * RelationalOp             ::=     '<' | '>' | '<=' | '>=' | '==' | '!='
 * TermOp                   ::=     '+' | '-'
 * FactorOp                 ::=     '*' | '/' | '%'
 * BinaryOp                 ::=     AssignmentOp | LogicalOp | RelationalOp | TermOp | FactorOp
 * ---------------------------------------------------------------
 */
public class BinaryExpressionParser extends Parser {
    public BinaryExpressionParser(TokenStream stream, ScopeManager scopeManager) {
        super(stream, scopeManager);
    }

    private Token parseOperator() {
        return stream().consumeNextToken();
    }

    private Identifier parseIdentifier() {
        var identifier = stream().consumeNextToken();
        VERIFY(identifier.getType() == Token.Type.IdentifierT);
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
        ASSERT_NOT_REACHED();
        return null;
    }

    private Expression parsePrimaryExpression() {
        var tokens = stream().peekTokens(2);
        if (tokens.get(0).getType() == Token.Type.IdentifierT) {
            // PrimaryExpression => Identifier.
            FIXME_UNIMPLEMENTED();
        }
        // PrimaryExpression => Literal.
        return parseLiteral();
    }

    private Expression parseGroupedExpression() {
        var nextToken = stream().peekNextToken();
        if (nextToken.getType() == Token.Type.LeftParenT) {
            // GroupedExpression => '(' BinaryExpression ')'.
            FIXME_UNIMPLEMENTED();
            return null;
        }
        // GroupedExpression => PrimaryExpression.
        return parsePrimaryExpression();
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
    public ASTNode parse() {
        var assignmentExpression = parseAssignmentExpression();
        var nextToken = stream().peekNextToken();
        if (BinaryOperator.isBinaryOperator(nextToken)) {
            // BinaryExpression => AssignmentExpression BinaryOp BinaryExpression.
            var opToken = stream().consumeNextToken();
            var rhs = (Expression) parse();
            var operator = BinaryOperator.construct(opToken, assignmentExpression, rhs);
            return new BinaryExpression().setOperator(operator);
        }
        // BinaryExpression => AssignmentExpression.
        return assignmentExpression;
    }
}
