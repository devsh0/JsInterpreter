package ast;

import ast.value.JSValue;

import java.util.Objects;

public class WhileStatement extends CompoundStatement {
    @Override
    public Object execute() {
        var valueOrError = conditionExpression.execute();
        Assert(valueOrError instanceof JSValue);
        var value = (JSValue) valueOrError;
        Object result = JSValue.undefined();
        while (value.isTruthy()) {
            result = body.execute();
            valueOrError = conditionExpression.execute();
            Assert(valueOrError instanceof JSValue);
            value = (JSValue) valueOrError;
        }
        return result;
    }

    public WhileStatement setConditionExpression(Expression expression) {
        Objects.requireNonNull(expression);
        conditionExpression = expression;
        return this;
    }

    private Expression conditionExpression;
}
