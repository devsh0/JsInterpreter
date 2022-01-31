package ast;

import ast.value.JSValue;
import java.util.Objects;

public class IfStatement implements CompoundStatement {
    @Override
    public Object execute() {
        var valueOrError = conditionExpression.execute();
        // FIXME: Shouldn't `truthy` and `falsy` properties be defined on `Expression` rather than `JSValue`?
        Assert(valueOrError instanceof JSValue);
        var value = (JSValue)valueOrError;
        if (value.isTruthy())
            return body.execute();
        return JSValue.undefined();
    }

    public IfStatement setBody(final Block body) {
        Objects.requireNonNull(body);
        this.body = body;
        return this;
    }

    public IfStatement setConditionExpression(Expression condition) {
        Objects.requireNonNull(condition);
        this.conditionExpression = condition;
        return this;
    }

    public static IfStatement from(Expression condition, Block body) {
        return new IfStatement().setConditionExpression(condition).setBody(body);
    }

    @Override
    public Block getBody() {
        return body;
    }

    @Override
    public Statement getLastStatement() {
        return body.getLastStatement();
    }

    private Expression conditionExpression;
    private Block body;
}
