package ast;

import ast.operator.UnaryOperator;

public class UnaryExpression implements Expression {
    private UnaryOperator operator;

    @Override
    public Object execute() {
        return operator.execute();
    }

    public UnaryExpression setOperator(UnaryOperator operator) {
        this.operator = operator;
        return this;
    }

    @Override
    public String getString(int indent) {
        return operator.getString(indent);
    }

    public Expression getOperand() {
        return operator.getOperand();
    }

    public UnaryOperator getOperator() {
        return operator;
    }

}
