package parser;

import ast.Expression;
import lexer.TokenStream;

public class ExpressionParser extends Parser {
    public ExpressionParser(TokenStream stream, ScopeManager scopeManager) {
        super(stream, scopeManager);
    }

    @Override
    public Expression parse() {
        return (Expression)new BinaryExpressionParser(stream(), scopeManager()).parse();
    }
}
