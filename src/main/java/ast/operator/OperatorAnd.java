package ast.operator;

import ast.Expression;
import ast.value.JSBoolean;
import ast.value.JSValue;

public class OperatorAnd implements LogicalOperator {
    @Override
    public String getDump(int indent) {
        return " && ";
    }

    @Override
    public Expression apply(Expression lhs, Expression rhs) {
        var lhsValue = (JSValue) lhs.execute();
        var rhsValue = (JSValue) rhs.execute();
        return JSBoolean.from(lhsValue.isTruthy() && rhsValue.isTruthy());
    }
}
