package ast.value;

import ast.Expression;
import ast.Identifier;

import java.util.Map;

import static myutils.Macro.unimplemented;
import static myutils.Macro.verify;

public class JSObject implements JSValue {
    private final Map<Identifier, Expression> map;

    private JSObject(Map<Identifier, Expression> map) {
        this.map = map;
    }

    @Override
    public Object execute() {
        unimplemented();
        return null;
    }

    @Override
    public boolean isTruthy() {
        return !isFalsy();
    }

    @Override
    public boolean isFalsy() {
        return map.isEmpty();
    }

    @Override
    public Object getValue() {
        unimplemented();
        return null;
    }

    @Override
    public JSValue setValue(Object value) {
        unimplemented();
        return null;
    }

    @Override
    public JSString asString() {
        unimplemented();
        return null;
    }

    @Override
    public String getPrettyString(int indent) {
        unimplemented();
        return null;
    }

    @Override
    public String toString() {
        var builder = new StringBuilder("{");
        int size = map.size();
        int counter = 0;
        for (var key : map.keySet()) {
            builder.append(key.toString());
            builder.append(":");
            builder.append(map.get(key).toString());
            if (counter < size - 1)
                builder.append(",");
            counter++;
        }
        builder.append("}");
        return builder.toString();
    }

    public static JSObject from(Map<Identifier, Expression> map) {
        verify(map != null);
        return new JSObject(map);
    }
}
