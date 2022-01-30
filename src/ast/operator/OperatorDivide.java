package ast.operator;

import ast.Expression;
import ast.value.JSValue;
import myutils.Assertable;

public class OperatorDivide implements BinaryOperator {
    public static interface Divisible extends JSValue {
        public Expression divide(Expression rhs);

        static Divisible valueOf(Object value) {
            Assertable._assert(value instanceof Divisible);
            return (Divisible)value;
        }
    }

    @Override
    public Expression apply(Expression lhs, Expression rhs) {
        var lhsValue = Divisible.valueOf(lhs.execute());
        var rhsValue = Divisible.valueOf(rhs.execute());
        return lhsValue.divide(rhsValue);
    }
}
