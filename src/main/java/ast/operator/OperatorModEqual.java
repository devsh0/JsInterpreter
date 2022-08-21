package ast.operator;

import ast.BinaryExpression;
import ast.Expression;
import ast.Identifier;
import ast.value.JSValue;
import org.js.Interpreter;

public class OperatorModEqual extends AbstractBinaryOperator {
    public OperatorModEqual(Expression lhs, Expression rhs) {
        super(lhs, rhs);
    }

    @Override
    public Object execute() {
        VERIFY(lhs instanceof Identifier);
        var dest = (Identifier)lhs;
        var src = (JSValue)(new OperatorMod(lhs, rhs)).execute();
        Interpreter.get().rewrite(dest, src);
        return src;
    }

    @Override
    public String getDump(int indent) {
        return " %= ";
    }

    @Override
    public String toString() {
        return "%=";
    }
}
