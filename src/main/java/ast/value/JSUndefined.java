package ast.value;

import ast.Expression;
import ast.operator.OperatorPlus;

import static myutils.Macro.unimplemented;

public class JSUndefined implements
        JSValue,
        OperatorPlus.Interface {
    @Override
    public Object execute() {
        return this;
    }

    @Override
    public boolean isTruthy() {
        return false;
    }

    @Override
    public boolean isFalsy() {
        return true;
    }

    @Override
    public Object getValue() {
        return null;
    }

    @Override
    public JSValue setValue(Object value) {
        return this;
    }

    @Override
    public JSString asString() {
        return JSString.from("undefined");
    }

    @Override
    public String getString(int indent) {
        return "undefined";
    }

    @Override
    public Expression add(Expression rhs) {
        if (rhs instanceof JSString)
            return asString().add(rhs);
        unimplemented();
        return null;
    }
}
