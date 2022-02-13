package parser;

import ast.BreakStatement;
import ast.Identifier;
import lexer.Token;

public class BreakStatementParser extends Parser {
    @Override
    public BreakStatement parse() {
        stream().consumeNextToken(); // "break"

        Identifier label = null;
        if (stream().peekNextToken().getType() == Token.Type.IdentifierT)
            label = Identifier.from(stream().consumeIdentifier().getValue());

        var targetLoop = scopeManager().getLoopScope(label);

        stream().consumeSemiColon();
        return new BreakStatement(targetLoop);
    }
}
