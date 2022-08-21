package ast.operator;

import ast.Expression;
import ast.value.JSValue;
import myutils.Assertable;

public class OperatorDivide extends AbstractBinaryOperator {
    public OperatorDivide(Expression lhs, Expression rhs) {
        super(lhs, rhs);
    }

    @Override
    public Object execute() {
        var lhsValue = Interface.valueOf(lhs.execute());
        var rhsValue = Interface.valueOf(rhs.execute());
        return lhsValue.divide(rhsValue);
    }

    @Override
    public String getDump(int indent) {
        return " / ";
    }

    public static interface Interface extends JSValue {
        public Expression divide(Expression rhs);

        static Interface valueOf(Object value) {
            Assertable._ASSERT(value instanceof Interface);
            return (Interface) value;
        }
    }

    @Override
    public String toString() {
        return "/";
    }
}
