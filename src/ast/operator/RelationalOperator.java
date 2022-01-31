package ast.operator;

public interface RelationalOperator extends BinaryOperator {
    public static BinaryOperator Equals = new OperatorEquals();
    public static BinaryOperator LessThan = new OperatorLessThan();
}
