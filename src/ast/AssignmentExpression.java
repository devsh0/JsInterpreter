package ast;

import ast.value.JSValue;
import org.js.Interpreter;

import java.util.Objects;

public class AssignmentExpression extends ExpressionStatement {
    @Override
    public Object execute() {
        var interpreter = Interpreter.get();
        var value = super.execute();
        ASSERT(value instanceof JSValue);
        return interpreter.rewrite(id, (JSValue)value);
    }

    public AssignmentExpression setDestination(Identifier id) {
        Objects.requireNonNull(id);
        this.id = id;
        return this;
    }

    public static AssignmentExpression from(Identifier id, Expression expression) {
        var assignExpression = new AssignmentExpression().setDestination(id);
        assignExpression.setSource(expression);
        return assignExpression;
    }

    @Override
    public String getDump(int indent) {
        var builder = getIndentedBuilder(indent);
        builder.append(id.getDump(indent)).append(" = ").append(expression.getDump(indent));
        return builder.toString();
    }

    private Identifier id;
}
