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

    public Identifier getLabel() {
        return label;
    }

    private Statement initializer;
    private Expression updateExpression;
    private Identifier label;
}
