package ast;

import java.util.Objects;

import static myutils.Macro.verify;

public class ForStatement extends LoopStatement {

    @Override
    public Object execute() {
        initializer.execute();
        return super.execute();
    }

    public ForStatement setInitializer(Statement initializer) {
        verify(initializer != null);
        this.initializer = initializer;
        return this;
    }

    public ForStatement setUpdateStatement(ExpressionStatement updateStatement) {
        verify(this.updateStatement == null);
        this.updateStatement = updateStatement;
        return this;
    }

    public CompoundStatement setLabel(Identifier label) {
        this.label = label;
        return this;
    }

    @Override
    public LoopStatement setBody(Block block) {
        Objects.requireNonNull(block);
        if (updateStatement != null)
            block.append(updateStatement);
        super.setBody(block);
        return this;
    }

    @Override
    public String getPrettyString(int indent) {
        var builder = new StringBuilder("\n");

        if (label != null) {
            builder.append(" ".repeat(indent));
            builder.append(label);
            builder.append(":\n");
        }

        builder.append(" ".repeat(indent));
        builder.append("for (");
        var initString = initializer == null ? "" : initializer.getPrettyString(indent);
        builder.append(initString.trim()).append(" ");
        builder.append(conditionStatement == null ? "" : conditionStatement.getSource().getPrettyString(indent));
        builder.append("; ");
        builder.append(updateStatement == null ? "" : updateStatement.getSource().getPrettyString(indent));
        builder.append(")");
        builder.append(this.body.getPrettyString(indent));
        return builder.toString();
    }

    public ExpressionStatement getUpdateStatement() {
        return updateStatement;
    }

    public Identifier getLabel() {
        return label;
    }

    private Statement initializer;
    private ExpressionStatement updateStatement;
    private Identifier label;
}
