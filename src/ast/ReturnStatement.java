package ast;

import org.js.Interpreter;

import java.util.Objects;

public class ReturnStatement implements Statement {

    public ReturnStatement(FunctionDeclaration ref) {
        functionRef = ref;
    }

    @Override
    public Object execute() {
        throw new ReturnException(functionRef, expression.execute());
    }

    public ReturnStatement setExpression(final Expression expression) {
        Objects.requireNonNull(expression);
        this.expression = expression;
        return this;
    }

    public FunctionDeclaration getFunctionRef() {
        return functionRef;
    }

    private Expression expression;
    private FunctionDeclaration functionRef;
}
