package ast;

import static myutils.Macro.verify;

public class ForStatement extends LoopStatement {
    @Override
    public Object execute() {
        initializer.execute();
        if (this.updateExpression != null)
            this.body.append(new ExpressionStatement().setSource(this.updateExpression));
        return super.execute();
    }

    public ForStatement setInitializer(Statement initializer) {
        verify(initializer != null);
        this.initializer = initializer;
        return this;
    }

    public ForStatement setUpdateExpression(Expression expression) {
        this.updateExpression = expression;
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
        builder.append(conditionExpression == null ? "" : conditionExpression.getString(indent));
        builder.append("; ");
        builder.append(updateExpression == null ? "" : updateExpression.getString(indent));
        builder.append(")");
        builder.append(this.body.getString(indent));
        return builder.toString();
    }

    public Identifier getLabel() {
        return label;
    }

    private Statement initializer;
    private Expression updateExpression;
    private Identifier label;
}
