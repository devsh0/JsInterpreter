package ast.operator;

import ast.Expression;

public abstract class AbstractUnaryOperator implements UnaryOperator {
    protected final Expression operand;

    public AbstractUnaryOperator(Expression operand) {
        this.operand = operand;
    }

    public Expression getOperand() {
        return operand;
    }

    public abstract String toString();

    @Override
    public String getString(int indent) {
        return this + operand.getString(indent);
    }
}
