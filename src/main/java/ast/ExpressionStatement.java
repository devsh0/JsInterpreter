package ast;

public class ExpressionStatement implements Statement, Expression {
    @Override
    public Object execute() {
        if (expression == null)
            return null;
        return expression.execute();
    }

    @Override
    public String getDump(int indent) {
        return expression.getDump(indent);
    }

    public ExpressionStatement setSource(Expression expression) {
        this.expression = expression;
        return this;
    }

    public Expression getSource() {
        return this.expression;
    }

    protected Expression expression;
}
