package ast;

import ast.value.JSValue;
import org.js.Interpreter;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class Block implements Statement {
    public Block(CompoundStatement owner) {
        Objects.requireNonNull(owner);
        this.owner = owner;
        scope = new Scope();
    }

    private boolean handleEarlyExit(Interpreter interpreter) {
        var ref = interpreter.getExitingCompoundStatementRef();
        if (ref == null)
            return false;
        notifyExit();
        if (owner == ref)
            interpreter.clearExitPoint();
        return true;
    }

    @Override
    public Object execute() {
        var interpreter = Interpreter.get();
        interpreter.enterScope(scope);
        Object returnValue = JSValue.undefined();
        for (var statement : statementList) {
            returnValue = statement.execute();
            if (handleEarlyExit(interpreter))
                break;
        }
        interpreter.exitCurrentScope();
        return returnValue;
    }

    public Block append(final Statement node) {
        Objects.requireNonNull(node);
        statementList.add(node);
        return this;
    }

    public Optional<ASTNode> getOwner() {
        if (owner == null)
            return Optional.empty();
        return Optional.of(owner);
    }

    public Scope getScope() {
        return scope;
    }

    private void notifyExit() {
        shouldExit = true;
    }

    public boolean testAndClearExitFlag() {
        if (shouldExit) {
            shouldExit = false;
            return true;
        }
        return false;
    }

    private List<Statement> statementList = new ArrayList<>();
    private CompoundStatement owner;
    private Scope scope;
    private boolean shouldExit = false;
}
