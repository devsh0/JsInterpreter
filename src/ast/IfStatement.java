package ast;

import ast.value.JSValue;
import java.util.Objects;

public class IfStatement extends CompoundStatement {
    @Override
    public Object execute() {
        var valueOrError = conditionExpression.execute();
        // FIXME: Shouldn't `truthy` and `falsy` properties be defined on `Expression` rather than `JSValue`?
        Assert(valueOrError instanceof JSValue);
        var value = (JSValue)valueOrError;
        if (value.isTruthy())
            return body.execute();
        return alternate != null ? alternate.execute() : JSValue.undefined();
    }

    public IfStatement setConditionExpression(Expression condition) {
        Objects.requireNonNull(condition);
        this.conditionExpression = condition;
        return this;
    }

    public IfStatement setAlternate(Statement statement) {
        Objects.requireNonNull(statement);
        Assert(statement instanceof IfStatement || statement instanceof Block);
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
