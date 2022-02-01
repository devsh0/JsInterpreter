package org.js;

import ast.*;
import myutils.Assertable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Interpreter implements Assertable {
    private static Interpreter interpreter = null;
    private List<Scope> stack = new ArrayList<>();
    private CompoundStatement exitRef;

    private Interpreter() {
        Scope globalScope = new Scope();
        stack.add(globalScope);
    }

    public Interpreter setExitPoint(CompoundStatement ref) {
        exitRef = ref;
        return this;
    }

    public CompoundStatement getExitingCompoundStatementRef() {
        return exitRef;
    }

    public Interpreter clearExitPoint() {
        exitRef = null;
        return this;
    }

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

    public Scope getCurrentScope () {
        Assert(stack.size() >= 1);
        return stack.get(stack.size() - 1);
    }

    public Expression rewrite(Identifier identifier, Expression value) {
        for (int i = stack.size() - 1; i >= 0; i--) {
            var scope = stack.get(i);
            var entityOrNull = scope.getIdentifierEntity(identifier);
            if (entityOrNull.isPresent())
                scope.addEntry(identifier, value);
        }
        return value;
    }

    public Scope exitCurrentScope() {
        Assert(stack.size() > 0);
        return stack.remove(stack.size() - 1);
    }

    public Object run (final Program program) {
        return program.execute();
    }

    public static Interpreter get() {
        if (interpreter == null)
            interpreter = new Interpreter();
        return interpreter;
    }
}
