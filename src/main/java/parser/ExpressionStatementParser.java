package parser;

import ast.ExpressionStatement;
import lexer.TokenStream;

public class ExpressionStatementParser extends Parser {
    public ExpressionStatementParser(TokenStream stream, ScopeManager scopeManager) {
        super(stream, scopeManager);
    }

    @Override
    public ExpressionStatement parse() {
        var statement = new ExpressionStatement();
        // Expressions are allowed to be empty.
        if (!stream().peekNextToken().getValue().equals(";"))
            statement.setSource(new ExpressionParser(stream(), scopeManager()).parse());
        stream().consumeAndMatch(";");
        return statement;
    }
}
