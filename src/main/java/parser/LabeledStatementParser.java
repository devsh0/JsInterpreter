package parser;

import ast.Identifier;
import ast.WhileStatement;
import lexer.TokenStream;

public class LabeledStatementParser extends Parser {
    public LabeledStatementParser(TokenStream stream, ScopeManager scopeManager) {
        super(stream, scopeManager);
    }

    @Override
    public WhileStatement parse() {
        var label = Identifier.from(stream().consumeIdentifier().getValue());
        stream().consumeAndMatch(":");
        // FIXME: This can also be a switch or a for loop.
        return new WhileStatementParser(stream(), scopeManager(), label).parse();
    }
}
