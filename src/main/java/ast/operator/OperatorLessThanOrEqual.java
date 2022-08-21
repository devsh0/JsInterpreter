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
        var lhsValue = SupportsLessThanOrEqualTest.valueOf(lhs.execute());
        var rhsValue = SupportsLessThanOrEqualTest.valueOf(rhs.execute());
        return lhsValue.isLessThanOrEqual(rhsValue);
    }

    @Override
    public String getDump(int indent) {
        return " <= ";
    }

    public static interface SupportsLessThanOrEqualTest extends JSValue {
        public JSBoolean isLessThanOrEqual(Expression other);

        static SupportsLessThanOrEqualTest valueOf(Object value) {
            Assertable._ASSERT(value instanceof SupportsLessThanOrEqualTest);
            return (SupportsLessThanOrEqualTest) value;
        }
    }

    @Override
    public String toString() {
        return "<=";
    }
}
