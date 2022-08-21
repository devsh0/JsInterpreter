package ast.value;

import ast.Expression;
import ast.operator.*;

import java.util.Objects;

import static myutils.Macro.verify;

public class JSBoolean implements
        JSValue,
        OperatorGreaterThan.Interface,
        OperatorGreaterThanOrEqual.Interface,
        OperatorLessThanOrEqual.Interface,
        OperatorLessThan.Interface,
        OperatorEquals.Interface {
    private boolean value;

    @Override
    public Object execute() {
        return this;
    }

    @Override
    public String getDump(int indent) {
        return value + "";
    }

    @Override
    public boolean isTruthy() {
        return value;
    }

    @Override
    public boolean isFalsy() {
        return !isTruthy();
    }

    @Override
    public Object getValue() {
        return value;
    }

    @Override
    public JSBoolean setValue(Object value) {
        verify(value instanceof Boolean);
        this.value = (Boolean) value;
        return this;
    }

    @Override
    public JSString asString() {
        return JSString.from(toString());
    }

    @Override
    public String toString() {
        return value ? "true" : "false";
    }

    public static JSBoolean from(Number number) {
        Objects.requireNonNull(number);
        return new JSBoolean().setValue(number.doubleValue() != 0);
    }

    public static JSBoolean from(String string) {
        Objects.requireNonNull(string);
        return new JSBoolean().setValue(!string.isEmpty());
    }

    public static JSBoolean from(Boolean value) {
        Objects.requireNonNull(value);
        return new JSBoolean().setValue(value);
    }

    private JSBoolean getJSBoolean(Expression expr) {
        Objects.requireNonNull(expr);
        var valueOrError = expr.execute();
        // TODO: Handle type casts.
        verify(valueOrError instanceof JSBoolean);
        return (JSBoolean)valueOrError;
    }

    @Override
    public JSBoolean isEqualTo(Expression other) {
        var bool = getJSBoolean(other);
        if (value != bool.value)
            return JSBoolean.from(false);
        return JSBoolean.from(true);
    }

    @Override
    public JSBoolean isLessThan(Expression other) {
        var bool = getJSBoolean(other);
        if (value && !(bool.value))
            return JSBoolean.from(true);
        return JSBoolean.from(false);
    }

    @Override
    public JSBoolean isGreaterThan(Expression other) {
        if (isLessThan(other).isTruthy())
            return from(false);
        if (isEqualTo(other).isTruthy())
            return from(false);
        return from(true);
    }

    @Override
    public JSBoolean isGreaterThanOrEqual(Expression other) {
        if (isGreaterThan(other).isTruthy())
            return from(true);
        return from(isEqualTo(other).isTruthy());
    }

    @Override
    public JSBoolean isLessThanOrEqual(Expression other) {
        if (isLessThan(other).isTruthy())
            return from(true);
        return from(isEqualTo(other).isTruthy());
    }
}
