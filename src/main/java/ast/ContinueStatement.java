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

    @Override
    public String getDump(int indent) {
        var builder = getIndentedBuilder(indent);
        builder.append("continue ");
        if (label != null)
            builder.append(label.getDump(indent));
        return builder.toString();
    }

    public ContinueStatement setLabel(Identifier label) {
        Objects.requireNonNull(label);
        this.label = label;
        return this;
    }

    private CompoundStatement owner;
    private Identifier label;
}
