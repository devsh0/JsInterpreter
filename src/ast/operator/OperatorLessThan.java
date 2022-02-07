package ast.operator;

import ast.Expression;
import ast.value.JSBoolean;
import ast.value.JSValue;
import myutils.Assertable;

public class OperatorLessThan implements RelationalOperator {
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
    public Expression apply(Expression lhs, Expression rhs) {
        var lhsValue = SupportsLessThanTest.valueOf(lhs.execute());
        var rhsValue = SupportsLessThanTest.valueOf(rhs.execute());
        return lhsValue.isLessThan(rhsValue);
    }
}
