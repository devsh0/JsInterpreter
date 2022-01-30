package ast;

import org.js.Interpreter;

import java.util.Objects;

public class AssignmentExpression implements ExpressionStatement {
    @Override
    public Object execute() {
        var interpreter = Interpreter.get();
        var entity = interpreter.queryScope(id);
        Assert(entity.isPresent());
        interpreter.getCurrentScope().addEntry(id, value);
        return value;
    }

    public AssignmentExpression setIdentifier(Identifier id) {
        Objects.requireNonNull(id);
        this.id = id;
        return this;
    }

    public AssignmentExpression setValue(Expression expression) {
        Objects.requireNonNull(expression);
        this.value = expression;
        return this;
    }

    public static AssignmentExpression from(Identifier id, Expression expression) {
        return new AssignmentExpression().setIdentifier(id).setValue(expression);
    }

    private Identifier id;
    private Expression value;
}
