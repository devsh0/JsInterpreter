package ast.operator;

import ast.Expression;
import ast.value.JSBoolean;
import ast.value.JSValue;

import static myutils.Macro.verify;

public class BooleanNot extends AbstractUnaryOperator {
    public BooleanNot(Expression operand) {
        super(operand);
    }

    @Override
    public Object execute() {
        var entity = operand.execute();
        verify(entity instanceof JSValue);
        var value = (JSValue) entity;
        return JSBoolean.from(value.isTruthy() ? false : true);
    }

    @Override
    public String toString() {
        return "!";
    }
}
