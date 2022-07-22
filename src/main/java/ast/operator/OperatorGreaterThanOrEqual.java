package ast.operator;

import ast.Expression;
import ast.value.JSBoolean;
import ast.value.JSValue;
import myutils.Assertable;

public class OperatorGreaterThanOrEqual implements RelationalOperator {
    @Override
    public String getDump(int indent) {
        return " >= ";
    }

    public static interface SupportsGreaterThanOrEqualTest extends JSValue {
        public JSBoolean isGreaterThanOrEqual(Expression other);

        static SupportsGreaterThanOrEqualTest valueOf(Object value) {
            Assertable._ASSERT(value instanceof SupportsGreaterThanOrEqualTest);
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
