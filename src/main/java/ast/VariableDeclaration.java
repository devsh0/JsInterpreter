package ast;

import ast.value.JSUndefined;
import ast.value.JSValue;
import org.js.Interpreter;

import java.util.Objects;

public class VariableDeclaration implements Statement {
    @Override
    public Object execute() {
        Interpreter.get().getCurrentScope().addOrUpdateEntry(id, initializer);
        return this;
    }

    public VariableDeclaration setIdentifier(Identifier id) {
        Objects.requireNonNull(id);
        this.id = id;
        return this;
    }

    Identifier getIdentifier() {
        return this.id;
    }

    Expression getInitializer() {
        return this.initializer;
    }

    public VariableDeclaration setInitializer(Expression expression) {
        Objects.requireNonNull(expression);
        this.initializer = expression;
        return this;
    }

    @Override
    public String getPrettyString(int indent) {
        var builder = new StringBuilder("\n");
        builder.append(" ".repeat(indent));
        builder.append("let ");
        if (!initializer.equals(JSValue.undefined()))
            builder.append(initializer.getPrettyString(indent));
        builder.append(";");
        return builder.toString();
    }

    private Identifier id;
    private Expression initializer = new JSUndefined();

    public static VariableDeclaration from(Identifier id) {
        return new VariableDeclaration().setIdentifier(id);
    }

    public static VariableDeclaration from(Identifier id, Expression initializer) {
        return from(id).setInitializer(initializer);
    }
}
