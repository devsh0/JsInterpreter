package ast.operator;

import ast.Expression;
import ast.value.JSValue;
import myutils.Assertable;

public class OperatorDivide extends AbstractBinaryOperator {
    public OperatorDivide(Expression lhs, Expression rhs) {
        super(lhs, rhs);
    }

    @Override
    public Object execute() {
        var lhsValue = Divisible.valueOf(lhs.execute());
        var rhsValue = Divisible.valueOf(rhs.execute());
        return lhsValue.divide(rhsValue);
    }

    @Override
    public String getDump(int indent) {
        return " / ";
    }

    public static interface Divisible extends JSValue {
        public Expression divide(Expression rhs);

        static Divisible valueOf(Object value) {
            Assertable._ASSERT(value instanceof Divisible);
            return (Divisible) value;
        }
    }

    @Override
    public String toString() {
        return "/";
    }
}
