package ast.operator;

import ast.Expression;
import ast.value.JSBoolean;
import ast.value.JSValue;
import myutils.Assertable;

public class OperatorLessThanOrEqual implements RelationalOperator {
    public static interface SupportsLessThanOrEqualTest extends JSValue {
        public JSBoolean isLessThanOrEqual(Expression other);

        static SupportsLessThanOrEqualTest valueOf(Object value) {
            Assertable._ASSERT(value instanceof SupportsLessThanOrEqualTest);
            return (SupportsLessThanOrEqualTest) value;
        }
    }
    @Override
    public Expression apply(Expression lhs, Expression rhs) {
        var lhsValue = SupportsLessThanOrEqualTest.valueOf(lhs.execute());
        var rhsValue = SupportsLessThanOrEqualTest.valueOf(rhs.execute());
        return lhsValue.isLessThanOrEqual(rhsValue);
    }
}
