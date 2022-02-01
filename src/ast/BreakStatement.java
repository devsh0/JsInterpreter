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
        if (label != null) {
            var entityOrEmpty = interpreter.queryScope(label);
            Assert(entityOrEmpty.isPresent());
            var entity = entityOrEmpty.get();
            Assert(entity instanceof SupportsBreakStatement);
            var breakableEntity = (SupportsBreakStatement)entity;
            interpreter.prepareToExitCompoundStatement(breakableEntity);
        }
        if (label == null) {
            interpreter.prepareToExitCompoundStatement(owner);
            return null;
        }


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
