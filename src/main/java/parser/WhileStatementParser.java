package parser;

import ast.Block;
import ast.Identifier;
import ast.WhileStatement;
import lexer.TokenStream;

public class WhileStatementParser extends Parser {
    private Identifier label;

    public WhileStatementParser(TokenStream stream, ScopeManager scopeManager, Identifier label) {
        super(stream, scopeManager);
        this.label = label;
    }

    private Block parseBody(WhileStatement whileStatement) {
        var hasBlock = stream().peekNextToken().getValue().equals("{");
        if (hasBlock)
            stream().consumeAndMatch("{");
        scopeManager().pushLoopScope(whileStatement);
        var whileBody = new BlockParser(stream(), scopeManager(), whileStatement, hasBlock).parse();
        scopeManager().popLoopScope();
        if (hasBlock)
            stream().consumeAndMatch("}");
        return whileBody;
    }

    @Override
    public WhileStatement parse() {
        var whileStatement = new WhileStatement();
        whileStatement.setLabel(label);
        stream().consumeNextToken(); // "while"
        stream().consumeAndMatch("(");
        whileStatement.setConditionExpression(new ExpressionParser(stream(), scopeManager()).parse());
        stream().consumeAndMatch(")");
        whileStatement.setBody(parseBody(whileStatement));
        return whileStatement;
    }
}
