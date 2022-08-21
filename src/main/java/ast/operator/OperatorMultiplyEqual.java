package ast.operator;

import ast.BinaryExpression;
import ast.Expression;
import ast.Identifier;
import ast.value.JSValue;
import org.js.Interpreter;

import static myutils.Macro.verify;

public class OperatorMultiplyEqual extends AbstractBinaryOperator {
    public OperatorMultiplyEqual(Expression lhs, Expression rhs) {
        super(lhs, rhs);
    }

    @Override
    public Object execute() {
        verify(lhs instanceof Identifier);
        var dest = (Identifier)lhs;
        var src = (JSValue)(new OperatorMultiply(lhs, rhs)).execute();
        Interpreter.get().rewrite(dest, src);
        return src;
    }

    @Override
    public String getDump(int indent) {
        return " *= ";
    }

    @Override
    public String toString() {
        return "*=";
    }
}
