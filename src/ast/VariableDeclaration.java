package ast;

import ast.value.JSUndefined;
import org.js.Interpreter;

import java.util.Objects;

public class VariableDeclaration implements Statement {
    @Override
    public Object execute() {
        Interpreter.get().getCurrentScope().addEntry(id, initializer);
        return this;
    }

    @Override
    public String getDump(int indent) {
        var builder = getIndentedBuilder(indent);
        builder.append("let ").append(id);
        if (!initializer.getDump(indent).equals("undefined"))
            builder.append(" = ").append(initializer.getDump(indent));
        return builder.toString();
    }

    public VariableDeclaration setIdentifier(Identifier id) {
        Objects.requireNonNull(id);
        this.id = id;
        return this;
    }

    public VariableDeclaration setInitializer(Expression expression) {
        Objects.requireNonNull(expression);
        this.initializer = expression;
        return this;
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
