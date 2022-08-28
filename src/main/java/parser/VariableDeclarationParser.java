package parser;

import ast.BinaryExpression;
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
        var nextTokens = stream().peekTokens(2);
        if (nextTokens.get(1).getValue().equals("=")) {
            var assignmentExpression = new ExpressionParser(stream(), scopeManager()).parse();
            declaration.setInitializer((BinaryExpression) assignmentExpression);
        }
        var idStr = nextTokens.get(0).getValue();
        declaration.setIdentifier(Identifier.from(idStr));
        if (stream().peekNextToken().getValue().equals("=")) {
            stream().consumeAndMatch("=");
            declaration.setInitializer(new ExpressionParser(stream(), scopeManager()).parse());
        }
        stream().consumeAndMatch(";");
        return declaration;
    }
}
