package ast;

import org.js.Interpreter;

import java.util.Objects;

public class BreakStatement implements Statement {
    public BreakStatement(CompoundStatement owner) {
        Objects.requireNonNull(owner);
        this.owner = owner;
    }

    @Override
    public Object execute() {
        CompoundStatement breakableEntity;
        if (label != null) {
            var entityOrEmpty = Interpreter.get().queryScope(label);
            ASSERT(entityOrEmpty.isPresent());
            var entity = entityOrEmpty.get();
            ASSERT(entity instanceof CompoundStatement);
            breakableEntity = (CompoundStatement) entity;
        } else {
            breakableEntity = owner;
        }
        throw new BreakException(breakableEntity);
    }

    public BreakStatement setLabel(Identifier label) {
        Objects.requireNonNull(label);
        this.label = label;
        return this;
    }

    private CompoundStatement owner;
    private Identifier label;
}
