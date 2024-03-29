package ast.operator;

import ast.Expression;
import ast.value.JSBoolean;

public class OperatorNotEquals extends AbstractBinaryOperator {
    public OperatorNotEquals(Expression lhs, Expression rhs) {
        super(lhs, rhs);
    }

    @Override
    public Object execute() {
        var lhsValue = OperatorEquals.Interface.valueOf(lhs.execute());
        var rhsValue = OperatorEquals.Interface.valueOf(rhs.execute());
        return JSBoolean.from(lhsValue.isEqualTo(rhsValue).isFalsy());
    }

    @Override
    public String toString() {
        return "!=";
    }
}
