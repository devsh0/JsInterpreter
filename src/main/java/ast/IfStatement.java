package ast;

import ast.value.JSValue;

import static myutils.Macro.verify;

public class IfStatement extends CompoundStatement {
    @Override
    public Object execute() {
        // FIXME: Shouldn't `truthy` and `falsy` properties be defined on `Expression` rather than `JSValue`?
        var valueOrError = conditionExpression.execute();
        verify(valueOrError instanceof JSValue);
        var value = (JSValue) valueOrError;
        if (value.isTruthy())
            return body.execute();
        return alternate != null ? alternate.execute() : JSValue.undefined();
    }

    public IfStatement setConditionExpression(Expression condition) {
        verify(condition != null);
        this.conditionExpression = condition;
        return this;
    }

    public IfStatement setAlternate(Block statement) {
        this.alternate = statement;
        return this;
    }

    public static IfStatement from(Expression condition, Block body) {
        var statement = new IfStatement().setConditionExpression(condition);
        statement.setBody(body);
        return statement;
    }

    public boolean hasAlternate() {
        return alternate != null;
    }

    @Override
    public String getString(int indent) {
        var builder = new StringBuilder("\n");
        builder.append(" ".repeat(indent));
        builder.append("if (");
        builder.append(conditionExpression.getString(indent));
        builder.append(")");
        builder.append(this.body.getString(indent));

        if (hasAlternate()) {
            builder.append(" ".repeat(indent));
            builder.append("else");
            builder.append(this.alternate.getString(indent));
        }

        return builder.toString();
    }

    private Expression conditionExpression;
    private Block alternate;
}
