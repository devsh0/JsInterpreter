package ast;

import java.util.Objects;

public class ExpressionStatement implements Statement, Expression {
    @Override
    public Object execute() {
        return expression.execute();
    }

    @Override
    public String getDump(int indent) {
        return expression.getDump(indent);
    }

    public ExpressionStatement setSource(Expression expression) {
        Objects.requireNonNull(expression);
        this.expression = expression;
        return this;
    }

    protected Expression expression;
}
