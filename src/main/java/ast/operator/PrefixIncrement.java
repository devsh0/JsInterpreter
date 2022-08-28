package ast.operator;

import ast.Expression;
import ast.Identifier;
import ast.value.JSNumber;
import org.js.Interpreter;

import static myutils.Macro.verify;

public class PrefixIncrement extends AbstractUnaryOperator {
    public PrefixIncrement(Expression operand) {
        super(operand);
    }

    @Override
    public Object execute() {
        verify(operand instanceof Identifier);
        var variable = (Identifier) operand;
        var value = variable.execute();
        // FIXME: Allowed on other JsValues, returns NaN.
        verify(value instanceof JSNumber);
        var number = (JSNumber) value;
        var oldValue = (Double) number.getValue();
        var newValue = JSNumber.from(oldValue + 1);
        Interpreter.get().rewrite(variable, newValue);
        return newValue;
    }

    @Override
    public String getDump(int indent) {
        return " ++" + operand.getDump(indent);
    }

    @Override
    public String toString() {
        return "++";
    }
}
