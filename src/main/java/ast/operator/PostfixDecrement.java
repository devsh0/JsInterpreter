package ast.operator;

import ast.Expression;
import ast.Identifier;
import ast.value.JSNumber;
import org.js.Interpreter;

import static myutils.Macro.verify;

public class PostfixDecrement extends AbstractUnaryOperator {
    public PostfixDecrement(Expression operand) {
        super(operand);
    }

    @Override
    public Object execute() {
        verify(operand instanceof Identifier);
        var variable = (Identifier) operand;
        var value = variable.execute();
        // FIXME: Allowed on other JsValues, returns NaN.
        verify(value instanceof JSNumber);
        var oldNumber = (JSNumber) value;
        var oldValue = (Double) oldNumber.getValue();
        var newNumber = JSNumber.from(oldValue - 1);
        Interpreter.get().rewrite(variable, newNumber);
        return oldNumber;
    }

    @Override
    public String toString() {
        return "--";
    }

    @Override
    public String getString(int indent) {
        return operand.getString(indent) + this;
    }
}
