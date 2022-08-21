package ast.operator;

import ast.Expression;
import ast.value.JSValue;
import myutils.Assertable;

public class OperatorMinus extends AbstractBinaryOperator {
    public OperatorMinus(Expression lhs, Expression rhs) {
        super(lhs, rhs);
    }

    @Override
    public Object execute() {
        var lhsValue = Subtractable.valueOf(lhs.execute());
        var rhsValue = Subtractable.valueOf(rhs.execute());
        return lhsValue.subtract(rhsValue);
    }

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
    public String toString() {
        return "-";
    }
}
