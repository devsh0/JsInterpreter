package ast.operator;

import ast.Expression;
import ast.value.JSBoolean;

public class OperatorNotEquals implements RelationalOperator {
    @Override
    public String getDump(int indent) {
        return " != ";
    }

    @Override
    public Expression apply(Expression lhs, Expression rhs) {
        var lhsValue = OperatorEquals.SupportsEqualityTest.valueOf(lhs.execute());
        var rhsValue = OperatorEquals.SupportsEqualityTest.valueOf(rhs.execute());
        return JSBoolean.from(lhsValue.isEqualTo(rhsValue).isFalsy());
    }
}
