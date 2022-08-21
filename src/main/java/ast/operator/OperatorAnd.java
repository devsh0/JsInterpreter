package ast.operator;

import ast.Expression;
import ast.value.JSBoolean;
import ast.value.JSValue;

public class OperatorAnd extends AbstractBinaryOperator {
    public OperatorAnd(Expression lhs, Expression rhs) {
        super(lhs, rhs);
    }

    @Override
    public Object execute() {
        var lhsValue = (JSValue) lhs.execute();
        var rhsValue = (JSValue) rhs.execute();
        return JSBoolean.from(lhsValue.isTruthy() && rhsValue.isTruthy());
    }

    @Override
    public String getDump(int indent) {
        return " && ";
    }

    @Override
    public String toString() {
        return "&&";
    }
}
