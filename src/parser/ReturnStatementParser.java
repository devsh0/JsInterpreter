package parser;

import ast.ReturnStatement;
import lexer.Token;

public class ReturnStatementParser extends Parser {

    @Override
    public ReturnStatement parse() {
        stream().consumeNextToken(); // "return"
        var target = scopeManager().getActiveFunctionScope();
        var statement = new ReturnStatement(target);
        if (stream().peekNextToken().getType() != Token.Type.SemiColonT) {
            var expression = new ExpressionParser().parse();
            statement.setExpression(expression);
        }
        stream().consumeSemiColon();
        return statement;
    }
}
