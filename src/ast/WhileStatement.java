package ast;

import ast.value.JSValue;
import org.js.Interpreter;

import java.util.Objects;

public class WhileStatement extends SupportsBreakStatement {
    @Override
    public Object execute() {
        var interpreter = Interpreter.get();
        if (label != null)
            interpreter.getCurrentScope().addEntry(label, this);
        var valueOrError = conditionExpression.execute();
        Assert(valueOrError instanceof JSValue);
        var value = (JSValue) valueOrError;
        Object result = JSValue.undefined();

        while (value.isTruthy()) {
            result = body.execute();
            if (body.testAndClearExitFlag())
                break;
            valueOrError = conditionExpression.execute();
            Assert(valueOrError instanceof JSValue);
            value = (JSValue) valueOrError;
        }
        return result;
    }

    public WhileStatement setConditionExpression(Expression expression) {
        Objects.requireNonNull(expression);
        conditionExpression = expression;
        return this;
    }

    @Override
    public SupportsBreakStatement setLabel(Identifier label) {
        Objects.requireNonNull(label);
        this.label = label;
        return this;
    }

    @Override
    public Identifier getLabel() {
        return label;
    }

    private Expression conditionExpression;
    private Identifier label;
}
