package ast.operator;

import ast.Expression;
import ast.value.JSValue;
import myutils.Assertable;

public class OperatorPlus extends AbstractBinaryOperator {
    public OperatorPlus(Expression lhs, Expression rhs) {
        super(lhs, rhs);
    }

    @Override
    public Object execute() {
        var lhsValue = Addable.valueOf(lhs.execute());
        var rhsValue = Addable.valueOf(rhs.execute());
        return lhsValue.add(rhsValue);
    }

    @Override
    public String getDump(int indent) {
        return " + ";
    }

    public static interface Addable extends JSValue {
        public Expression add(Expression rhs);

        static Addable valueOf(Object value) {
            Assertable._ASSERT(value instanceof Addable);
            return (Addable) value;
        }
    }

    @Override
    public String toString() {
        return "+";
    }
}
