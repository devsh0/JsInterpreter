package ast.operator;

import ast.Expression;
import ast.value.JSBoolean;
import ast.value.JSValue;
import myutils.Assertable;

public class OperatorLessThanOrEqual extends AbstractBinaryOperator {
    public OperatorLessThanOrEqual(Expression lhs, Expression rhs) {
        super(lhs, rhs);
    }

    @Override
    public Object execute() {
        var lhsValue = Interface.valueOf(lhs.execute());
        var rhsValue = Interface.valueOf(rhs.execute());
        return lhsValue.isLessThanOrEqual(rhsValue);
    }

    @Override
    public String getDump(int indent) {
        return " <= ";
    }

    public static interface Interface extends JSValue {
        public JSBoolean isLessThanOrEqual(Expression other);

        static Interface valueOf(Object value) {
            Assertable._ASSERT(value instanceof Interface);
            return (Interface) value;
        }
    }

    @Override
    public String toString() {
        return "<=";
    }
}
