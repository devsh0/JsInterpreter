package parser;

import ast.BinaryExpression;
import ast.CallExpression;
import ast.Expression;
import ast.Identifier;
import ast.operator.*;
import ast.value.JSValue;
import lexer.Token;
import lexer.TokenStream;
import myutils.Assertable;

import java.util.Stack;

public class ExpressionParser extends Parser {
    public ExpressionParser(TokenStream stream, ScopeManager scopeManager) {
        super(stream, scopeManager);
    }

    @Override
    public Expression parse() {
        return (Expression)new BinaryExpressionParser(stream(), scopeManager()).parse();
    }
}
