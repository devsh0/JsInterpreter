package ast;

import java.util.Objects;

public class ReturnStatement implements Statement {
    @Override
    public Object execute() {
        return expression.execute();
    }

    public ReturnStatement setExpression(final Expression expression) {
        Objects.requireNonNull(expression);
        this.expression = expression;
        return this;
    }

    public static ReturnStatement from (Expression expression) {
        return new ReturnStatement().setExpression(expression);
    }

    private Expression expression;
}
