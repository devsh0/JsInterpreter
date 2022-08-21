package ast.operator;

import ast.Expression;
import ast.Identifier;
import ast.value.JSValue;
import org.js.Interpreter;

import static myutils.Macro.verify;

public class OperatorDivideEqual extends AbstractBinaryOperator {
    public OperatorDivideEqual(Expression lhs, Expression rhs) {
        super(lhs, rhs);
    }

    @Override
    public Object execute() {
        verify(lhs instanceof Identifier);
        var dest = (Identifier) lhs;

        var src = (JSValue) (new OperatorDivide(lhs, rhs)).execute();
        Interpreter.get().rewrite(dest, src);
        return src;
    }

    @Override
    public String getDump(int indent) {
        return " += ";
    }

    @Override
    public String toString() {
        return "/=";
    }
}
