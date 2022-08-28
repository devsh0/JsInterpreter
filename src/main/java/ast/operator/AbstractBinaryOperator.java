package ast.operator;

import ast.Expression;

import java.util.Objects;

abstract public class AbstractBinaryOperator implements BinaryOperator {
    protected final Expression lhs;
    protected final Expression rhs;

    public AbstractBinaryOperator(Expression lhs, Expression rhs) {
        Objects.requireNonNull(lhs, "Null LHS!");
        Objects.requireNonNull(lhs, "Null RHS!");
        this.lhs = lhs;
        this.rhs = rhs;
    }

    @Override
    public Expression getLHS() {
        return this.lhs;
    }

    @Override
    public Expression getRHS() {
        return this.rhs;
    }

    @Override
    abstract public String toString();

    @Override
    public String getString(int indent) {
        return lhs.getString(indent) + " " + this + " " + rhs.getString(indent);
    }
}
