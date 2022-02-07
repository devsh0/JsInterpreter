package ast.operator;

import ast.Expression;
import ast.value.JSValue;
import myutils.Assertable;

public class OperatorMod implements BinaryOperator {
    @Override
    public String getDump(int indent) {
        return " % ";
    }

    public static interface Modable extends JSValue {
        public Expression mod(Expression rhs);

        static Modable valueOf(Object value) {
            Assertable._ASSERT(value instanceof Modable);
            return (Modable) value;
        }
    }

    @Override
    public Expression apply(Expression lhs, Expression rhs) {
        var lhsValue = Modable.valueOf(lhs.execute());
        var rhsValue = Modable.valueOf(rhs.execute());
        return lhsValue.mod(rhsValue);
    }
}
