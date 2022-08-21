package ast.operator;

import ast.Expression;
import ast.value.JSBoolean;
import ast.value.JSValue;

import static myutils.Macro.verify;

public class OperatorLessThan extends AbstractBinaryOperator {
    public OperatorLessThan(Expression lhs, Expression rhs) {
        super(lhs, rhs);
    }

    @Override
    public Object execute() {
        var lhsValue = Interface.valueOf(lhs.execute());
        var rhsValue = Interface.valueOf(rhs.execute());
        return lhsValue.isLessThan(rhsValue);
    }

    @Override
    public String getDump(int indent) {
        return " < ";
    }

    public interface Interface extends JSValue {
        public JSBoolean isLessThan(Expression other);

        static Interface valueOf(Object value) {
            verify(value instanceof Interface);
            return (Interface) value;
        }
    }

    @Override
    public String toString() {
        return "<";
    }
}
