package org.js;

import ast.*;
import myutils.Macro;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static myutils.Macro.verify;

public class Interpreter implements Macro {
    private static Interpreter interpreter = null;
    private List<Scope> stack = new ArrayList<>();

    public Interpreter enterScope(final Scope scope) {
        stack.add(scope);
        return this;
    }

    public Optional<ASTNode> queryScope(final Identifier identifier) {
        for (int i = stack.size() - 1; i >= 0; i--) {
            var scope = stack.get(i);
            var entityOrNull = scope.getIdentifierEntity(identifier);
            if (entityOrNull.isPresent())
                return entityOrNull;
        }
        return Optional.empty();
    }

    public Scope getCurrentScope() {
        verify(stack.size() >= 1);
        return stack.get(stack.size() - 1);
    }

    public Expression rewrite(Identifier identifier, Expression value) {
        for (int i = stack.size() - 1; i >= 0; i--) {
            var scope = stack.get(i);
            var entityOrNull = scope.getIdentifierEntity(identifier);
            if (entityOrNull.isPresent()) {
                scope.addOrUpdateEntry(identifier, value);
                return value;
            }
        }
        return value;
    }

    public Scope exitCurrentScope() {
        verify(stack.size() > 0);
        return stack.remove(stack.size() - 1);
    }

    public Object run(final ASTNode programNode) {
        verify(programNode instanceof Program);
        Program program = (Program) programNode;
        stack.add(new Scope(program));
        return program.execute();
    }

    public static Interpreter get() {
        if (interpreter == null)
            interpreter = new Interpreter();
        return interpreter;
    }
}
