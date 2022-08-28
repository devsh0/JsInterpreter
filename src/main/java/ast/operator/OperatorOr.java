package ast.operator;

import ast.Expression;
import ast.value.JSBoolean;
import ast.value.JSValue;

public class OperatorOr extends AbstractBinaryOperator {
    public OperatorOr(Expression lhs, Expression rhs) {
        super(lhs, rhs);
    }

    @Override
    public Object execute() {
        var lhsValue = (JSValue) lhs.execute();
        var rhsValue = (JSValue) rhs.execute();
        return JSBoolean.from(lhsValue.isTruthy() || rhsValue.isTruthy());
    }

    @Override
    public String toString() {
        return "||";
    }
}
