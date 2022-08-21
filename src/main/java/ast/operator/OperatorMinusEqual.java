package ast.operator;

import ast.BinaryExpression;
import ast.Expression;
import ast.Identifier;
import ast.value.JSValue;
import org.js.Interpreter;

public class OperatorMinusEqual extends AbstractBinaryOperator {
    public OperatorMinusEqual(Expression lhs, Expression rhs) {
        super(lhs, rhs);
    }

    @Override
    public Object execute() {
        VERIFY(lhs instanceof Identifier);
        var dest = (Identifier)lhs;
        var src = (JSValue)(new OperatorMinus(lhs, rhs)).execute();
        Interpreter.get().rewrite(dest, src);
        return src;
    }

    @Override
    public String getDump(int indent) {
        return " -= ";
    }

    @Override
    public String toString() {
        return "-=";
    }
}
