package ast.value;

import ast.Expression;
import ast.operator.OperatorPlus;

import java.util.Objects;

public class JSString implements OperatorPlus.Addable {
    private String value;
    @Override
    public Object execute() {
        return this;
    }

    @Override
    public Expression add(Expression rhs) {
        if (rhs instanceof JSString) {
            var otherString = (JSString)rhs;
            return JSString.from(value + otherString.value);
        } else if (rhs instanceof JSNumber) {
            var otherString = ((JSNumber)rhs).asString();
            return JSString.from(value + otherString.value);
        } else {
            Assert(false);
            return null;
        }
    }

    @Override
    public boolean isTruthy() {
        return value.isEmpty();
    }

    @Override
    public boolean isFalsy() {
        return !value.isEmpty();
    }

    @Override
    public Object getValue() {
        return value;
    }

    @Override
    public JSString setValue(Object value) {
        Objects.requireNonNull(value);
        Assert(value instanceof String);
        this.value = (String)value;
        return this;
    }

    @Override
    public JSString asString() {
        return this;
    }

    @Override
    public String toString() {
        return value;
    }

    public static JSString from(String string) {
        return new JSString().setValue(string);
    }
}
