package parser;

import ast.Expression;
import ast.ForStatement;
import ast.Identifier;
import ast.Statement;
import lexer.Token;

public class ForStatementParser extends Parser {
    private Identifier label;

    public ForStatementParser(Identifier label) {
        this.label = label;
    }

    private Statement parseInitializer() {
        if (stream().peekNextToken().getValue().equals("let"))
            return new VariableDeclarationParser().parse();
        return new ExpressionStatementParser().parse();
    }

    private Expression parseConditionExpression() {
        // Note that expression statements with missing expressions are still valid.
        var expressionStatement = new ExpressionStatementParser().parse();
        return expressionStatement.getSource();
    }

    private Expression parseUpdateExpression() {
        if (stream().peekNextToken().getType() == Token.Type.RightParenT)
            return null;
        return new ExpressionParser().parse();
    }

    @Override
    public ForStatement parse() {
        var forStatement = new ForStatement();
        forStatement.setLabel(label);

        stream().consumeNextToken(); // "for"

        stream().consumeAndMatch("(");
        forStatement.setInitializer(parseInitializer());
        forStatement.setConditionExpression(parseConditionExpression());
        forStatement.setUpdateExpression(parseUpdateExpression());
        stream().consumeAndMatch(")");

        var hasMultipleStatement = stream().peekNextToken().getValue().equals("{");

        if (hasMultipleStatement)
            stream().consumeAndMatch("{");

        scopeManager().pushLoopScope(forStatement);
        var forBody = new BlockParser(forStatement, hasMultipleStatement).parse();
        scopeManager().popLoopScope();

        if (hasMultipleStatement)
            stream().consumeAndMatch("}");

        forStatement.setBody(forBody);
        return forStatement;
    }
}
