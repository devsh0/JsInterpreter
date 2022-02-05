package ast.operator;

import ast.Expression;
import ast.value.JSBoolean;
import ast.value.JSValue;
import myutils.Assertable;

public class OperatorGreaterThan implements RelationalOperator {
    public static interface SupportsGreaterThanTest extends JSValue {
        public JSBoolean isGreaterThan(Expression other);

        static SupportsGreaterThanTest valueOf(Object value) {
            Assertable._ASSERT(value instanceof SupportsGreaterThanTest);
            return (SupportsGreaterThanTest) value;
        }
    }
    @Override
    public Expression apply(Expression lhs, Expression rhs) {
        var lhsValue = SupportsGreaterThanTest.valueOf(lhs.execute());
        var rhsValue = SupportsGreaterThanTest.valueOf(rhs.execute());
        return lhsValue.isGreaterThan(rhsValue);
    }
}
