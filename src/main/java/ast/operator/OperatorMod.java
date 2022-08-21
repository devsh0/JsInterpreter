package ast.operator;

import ast.Expression;
import ast.value.JSValue;
import myutils.Assertable;

public class OperatorMod extends AbstractBinaryOperator {
    public OperatorMod(Expression lhs, Expression rhs) {
        super(lhs, rhs);
    }

    @Override
    public Object execute() {
        var lhsValue = Modable.valueOf(lhs.execute());
        var rhsValue = Modable.valueOf(rhs.execute());
        return lhsValue.mod(rhsValue);
    }

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
    public String toString() {
        return "%";
    }
}
