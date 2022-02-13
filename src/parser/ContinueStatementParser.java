package parser;

import ast.ContinueStatement;
import ast.Identifier;
import lexer.Token;

public class ContinueStatementParser extends Parser {
    @Override
    public ContinueStatement parse() {
        stream().consumeNextToken(); // "continue"
        Identifier label = null;
        if (stream().peekNextToken().getType() == Token.Type.IdentifierT)
            label = Identifier.from(stream().consumeIdentifier().getValue());

        var targetLoop = scopeManager().getLoopScope(label);
        stream().consumeSemiColon();
        return new ContinueStatement(targetLoop);
    }
}
