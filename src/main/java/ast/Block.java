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
        scope = new Scope(owner);
    }

    @Override
    public Object execute() {
        var interpreter = Interpreter.get();
        var clone = scope.clone();

        interpreter.enterScope(clone);
        Object returnValue = JSValue.undefined();
        for (var statement : statementList)
            returnValue = statement.execute();
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

    private List<Statement> statementList = new ArrayList<>();
    private CompoundStatement owner;
    private Scope scope;
}
