package parser;

import ast.Block;
import ast.FunctionDeclaration;
import ast.Identifier;
import lexer.Token;

import java.util.ArrayList;
import java.util.List;

public class FunctionDeclarationParser extends Parser {
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
                FIXME_REPORT_SYNTAX_ERROR();
        }
        stream().consumeAndMatch(")");
        var parameters = new Identifier[parameterList.size()];
        for (int i = 0; i < parameterList.size(); i++)
            parameters[i] = parameterList.get(i);
        return parameters;
    }

    private Block parseFunctionBody(FunctionDeclaration function) {
        return new BlockParser(function).parse();
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
        scopeManager().popFunctionScope();
        stream().consumeAndMatch("}");

        return function;
    }
}
