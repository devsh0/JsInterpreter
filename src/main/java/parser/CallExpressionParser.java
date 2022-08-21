package parser;

import ast.ASTNode;
import ast.CallExpression;
import ast.Expression;
import ast.Identifier;
import lexer.Token;
import lexer.TokenStream;

import java.util.ArrayList;
import java.util.List;

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
                    FIXME_REPORT_SYNTAX_ERROR();
            }
        }
        return arguments;
    }

    @Override
    public ASTNode parse() {
        var token = stream().consumeNextToken();
        VERIFY(token.getType() == Token.Type.IdentifierT);
        var functionIdentifier = Identifier.from(token.getValue());
        var callExpression = new CallExpression().setIdentifier(functionIdentifier);
        stream().consumeAndMatch("(");
        parseArgumentList().forEach(callExpression::appendArgument);
        stream().consumeAndMatch(")");
        return callExpression;
    }
}
