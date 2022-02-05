package ast.operator;

import ast.Expression;
import ast.value.JSValue;
import myutils.Assertable;

public class OperatorPlus implements BinaryOperator {
    public static interface Addable extends JSValue {
        public Expression add(Expression rhs);

        static Addable valueOf(Object value) {
            Assertable._ASSERT(value instanceof Addable);
            return (Addable) value;
        }
    }

    @Override
    public Expression apply(Expression lhs, Expression rhs) {
        var lhsValue = Addable.valueOf(lhs.execute());
        var rhsValue = Addable.valueOf(rhs.execute());
        return lhsValue.add(rhsValue);
    }
}
