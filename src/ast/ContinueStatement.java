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
        var interpreter = Interpreter.get();
        BreakAndContinueSupportingBlock continuableEntity;
        if (label != null) {
            var entityOrEmpty = interpreter.queryScope(label);
            Assert(entityOrEmpty.isPresent());
            var entity = entityOrEmpty.get();
            Assert(entity instanceof BreakAndContinueSupportingBlock);
            continuableEntity = (BreakAndContinueSupportingBlock) entity;
        } else
            continuableEntity = (BreakAndContinueSupportingBlock) owner;
        continuableEntity.setContinueFlag(true);
        interpreter.setExitPoint(continuableEntity);
        return null;
    }

    public ContinueStatement setLabel(Identifier label) {
        Objects.requireNonNull(label);
        this.label = label;
        return this;
    }

    private CompoundStatement owner;
    private Identifier label;
}
