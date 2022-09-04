package parser;

import ast.ContinueStatement;
import ast.Identifier;
import lexer.Token;
import lexer.TokenStream;

public class ContinueStatementParser extends Parser {
    public ContinueStatementParser(TokenStream stream, ScopeManager scopeManager) {
        super(stream, scopeManager);
    }

    @Override
    public ContinueStatement parse() {
        stream().consumeNextToken(); // "continue"
        Identifier label = null;
        if (stream().peekNextToken().getType() == Token.Type.IdentifierT)
            label = Identifier.from(stream().consumeIdentifier().getValue());

        var targetLoop = scopeManager().getLoopWithLabel(label);
        stream().consumeAndMatch(";");
        return new ContinueStatement(targetLoop);
    }
}
