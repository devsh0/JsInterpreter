package parser;

import ast.Block;
import ast.FunctionDeclaration;
import ast.Identifier;
import lexer.Token;
import lexer.TokenStream;

import java.util.ArrayList;
import java.util.List;

import static myutils.Macro.todo_report_syntax_error;

public class FunctionDeclarationParser extends Parser {
    public FunctionDeclarationParser(TokenStream stream, ScopeManager scopeManager) {
        super(stream, scopeManager);
    }

    private Identifier parseFunctionIdentifier() {
        var idToken = stream().consumeIdentifier();
        return Identifier.from(idToken.getValue());
    }

    private Identifier[] parseParameterList() {
        List<Identifier> parameterList = new ArrayList<>();
        stream().consumeAndMatch("(");
        var nextTokenType = stream().peekNextToken().getType();
        while (nextTokenType != Token.Type.RightParenT) {
            var nextParameter = stream().consumeIdentifier().getValue();
            parameterList.add(Identifier.from(nextParameter));
            nextTokenType = stream().peekNextToken().getType();
            if (nextTokenType == Token.Type.CommaT) {
                stream().consumeAndMatch(",");
                nextTokenType = stream().peekNextToken().getType();
                continue;
            }
            if (nextTokenType != Token.Type.RightParenT)
                todo_report_syntax_error();
        }
        stream().consumeAndMatch(")");
        var parameters = new Identifier[parameterList.size()];
        for (int i = 0; i < parameterList.size(); i++)
            parameters[i] = parameterList.get(i);
        return parameters;
    }

    private Block parseFunctionBody(FunctionDeclaration function) {
        return new BlockParser(stream(), scopeManager(), function).parse();
    }

    @Override
    public FunctionDeclaration parse() {
        stream().consumeNextToken(); // "function"
        var function = new FunctionDeclaration();
        function.setIdentifier(parseFunctionIdentifier());
        function.setParameters(parseParameterList());

        stream().consumeAndMatch("{");
        scopeManager().pushFunctionScope(function);
        function.setBody(parseFunctionBody(function));
        scopeManager().popFunction();
        stream().consumeAndMatch("}");

        return function;
    }
}
