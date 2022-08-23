package parser;

import ast.ASTNode;
import ast.CallExpression;
import ast.Expression;
import ast.Identifier;
import lexer.Token;
import lexer.TokenStream;

import java.util.ArrayList;
import java.util.List;

import static myutils.Macro.todo_report_syntax_error;
import static myutils.Macro.verify;

public class CallExpressionParser extends Parser {

    public CallExpressionParser(TokenStream stream, ScopeManager scopeManager) {
        super(stream, scopeManager);
    }

    private List<Expression> parseArgumentList() {
        var arguments = new ArrayList<Expression>();
        while (stream().peekNextToken().getType() != Token.Type.RightParenT) {
            var argument = new ExpressionParser(stream(), scopeManager()).parse();
            arguments.add(argument);
            if (stream().peekNextToken().getType() == Token.Type.CommaT) {
                stream().consumeNextToken();
                if(stream().peekNextToken().getType() == Token.Type.RightParenT)
                    todo_report_syntax_error();
            }
        }
        return arguments;
    }

    @Override
    public Expression parse() {
        var token = stream().consumeNextToken();
        verify(token.getType() == Token.Type.IdentifierT);
        var functionIdentifier = Identifier.from(token.getValue());
        var callExpression = new CallExpression().setIdentifier(functionIdentifier);
        stream().consumeAndMatch("(");
        parseArgumentList().forEach(callExpression::appendArgument);
        stream().consumeAndMatch(")");
        return callExpression;
    }
}
