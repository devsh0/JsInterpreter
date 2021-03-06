package ast.operator;

import ast.Expression;
import ast.value.JSValue;
import myutils.Assertable;

public class OperatorMinus implements BinaryOperator {
    @Override
    public String getDump(int indent) {
        return " - ";
    }

    public static interface Subtractable extends JSValue {
        public Expression subtract(Expression rhs);

        static Subtractable valueOf(Object value) {
            Assertable._ASSERT(value instanceof Subtractable);
            return (Subtractable) value;
        }
    }

    @Override
    public Expression apply(Expression lhs, Expression rhs) {
        var lhsValue = Subtractable.valueOf(lhs.execute());
        var rhsValue = Subtractable.valueOf(rhs.execute());
        return lhsValue.subtract(rhsValue);
    }
}
