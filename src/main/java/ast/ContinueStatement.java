package ast;

import org.js.Interpreter;

import java.util.Objects;

import static myutils.Macro.verify;

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
            verify(entityOrEmpty.isPresent());
            var entity = entityOrEmpty.get();
            verify(entity instanceof CompoundStatement);
            continuableEntity = (CompoundStatement) entity;
        } else {
            continuableEntity = owner;
        }
        throw new ContinueException(continuableEntity);
    }

    public ContinueStatement setLabel(Identifier label) {
        Objects.requireNonNull(label);
        this.label = label;
        return this;
    }

    @Override
    public String getPrettyString(int indent) {
        var builder = new StringBuilder("\n");
        builder.append(" ".repeat(indent));
        builder.append("continue");
        if (label != null) {
            builder.append(" ");
            builder.append(label);
        }
        builder.append(";");
        return builder.toString();
    }

    private CompoundStatement owner;
    private Identifier label;
}
