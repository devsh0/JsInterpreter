package ast.operator;

import ast.Expression;
import ast.Identifier;
import ast.value.JSBoolean;
import ast.value.JSNumber;
import ast.value.JSValue;
import org.js.Interpreter;

import static myutils.Macro.verify;

public class BooleanNot extends AbstractUnaryOperator {
    public BooleanNot(Expression operand) {
        super(operand);
    }

    @Override
    public Object execute() {
        var entity = operand.execute();
        verify(entity instanceof JSValue);
        var value = (JSValue)entity;
        return JSBoolean.from(value.isTruthy() ? false : true);
    }

    @Override
    public String getDump(int indent) {
        return "!" + operand.getDump(indent);
    }

    @Override
    public String toString() {
        return "!";
    }
}
