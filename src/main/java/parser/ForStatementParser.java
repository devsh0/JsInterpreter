package parser;

import ast.*;
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

    private Block parseBody(ForStatement forStatement) {
        var hasCurlyEnclosedBlock = stream().peekNextToken().getValue().equals("{");
        if (hasCurlyEnclosedBlock)
            stream().consumeAndMatch("{");
        scopeManager().pushLoop(forStatement);
        var forBody = new BlockParser(stream(), scopeManager(), forStatement, hasCurlyEnclosedBlock).parse();
        scopeManager().popLoop();
        if (hasCurlyEnclosedBlock)
            stream().consumeAndMatch("}");
        return forBody;
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
        forStatement.setBody(parseBody(forStatement));
        return forStatement;
    }
}
