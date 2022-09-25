package parser;

import ast.BreakStatement;
import ast.Identifier;
import lexer.Token;
import lexer.TokenStream;

public class BreakStatementParser extends Parser {
    public BreakStatementParser(TokenStream stream, ScopeManager scopeManager) {
        super(stream, scopeManager);
    }

    @Override
    public BreakStatement parse() {
        stream().consumeNextToken(); // "break"

        Identifier label = null;
        if (stream().peekNextToken().getType() == Token.Type.IdentifierT)
            label = Identifier.from(stream().consumeIdentifier().getValue());

        var targetLoop = scopeManager().getLoopWithLabel(label);

        stream().consumeAndMatch(";");
        return new BreakStatement(targetLoop).setLabel(label);
    }
}
