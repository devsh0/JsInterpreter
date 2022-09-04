package parser;

import ast.ReturnStatement;
import lexer.Token;
import lexer.TokenStream;

public class ReturnStatementParser extends Parser {

    public ReturnStatementParser(TokenStream stream, ScopeManager scopeManager) {
        super(stream, scopeManager);
    }

    @Override
    public ReturnStatement parse() {
        stream().consumeNextToken(); // "return"
        var target = scopeManager().getActiveFunction();
        var statement = new ReturnStatement(target);
        if (stream().peekNextToken().getType() != Token.Type.SemiColonT) {
            var expression = new ExpressionParser(stream(), scopeManager()).parse();
            statement.setExpression(expression);
        }
        stream().consumeAndMatch(";");
        return statement;
    }
}
