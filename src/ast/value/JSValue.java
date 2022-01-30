package ast.value;

import ast.Expression;
import myutils.Assertable;

public interface JSValue extends Expression {
    public Object getValue();

    public JSValue setValue(Object value);

    public JSString asString();

    public static JSNumber from(Number value) {
        return JSNumber.from(value);
    }

    public static JSString from(String string) {
        return JSString.from(string);
    }

    public static JSUndefined undefined() {
        return new JSUndefined();
    }

}
