package parser;

import ast.Expression;
import ast.IfStatement;
import ast.Statement;

public class IfStatementParser extends Parser {
    private Expression parseConditionExpression() {
        stream().consumeLeftParen();
        var expression = new ExpressionParser().parse();
        stream().consumeRightParen();
        return expression;
    }

    @Override
    public Statement parse() {
        if (!stream().consumeNextToken().getValue().equals("if"))
            FIXME_REPORT_SYNTAX_ERROR();

        var ifStatement = new IfStatement();
        ifStatement.setConditionExpression(parseConditionExpression());

        var hasMultipleStatement = stream().peekNextToken().getValue().equals("{");

        if (hasMultipleStatement) {
            stream().consumeLeftCurly();
        }

        ifStatement.setBody(new BlockParser(ifStatement, hasMultipleStatement).parse());

        if (hasMultipleStatement) {
            stream().consumeRightCurly();
        }

        if (stream().peekNextToken().getValue().equals("else")) {
            // We have a chain of if-else.
            stream().consumeNextToken(); // "else"
            if (stream().peekNextToken().getValue().equals("if"))
                return ifStatement.setAlternate(new IfStatementParser().parse());

            hasMultipleStatement = stream().peekNextToken().getValue().equals("{");
            if (hasMultipleStatement)
                stream().consumeLeftCurly();
            ifStatement.setAlternate(new BlockParser(ifStatement, hasMultipleStatement).parse());
            if (hasMultipleStatement)
                stream().consumeRightCurly();
        }

        return ifStatement;
    }
}
