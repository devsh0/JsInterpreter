package parser;

import ast.Expression;
import ast.IfStatement;
import ast.Statement;
import lexer.TokenStream;

public class IfStatementParser extends Parser {
    public IfStatementParser(TokenStream stream, ScopeManager scopeManager) {
        super(stream, scopeManager);
    }

    private Expression parseConditionExpression() {
        stream().consumeAndMatch("(");
        var expression = new ExpressionParser(stream(), scopeManager()).parse();
        stream().consumeAndMatch(")");
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
            stream().consumeAndMatch("{");
        }

        ifStatement.setBody(new BlockParser(stream(), scopeManager(), ifStatement, hasMultipleStatement).parse());

        if (hasMultipleStatement) {
            stream().consumeAndMatch("}");
        }

        if (stream().peekNextToken().getValue().equals("else")) {
            // We have a chain of if-else.
            stream().consumeNextToken(); // "else"
            if (stream().peekNextToken().getValue().equals("if"))
                return ifStatement.setAlternate(new IfStatementParser(stream(), scopeManager()).parse());

            hasMultipleStatement = stream().peekNextToken().getValue().equals("{");
            if (hasMultipleStatement)
                stream().consumeAndMatch("{");
            ifStatement.setAlternate(new BlockParser(stream(), scopeManager(), ifStatement, hasMultipleStatement).parse());
            if (hasMultipleStatement)
                stream().consumeAndMatch("}");
        }

        return ifStatement;
    }
}
