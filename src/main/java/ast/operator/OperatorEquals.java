package ast.operator;

import ast.Expression;
import ast.value.JSBoolean;
import ast.value.JSValue;
import myutils.Assertable;

public class OperatorEquals extends AbstractBinaryOperator {
    public OperatorEquals(Expression lhs, Expression rhs) {
        super(lhs, rhs);
    }

    @Override
    public Object execute() {
        var lhsValue = SupportsEqualityTest.valueOf(lhs.execute());
        var rhsValue = SupportsEqualityTest.valueOf(rhs.execute());
        return lhsValue.isEqualTo(rhsValue);
    }

    @Override
    public String getDump(int indent) {
        return " == ";
    }

    public static interface SupportsEqualityTest extends JSValue {
        public JSBoolean isEqualTo(Expression other);

        static SupportsEqualityTest valueOf(Object value) {
            Assertable._ASSERT(value instanceof SupportsEqualityTest);
            return (SupportsEqualityTest) value;
        }
    }

    @Override
    public String toString() {
        return "==";
    }
}
