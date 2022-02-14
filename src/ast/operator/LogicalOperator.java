package ast.operator;

public interface LogicalOperator extends BinaryOperator {
    public static LogicalOperator And = new OperatorAnd();
    public static LogicalOperator Or = new OperatorOr();
}
