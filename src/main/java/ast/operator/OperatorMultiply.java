package ast.operator;

import ast.Expression;
import ast.value.JSValue;
import myutils.Assertable;

public class OperatorMultiply extends AbstractBinaryOperator {
    public OperatorMultiply(Expression lhs, Expression rhs) {
        super(lhs, rhs);
    }

    @Override
    public Object execute() {
        var lhsValue = Multiplicable.valueOf(lhs.execute());
        var rhsValue = Multiplicable.valueOf(rhs.execute());
        return lhsValue.multiply(rhsValue);
    }

    @Override
    public String getDump(int indent) {
        return " * ";
    }

    public static interface Multiplicable extends JSValue {
        public Expression multiply(Expression rhs);

        static Multiplicable valueOf(Object value) {
            Assertable._ASSERT(value instanceof Multiplicable);
            return (Multiplicable) value;
        }
    }

    @Override
    public String toString() {
        return "*";
    }
}
