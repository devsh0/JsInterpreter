package ast.value;

import ast.Expression;
import ast.operator.OperatorEquals;
import ast.operator.OperatorLessThan;
import ast.operator.OperatorPlus;

import java.util.Objects;

public class JSString implements OperatorPlus.Addable,
        OperatorEquals.SupportsEqualityTest,
        OperatorLessThan.SupportsLessThanTest {
    private String value;

    @Override
    public Object execute() {
        return this;
    }

    @Override
    public Expression add(Expression rhs) {
        if (rhs instanceof JSString) {
            var otherString = (JSString) rhs;
            return JSString.from(value + otherString.value);
        } else if (rhs instanceof JSNumber) {
            var otherString = ((JSNumber) rhs).asString();
            return JSString.from(value + otherString.value);
        } else {
            Assert(false);
            return null;
        }
    }

    @Override
    public boolean isTruthy() {
        return !value.isEmpty();
    }

    @Override
    public boolean isFalsy() {
        return value.isEmpty();
    }

    @Override
    public Object getValue() {
        return value;
    }

    @Override
    public JSString setValue(Object value) {
        Objects.requireNonNull(value);
        Assert(value instanceof String);
        this.value = (String) value;
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

    @Override
    public void dump() {
        System.out.println("[JSString: '" + value + "']");
    }

    public static JSString from(String string) {
        return new JSString().setValue(string);
    }

    private JSString getJSString(Expression expr) {
        Objects.requireNonNull(expr);
        var valueOrError = expr.execute();
        // TODO: Handle type casts.
        Assert(valueOrError instanceof JSString);
        return (JSString) valueOrError;
    }

    @Override
    public JSBoolean isLessThan(Expression other) {
        var string = getJSString(other);
        var runUpto = Math.min(value.length(), string.value.length());
        for (int i = 0; i < runUpto; i++) {
            if (value.charAt(i) < string.value.charAt(i))
                return JSBoolean.from(true);
            if (value.charAt(i) > string.value.charAt(i))
                return JSBoolean.from(false);
        }
        return JSBoolean.from(false);
    }

    @Override
    public JSBoolean isEqualTo(Expression other) {
        var string = getJSString(other);
        if (value.length() != string.value.length())
            return JSBoolean.from(false);
        for (int i = 0; i < value.length(); i++)
            if (value.charAt(i) != string.value.charAt(i))
                return JSBoolean.from(false);
        return JSBoolean.from(true);
    }
}
