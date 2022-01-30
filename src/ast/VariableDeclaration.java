package ast;

import ast.value.JSUndefined;
import org.js.Interpreter;

import java.util.Objects;

public class VariableDeclaration implements ASTNode {
    @Override
    public Object execute() {
        Interpreter.get().getCurrentScope().addEntry(id, initializer);
        return this;
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
