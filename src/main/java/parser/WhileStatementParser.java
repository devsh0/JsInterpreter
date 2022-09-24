package parser;

import ast.Block;
import ast.ExpressionStatement;
import ast.Identifier;
import ast.WhileStatement;
import lexer.TokenStream;

public class WhileStatementParser extends Parser {
    private Identifier label;

    public WhileStatementParser(TokenStream stream, ScopeManager scopeManager, Identifier label) {
        super(stream, scopeManager);
        this.label = label;
    }

    private ExpressionStatement parseConditionStatement() {
        var expression = new ExpressionParser(stream(), scopeManager()).parse();
        return new ExpressionStatement().setSource(expression);
    }

    private Block parseBody(WhileStatement whileStatement) {
        var hasCurlyEnclosedBlock = stream().peekNextToken().getValue().equals("{");
        if (hasCurlyEnclosedBlock)
            stream().consumeAndMatch("{");
        scopeManager().pushLoop(whileStatement);
        var whileBody = new BlockParser(stream(), scopeManager(), whileStatement, hasCurlyEnclosedBlock).parse();
        scopeManager().popLoop();
        if (hasCurlyEnclosedBlock)
            stream().consumeAndMatch("}");
        return whileBody;
    }

    @Override
    public WhileStatement parse() {
        var whileStatement = new WhileStatement();
        whileStatement.setLabel(label);
        stream().consumeNextToken(); // "while"
        stream().consumeAndMatch("(");
        whileStatement.setConditionStatement(parseConditionStatement());
        stream().consumeAndMatch(")");
        whileStatement.setBody(parseBody(whileStatement));
        return whileStatement;
    }
}
