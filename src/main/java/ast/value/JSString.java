package ast.value;

import ast.Expression;
import ast.operator.*;

import java.util.Objects;

import static myutils.Macro.unreachable;
import static myutils.Macro.verify;

public class JSString implements
        OperatorPlus.Interface,
        OperatorLessThanOrEqual.Interface,
        OperatorGreaterThan.Interface,
        OperatorGreaterThanOrEqual.Interface,
        OperatorEquals.Interface,
        OperatorLessThan.Interface {
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
            unreachable();
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
        verify(value instanceof String);
        this.value = (String) value;
        return this;
    }

    @Override
    public JSString asString() {
        return this;
    }

    @Override
    public String toString() {
        return "\"" + value + "\"";
    }

    public static JSString from(String string) {
        return new JSString().setValue(string);
    }

    private JSString getJSString(Expression expr) {
        verify(expr != null);
        var valueOrError = expr.execute();
        // TODO: Handle type casts.
        verify(valueOrError instanceof JSString);
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
        return JSBoolean.from(value.length() < string.value.length());
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

    @Override
    public JSBoolean isGreaterThan(Expression other) {
        var isEqual = isEqualTo(other);
        if (isEqual.isTruthy())
            return JSBoolean.from(false);
        var isLessThan = isLessThan(other);
        if (isLessThan.isTruthy())
            return JSBoolean.from(false);
        return JSBoolean.from(true);
    }

    @Override
    public JSBoolean isGreaterThanOrEqual(Expression other) {
        var isGreater = isGreaterThan(other);
        if (isGreater.isTruthy())
            return JSBoolean.from(true);
        return JSBoolean.from(isEqualTo(other).isTruthy());
    }

    @Override
    public JSBoolean isLessThanOrEqual(Expression other) {
        var isLess = isLessThan(other);
        if (isLess.isTruthy())
            return JSBoolean.from(true);
        return JSBoolean.from(isEqualTo(other).isTruthy());
    }

    @Override
    public String getPrettyString(int indent) {
        return toString();
    }
}
