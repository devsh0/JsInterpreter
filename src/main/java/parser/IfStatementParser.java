package parser;

import ast.Block;
import ast.Expression;
import ast.IfStatement;
import ast.Statement;
import lexer.TokenStream;

import java.util.Optional;

import static myutils.Macro.todo_report_syntax_error;

public class IfStatementParser extends Parser {
    public IfStatementParser(TokenStream stream, ScopeManager scopeManager) {
        super(stream, scopeManager);
    }

    private Expression parseConditionExpression() {
        stream().consumeAndMatch("(");
        var expression = new ExpressionParser(stream(), scopeManager()).parse();
        stream().consumeAndMatch(")");
        return expression;
    }

    private Block parseBody(IfStatement ifStatement) {
        boolean hasCurlyEnclosedBlock = stream().peekNextToken().getValue().equals("{");
        if (hasCurlyEnclosedBlock)
            stream().consumeAndMatch("{");
        Block block = new BlockParser(stream(), scopeManager(), ifStatement, hasCurlyEnclosedBlock).parse();
        if (hasCurlyEnclosedBlock) {
            stream().consumeAndMatch("}");
        }
        return block;
    }

    private Optional<Statement> maybeParseAlternate(IfStatement ifStatement) {
        if (stream().peekNextToken().getValue().equals("else")) {
            stream().consumeNextToken();
            var hasIfStatementNext = stream().peekNextToken().getValue().equals("if");
            return Optional.of(hasIfStatementNext ? parse() : parseBody(ifStatement));
        }
        return Optional.empty();
    }


    @Override
    public IfStatement parse() {
        var ifStatement = new IfStatement();
        if (!stream().consumeNextToken().getValue().equals("if"))
            todo_report_syntax_error();
        ifStatement.setConditionExpression(parseConditionExpression());
        ifStatement.setBody(parseBody(ifStatement));
        var maybeAlternate = maybeParseAlternate(ifStatement);
        ifStatement.setAlternate(maybeAlternate.orElse(null));
        return ifStatement;
    }
}
