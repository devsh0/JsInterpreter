package ast;

import java.util.Objects;

public class BreakStatement implements Statement {
    public BreakStatement(CompoundStatement target) {
        Objects.requireNonNull(target);
        this.target = target;
    }

    @Override
    public Object execute() {
        throw new BreakException(target);
    }

    public BreakStatement setLabel(Identifier label) {
        this.label = label;
        return this;
    }

    @Override
    public String getPrettyString(int indent) {
        var builder = new StringBuilder("\n");
        builder.append(" ".repeat(indent));
        builder.append("break");
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
