package ast.operator;

import ast.Expression;
import ast.value.JSBoolean;
import ast.value.JSValue;
import myutils.Assertable;

public class OperatorLessThan extends AbstractBinaryOperator {
    public OperatorLessThan(Expression lhs, Expression rhs) {
        super(lhs, rhs);
    }

    @Override
    public Object execute() {
        var lhsValue = SupportsLessThanTest.valueOf(lhs.execute());
        var rhsValue = SupportsLessThanTest.valueOf(rhs.execute());
        return lhsValue.isLessThan(rhsValue);
    }

    @Override
    public String getDump(int indent) {
        return " < ";
    }

    public static interface SupportsLessThanTest extends JSValue {
        public JSBoolean isLessThan(Expression other);

        static SupportsLessThanTest valueOf(Object value) {
            Assertable._ASSERT(value instanceof SupportsLessThanTest);
            return (SupportsLessThanTest) value;
        }
    }

    @Override
    public String toString() {
        return "<";
    }
}
