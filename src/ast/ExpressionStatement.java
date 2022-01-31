package ast;

import java.util.Objects;

public class ExpressionStatement implements Statement, Expression {
    @Override
    public Object execute() {
        return expression.execute();
    }

    public ExpressionStatement setExpression(Expression expression) {
        Objects.requireNonNull(expression);
        this.expression = expression;
        return this;
    }

    protected Expression expression;
}
