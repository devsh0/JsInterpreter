package ast.operator;

import ast.ASTNode;
import ast.Expression;

public interface BinaryOperator extends ASTNode {
    public static BinaryOperator Plus = new OperatorPlus();
    public static BinaryOperator Minus = new OperatorMinus();
    public static BinaryOperator Multiply = new OperatorMultiply();
    public static BinaryOperator Divide = new OperatorDivide();

    @Override
    public default Object execute() {
        return this;
    }
    Expression apply(Expression lhs, Expression rhs);
}
