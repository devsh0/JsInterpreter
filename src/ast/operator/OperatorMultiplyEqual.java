package ast.operator;

import ast.BinaryExpression;
import ast.Expression;
import ast.Identifier;
import ast.value.JSValue;
import org.js.Interpreter;

public class OperatorMultiplyEqual implements BinaryOperator {
    @Override
    public String getDump(int indent) {
        return " += ";
    }

    @Override
    public Expression apply(Expression lhs, Expression rhs) {
        VERIFY(lhs instanceof Identifier);
        var dest = (Identifier)lhs;
        var src = (JSValue)(new BinaryExpression().setOperator(Multiply).setLhs(dest).setRhs(rhs)).execute();
        Interpreter.get().rewrite(dest, src);
        return src;
    }
}
