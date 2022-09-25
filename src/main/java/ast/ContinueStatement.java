package ast;

import java.util.Objects;

public class ContinueStatement implements Statement {
    public ContinueStatement(CompoundStatement target) {
        Objects.requireNonNull(target);
        this.target = target;
    }

    @Override
    public Object execute() {
        throw new ContinueException(target);
    }

    public ContinueStatement setLabel(Identifier label) {
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

    private CompoundStatement target;
    private Identifier label;
}
