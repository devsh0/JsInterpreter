package ast.value;

import ast.Expression;

public interface JSValue extends Expression {
    public boolean isTruthy();

    public boolean isFalsy();

    public Object getValue();

    public JSValue setValue(Object value);

    public JSString asString();

    public static JSNumber from(Number value) {
        return JSNumber.from(value);
    }

    public static JSString from(String string) {
        return JSString.from(string);
    }

    public static JSBoolean from(Boolean value) {
        return JSBoolean.from(value);
    }

    public static JSUndefined undefined() {
        return new JSUndefined();
    }

    @Override
    public String getPrettyString(int indent);
}
