package parser;

import ast.Identifier;
import ast.Statement;
import lexer.TokenStream;

import static myutils.Macro.verify;

public class LabeledStatementParser extends Parser {
    public LabeledStatementParser(TokenStream stream, ScopeManager scopeManager) {
        super(stream, scopeManager);
    }

    @Override
    public Statement parse() {
        var label = Identifier.from(stream().consumeIdentifier().getValue());
        stream().consumeAndMatch(":");
        var tokenValue = stream().peekNextToken().getValue();
        verify(tokenValue.equals("for") || tokenValue.equals("while"));
        if (tokenValue.equals("for"))
            return new ForStatementParser(stream(), scopeManager(), label).parse();
        return new WhileStatementParser(stream(), scopeManager(), label).parse();
    }
}
