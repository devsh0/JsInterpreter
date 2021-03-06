package ast.value;

public class JSUndefined implements JSValue {
    @Override
    public Object execute() {
        return this;
    }

    @Override
    public String getDump(int indent) {
        return "undefined";
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
}
