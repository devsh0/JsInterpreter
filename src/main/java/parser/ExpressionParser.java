package parser;

import ast.BinaryExpression;
import ast.CallExpression;
import ast.Expression;
import ast.Identifier;
import ast.operator.*;
import ast.value.JSValue;
import lexer.Token;
import myutils.Assertable;

import java.util.Stack;

public class ExpressionParser extends Parser {
    private final Stack<Expression> subexpressions = new Stack<>();

    private enum SubexpressionBeginMarker {
        Invalid,
        BooleanLiteral,
        FunctionCall,
        NumberLiteral,
        StringLiteral,
        GroupedExpression,
        UnaryOperatedVariableRef,
        VariableRef
    }

    // This includes variable refs, (++,--,!)variableRef, '(', call expressions, and literals.
    private boolean isValidStartOfSubexpression(Token token) {
        return tokenToSubexpressionBeginMarker(token) != SubexpressionBeginMarker.Invalid;
    }

    private boolean isOperator(Token token) {
        var type = token.getType();
        return type == Token.Type.UnaryOperatorT || type == Token.Type.BinaryOperatorT || type == Token.Type.UnaryOrBinaryOperatorT;
    }

    private boolean isExpressionEntity(Token token) {
        return isValidStartOfSubexpression(token) || isOperator(token);
    }

    private SubexpressionBeginMarker tokenToSubexpressionBeginMarker(Token token) {
        var tokenType = token.getType();
        var tokenValue = token.getValue();

        if (tokenType == Token.Type.KeywordT) {
            if (tokenValue.equals("true") || tokenValue.equals("false"))
                return SubexpressionBeginMarker.BooleanLiteral;
            Assertable._FIXME_REPORT_SYNTAX_ERROR();
        }

        if (tokenType == Token.Type.LeftParenT)
            return SubexpressionBeginMarker.GroupedExpression;

        if (tokenType == Token.Type.UnaryOperatorT)
            return SubexpressionBeginMarker.UnaryOperatedVariableRef;

        if (tokenType == Token.Type.IdentifierT) {
            var isFunctionCall = stream().peekNextToken().getType() == Token.Type.LeftParenT;
            return isFunctionCall ? SubexpressionBeginMarker.FunctionCall : SubexpressionBeginMarker.VariableRef;
        }

        if (tokenType == Token.Type.StringLiteralT)
            return SubexpressionBeginMarker.StringLiteral;

        if (tokenType == Token.Type.NumberLiteralT)
            return SubexpressionBeginMarker.NumberLiteral;

        return SubexpressionBeginMarker.Invalid;
    }

    private void appendArgumentHelper(CallExpression callExpression) {
        var argumentExpression = new ExpressionParser().parse();
        callExpression.addArgument(argumentExpression);
        if (stream().peekNextToken().getType() == Token.Type.CommaT) {
            stream().consumeAndMatch(",");
            appendArgumentHelper(callExpression);
        }
    }

    private CallExpression parseCallExpression(String functionName) {
        var callExpression = new CallExpression().setCallee(Identifier.from(functionName));
        stream().consumeAndMatch("(");
        var nextToken = stream().peekNextToken();
        while (nextToken.getType() != Token.Type.RightParenT) {
            appendArgumentHelper(callExpression);
            nextToken = stream().peekNextToken();
        }
        stream().consumeAndMatch(")");
        return callExpression;
    }

    private Expression parseGroupedExpression() {
        var expression = new ExpressionParser().parse();
        stream().consumeAndMatch(")");
        return expression;
    }

    private Expression buildSubexpression(Token token) {
        switch (tokenToSubexpressionBeginMarker(token)) {
            case VariableRef:
                return Identifier.from(token.getValue());
            case FunctionCall:
                return parseCallExpression(token.getValue());
            case GroupedExpression:
                return parseGroupedExpression();
            case StringLiteral:
                return JSValue.from(token.getValue());
            case NumberLiteral:
                return JSValue.from(Double.parseDouble(token.getValue()));
            case BooleanLiteral:
                return JSValue.from(token.getValue().equals("true"));
            case UnaryOperatedVariableRef:
                FIXME_UNIMPLEMENTED();
            case Invalid:
                FIXME_REPORT_SYNTAX_ERROR();
        }
        ASSERT_NOT_REACHED();
        return null;
    }

    private void pushOperator() {
        var operatorStr = stream().consumeNextToken().getValue();
        BinaryOperator operator = null;
        switch (operatorStr) {
            case "+":
                // FIXME: This may just be a unary operator.
                operator = BinaryOperator.Plus;
                break;
            case "-":
                // FIXME: This may just be a unary operator.
                operator = BinaryOperator.Minus;
                break;
            case "*":
                operator = BinaryOperator.Multiply;
                break;
            case "/":
                operator = BinaryOperator.Divide;
                break;
            case "%":
                operator = BinaryOperator.Mod;
                break;
            case "=":
                operator = BinaryOperator.Assignment;
                break;
            case ">":
                operator = RelationalOperator.GreaterThan;
                break;
            case "<":
                operator = RelationalOperator.LessThan;
                break;
            case "<=":
                operator = RelationalOperator.LessThanOrEqual;
                break;
            case ">=":
                operator = RelationalOperator.GreaterThanOrEqual;
                break;
            case "==":
                operator = RelationalOperator.Equals;
                break;
            case "+=":
                operator = BinaryOperator.PlusEqual;
                break;
            case "-=":
                operator = BinaryOperator.MinusEqual;
                break;
            case "*=":
                operator = BinaryOperator.MultiplyEqual;
                break;
            case "/=":
                operator = BinaryOperator.DivideEqual;
                break;
            case "%=":
                operator = BinaryOperator.ModEqual;
                break;
            case "!=":
                operator = RelationalOperator.NotEquals;
                break;
            case "&&":
                operator = LogicalOperator.And;
                break;
            case "||":
                operator = LogicalOperator.Or;
                break;
            case "++":
            case "--":
            case "!":
                FIXME_UNIMPLEMENTED();
                break;
            default:
                FIXME_REPORT_SYNTAX_ERROR();
        }

        if (subexpressions.empty())
            FIXME_REPORT_SYNTAX_ERROR();
        var lhs = subexpressions.pop();
        var rhs = buildSubexpression(stream().consumeNextToken());
        var binaryExpression = new BinaryExpression();
        subexpressions.push(binaryExpression.setOperator(operator).setLhs(lhs).setRhs(rhs));
    }

    private void pushSubexpression() {
        var token = stream().consumeNextToken();
        subexpressions.push(buildSubexpression(token));
    }

    @Override
    public Expression parse() {
        var token = stream().peekNextToken();
        if (!isValidStartOfSubexpression(token))
            FIXME_REPORT_SYNTAX_ERROR();
        while (isExpressionEntity(token)) {
            if (isOperator(token))
                pushOperator();
            else pushSubexpression();
            token = stream().peekNextToken();
        }

        if (subexpressions.size() != 1)
            FIXME_REPORT_SYNTAX_ERROR();

        return subexpressions.pop();
    }
}
