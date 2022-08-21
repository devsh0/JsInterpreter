package ast;

import ast.value.JSValue;
import java.util.Objects;

import static myutils.Macro.verify;

public class IfStatement extends CompoundStatement {
    @Override
    public Object execute() {
        // FIXME: Shouldn't `truthy` and `falsy` properties be defined on `Expression` rather than `JSValue`?
        var valueOrError = conditionExpression.execute();
        verify(valueOrError instanceof JSValue);
        var value = (JSValue)valueOrError;
        if (value.isTruthy())
            return body.execute();
        return alternate != null ? alternate.execute() : JSValue.undefined();
    }

    @Override
    public String getDump(int indent) {
        var builder = getIndentedBuilder(indent);
        builder.append("if (").append(conditionExpression.getDump(indent)).append(")");
        builder.append(body.getDump(indent));
        if (alternate != null)
            builder.append("\n" + getIndent(indent)).append("else").append(alternate.getDump(indent));
        return builder.toString();
    }

    public IfStatement setConditionExpression(Expression condition) {
        verify(condition != null);
        this.conditionExpression = condition;
        return this;
    }

    public IfStatement setAlternate(Statement statement) {
        verify(statement != null);
        verify(statement instanceof IfStatement || statement instanceof Block);
        this.alternate = statement;
        return this;
    }

    public static IfStatement from(Expression condition, Block body) {
        var statement =  new IfStatement().setConditionExpression(condition);
        statement.setBody(body);
        return statement;
    }

    private Expression conditionExpression;
    private Statement alternate;
}
