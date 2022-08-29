package parser;

import ast.Identifier;
import ast.VariableDeclaration;
import lexer.TokenStream;

import static myutils.Macro.verify;

public class VariableDeclarationParser extends Parser {
    public VariableDeclarationParser(TokenStream stream, ScopeManager scopeManager) {
        super(stream, scopeManager);
    }

    @Override
    public VariableDeclaration parse() {
        verify(stream().consumeNextToken().getValue().equals("let"));
        var declaration = new VariableDeclaration();
        var tokens = stream().peekTokens(2);
        if (tokens.get(1).getValue().equals("=")) {
            var assignmentExpression = new ExpressionParser(stream(), scopeManager()).parse();
            declaration.setInitializer(assignmentExpression);
        } else {
            // Consume the name, there is no initializer.
            stream().consumeNextToken();
        }
        var idStr = tokens.get(0).getValue();
        var identifier = Identifier.from(idStr);
        declaration.setIdentifier(identifier);
        stream().consumeAndMatch(";");
        return declaration;
    }
}
