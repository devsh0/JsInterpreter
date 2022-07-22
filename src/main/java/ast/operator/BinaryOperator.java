package ast.operator;

import ast.ASTNode;
import ast.Expression;

public interface BinaryOperator extends ASTNode {
    public static BinaryOperator Plus = new OperatorPlus();
    public static BinaryOperator Minus = new OperatorMinus();
    public static BinaryOperator Multiply = new OperatorMultiply();
    public static BinaryOperator Divide = new OperatorDivide();
    public static BinaryOperator Mod = new OperatorMod();
    public static BinaryOperator Assignment = new OperatorAssignment();
    public static BinaryOperator PlusEqual = new OperatorPlusEqual();
    public static BinaryOperator MinusEqual = new OperatorMinusEqual();
    public static BinaryOperator MultiplyEqual = new OperatorMultiplyEqual();
    public static BinaryOperator DivideEqual = new OperatorDivideEqual();
    public static BinaryOperator ModEqual = new OperatorModEqual();

    @Override
    public default Object execute() {
        return this;
    }
    Expression apply(Expression lhs, Expression rhs);
}
