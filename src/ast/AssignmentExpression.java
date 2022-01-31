package ast;

import org.js.Interpreter;

import java.util.Objects;

public class AssignmentExpression extends ExpressionStatement {
    @Override
    public Object execute() {
        var interpreter = Interpreter.get();
        var entity = interpreter.queryScope(id);
        Assert(entity.isPresent());
        interpreter.getCurrentScope().addEntry(id, expression);
        return expression;
    }

    public AssignmentExpression setIdentifier(Identifier id) {
        Objects.requireNonNull(id);
        this.id = id;
        return this;
    }

    public static AssignmentExpression from(Identifier id, Expression expression) {
        var assignExpression = new AssignmentExpression().setIdentifier(id);
        assignExpression.setExpression(expression);
        return assignExpression;
    }

    private Identifier id;
}
