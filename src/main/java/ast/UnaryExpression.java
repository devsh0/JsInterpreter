package ast;

import ast.operator.UnaryOperator;

public class UnaryExpression implements Expression {
    private UnaryOperator operator;

    @Override
    public Object execute() {
        return null;
    }

    public UnaryExpression setOperator(UnaryOperator operator) {
        this.operator = operator;
        return this;
    }

    public Expression getOperand() {
        return operator.getOperand();
    }

    public UnaryOperator getOperator() {
        return operator;
    }

    @Override
    public String getDump(int indent) {
        return operator.getDump(indent);
    }
}
