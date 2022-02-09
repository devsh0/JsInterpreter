package ast.operator;

import ast.Expression;
import ast.Identifier;
import ast.value.JSValue;
import org.js.Interpreter;

public class OperatorAssignment implements BinaryOperator {
    @Override
    public String getDump(int indent) {
        return " = ";
    }

    @Override
    public Expression apply(Expression lhs, Expression rhs) {
        VERIFY(lhs instanceof Identifier);
        var dest = (Identifier)lhs;
        var src = (JSValue)rhs.execute();
        Interpreter.get().rewrite(dest, src);
        return src;
    }
}
