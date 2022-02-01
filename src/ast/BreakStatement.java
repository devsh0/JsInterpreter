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
        var interpreter = Interpreter.get();
        BreakAndContinueSupportingBlock breakableEntity;
        if (label != null) {
            var entityOrEmpty = interpreter.queryScope(label);
            Assert(entityOrEmpty.isPresent());
            var entity = entityOrEmpty.get();
            Assert(entity instanceof BreakAndContinueSupportingBlock);
            breakableEntity = (BreakAndContinueSupportingBlock) entity;
        } else
            breakableEntity = (BreakAndContinueSupportingBlock) owner;
        breakableEntity.setBreakFlag(true);
        interpreter.setExitPoint(breakableEntity);
        return null;
    }

    public BreakStatement setLabel(Identifier label) {
        Objects.requireNonNull(label);
        this.label = label;
        return this;
    }

    private CompoundStatement owner;
    private Identifier label;
}
