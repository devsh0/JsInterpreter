package ast.operator;

import ast.Expression;
import ast.value.JSBoolean;
import ast.value.JSValue;
import myutils.Assertable;

public class OperatorGreaterThanOrEqual implements RelationalOperator {
    public static interface SupportsGreaterThanOrEqualTest extends JSValue {
        public JSBoolean isGreaterThanOrEqual(Expression other);

        static SupportsGreaterThanOrEqualTest valueOf(Object value) {
            Assertable._assert(value instanceof SupportsGreaterThanOrEqualTest);
            return (SupportsGreaterThanOrEqualTest) value;
        }
    }
    @Override
    public Expression apply(Expression lhs, Expression rhs) {
        var lhsValue = SupportsGreaterThanOrEqualTest.valueOf(lhs.execute());
        var rhsValue = SupportsGreaterThanOrEqualTest.valueOf(rhs.execute());
        return lhsValue.isGreaterThanOrEqual(rhsValue);
    }
}
