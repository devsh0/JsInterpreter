package parser;

import ast.ExpressionStatement;

public class ExpressionStatementParser extends Parser {
    @Override
    public ExpressionStatement parse() {
        var statement = new ExpressionStatement();
        if (!stream().peekNextToken().getValue().equals(";"))
            statement.setSource(new ExpressionParser().parse());
        stream().consumeSemiColon();
        return statement;
    }
}
