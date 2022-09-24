package ast;

import static myutils.Macro.verify;

public class ForStatement extends LoopStatement {
    @Override
    public Object execute() {
        initializer.execute();
        if (this.updateStatement != null)
            this.body.append(new ExpressionStatement().setSource(this.updateStatement));
        return super.execute();
    }

    public ForStatement setInitializer(Statement initializer) {
        verify(initializer != null);
        this.initializer = initializer;
        return this;
    }

    public ForStatement setUpdateStatement(ExpressionStatement expression) {
        this.updateStatement = expression;
        return this;
    }

    public CompoundStatement setLabel(Identifier label) {
        this.label = label;
        return this;
    }

    @Override
    public String getString(int indent) {
        var builder = new StringBuilder("\n");

        if (label != null) {
            builder.append(" ".repeat(indent));
            builder.append(label);
            builder.append(":\n");
        }

        builder.append(" ".repeat(indent));
        builder.append("for (");
        var initString = initializer == null ? "" : initializer.getString(indent);
        if (initializer instanceof VariableDeclaration) {
            initString = initString.replace("\n", "");
            initString = initString.replace(";", "");
        }
        builder.append(initString.trim());
        builder.append("; ");
        builder.append(conditionStatement == null ? "" : conditionStatement.getString(indent));
        builder.append("; ");
        builder.append(updateStatement == null ? "" : updateStatement.getString(indent));
        builder.append(")");
        builder.append(this.body.getString(indent));
        return builder.toString();
    }

    public Identifier getLabel() {
        return label;
    }

    private Statement initializer;
    private ExpressionStatement updateStatement;
    private Identifier label;
}
