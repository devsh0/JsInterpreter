package parser;

import ast.Identifier;
import ast.VariableDeclaration;

public class VariableDeclarationParser extends Parser {
    @Override
    public VariableDeclaration parse() {
        VERIFY(stream().consumeNextToken().getValue().equals("let"));
        var declaration = new VariableDeclaration();
        var identifier = Identifier.from(stream().consumeIdentifier().getValue());
        declaration.setIdentifier(identifier);
        if (stream().peekNextToken().getValue().equals("=")) {
            stream().consumeAndMatch("=");
            declaration.setInitializer(new ExpressionParser().parse());
        }
        stream().consumeAndMatch(";");
        return declaration;
    }
}
