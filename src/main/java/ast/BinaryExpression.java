package ast;

import ast.operator.BinaryOperator;

import java.util.Objects;

public class BinaryExpression implements Expression {
    @Override
    public Object execute() {
        return operator.execute();
    }

    public BinaryExpression setOperator(final BinaryOperator operator) {
        Objects.requireNonNull(operator);
        this.operator = operator;
        return this;
    }

    public Expression getLHS() {
        return this.operator.getLHS();
    }

    public Expression getRHS() {
        return this.operator.getRHS();
    }

    public BinaryOperator getOperator() {
        return operator;
    }

    public String getPrettyString(int indent) {
        return operator.getPrettyString(indent);
    }

    private BinaryOperator operator;
}
