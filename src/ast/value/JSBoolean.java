package ast.value;

import java.util.Objects;

public class JSBoolean implements JSValue {
    private boolean value;

    @Override
    public Object execute() {
        return this;
    }

    @Override
    public boolean isTruthy() {
        return value;
    }

    @Override
    public boolean isFalsy() {
        return value;
    }

    @Override
    public Object getValue() {
        return value;
    }

    @Override
    public JSBoolean setValue(Object value) {
        Assert(value instanceof Boolean);
        this.value = (Boolean)value;
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
}
