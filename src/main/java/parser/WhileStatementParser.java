package parser;

import ast.Identifier;
import ast.WhileStatement;

public class WhileStatementParser extends Parser {
    private Identifier label;
    public WhileStatementParser(Identifier label) {
        this.label = label;
    }

    @Override
    public WhileStatement parse() {
        var whileStatement = new WhileStatement();
        whileStatement.setLabel(label);

        stream().consumeNextToken(); // "while"

        stream().consumeAndMatch("(");
        var conditionExpression = new ExpressionParser().parse();
        stream().consumeAndMatch(")");

        whileStatement.setConditionExpression(conditionExpression);

        var hasMultipleStatement = stream().peekNextToken().getValue().equals("{");

        if (hasMultipleStatement)
            stream().consumeAndMatch("{");

        scopeManager().pushLoopScope(whileStatement);
        var whileBody = new BlockParser(whileStatement, hasMultipleStatement).parse();
        scopeManager().popLoopScope();

        if (hasMultipleStatement)
            stream().consumeAndMatch("}");

        whileStatement.setBody(whileBody);
        return whileStatement;
    }
}
