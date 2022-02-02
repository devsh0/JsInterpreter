package ast;

import org.js.Interpreter;

import java.util.Objects;

public class ContinueStatement implements Statement {
    public ContinueStatement(CompoundStatement owner) {
        Objects.requireNonNull(owner);
        this.owner = owner;
    }

    @Override
    public Object execute() {
        CompoundStatement continuableEntity;
        if (label != null) {
            var entityOrEmpty = Interpreter.get().queryScope(label);
            Assert(entityOrEmpty.isPresent());
            var entity = entityOrEmpty.get();
            Assert(entity instanceof CompoundStatement);
            continuableEntity = (CompoundStatement) entity;
        } else {
            continuableEntity = owner;
        }
        throw new
                ContinueException(continuableEntity);
    }

    public ContinueStatement setLabel(Identifier label) {
        Objects.requireNonNull(label);
        this.label = label;
        return this;
    }

    private CompoundStatement owner;
    private Identifier label;
}
