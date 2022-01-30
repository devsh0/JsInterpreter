package ast;

import ast.operator.BinaryOperator;

import java.util.Objects;

public class BinaryExpression implements Expression {
    @Override
    public Object execute() {
        return operator.apply(lhs, rhs);
    }

    public BinaryExpression setOperator(final BinaryOperator operator) {
        Objects.requireNonNull(operator);
        this.operator = operator;
        return this;
    }

    public BinaryExpression setLhs(final Expression lhs) {
        Objects.requireNonNull(lhs);
        this.lhs = lhs;
        return this;
    }

    public BinaryExpression setRhs(final Expression rhs) {
        Objects.requireNonNull(rhs);
        this.rhs = rhs;
        return this;
    }

    public static BinaryExpression from(BinaryOperator operator, Expression lhs, Expression rhs) {
        return new BinaryExpression().setOperator(operator).setLhs(lhs).setRhs(rhs);
    }

    private BinaryOperator operator;
    private Expression lhs;
    private Expression rhs;
}
