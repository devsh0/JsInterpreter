package ast.operator;

public interface RelationalOperator extends BinaryOperator {
    public static RelationalOperator Equals = new OperatorEquals();
    public static RelationalOperator NotEquals = new OperatorNotEquals();
    public static RelationalOperator LessThan = new OperatorLessThan();
    public static RelationalOperator LessThanOrEqual = new OperatorLessThanOrEqual();
    public static RelationalOperator GreaterThan = new OperatorGreaterThan();
    public static RelationalOperator GreaterThanOrEqual = new OperatorGreaterThanOrEqual();
}
