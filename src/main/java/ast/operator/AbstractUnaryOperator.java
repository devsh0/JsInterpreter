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
    public String getPrettyString(int indent) {
        return this + operand.getPrettyString(indent);
    }
}
