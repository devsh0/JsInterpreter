package parser;

import ast.Expression;
import ast.ForStatement;
import ast.Identifier;
import ast.Statement;
import lexer.Token;
import lexer.TokenStream;

public class ForStatementParser extends Parser {
    private Identifier label;

    public ForStatementParser(TokenStream stream, ScopeManager scopeManager, Identifier label) {
        super(stream, scopeManager);
        this.label = label;
    }

    private Statement parseInitializer() {
        if (stream().peekNextToken().getValue().equals("let"))
            return new VariableDeclarationParser(stream(), scopeManager()).parse();
        return new ExpressionStatementParser(stream(), scopeManager()).parse();
    }

    private Expression parseConditionExpression() {
        // Note that expression statements with missing expressions are still valid.
        var expressionStatement = new ExpressionStatementParser(stream(), scopeManager()).parse();
        return expressionStatement.getSource();
    }

    private Expression parseUpdateExpression() {
        if (stream().peekNextToken().getType() == Token.Type.RightParenT)
            return null;
        return new ExpressionParser(stream(), scopeManager()).parse();
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
        var forBody = new BlockParser(stream(), scopeManager(), forStatement, hasMultipleStatement).parse();
        scopeManager().popLoopScope();

        if (hasMultipleStatement)
            stream().consumeAndMatch("}");

        forStatement.setBody(forBody);
        return forStatement;
    }
}
