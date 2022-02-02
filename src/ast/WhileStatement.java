package ast;

import ast.value.JSValue;
import org.js.Interpreter;

import java.util.Objects;

public class WhileStatement extends CompoundStatement {
    @Override
    public Object execute() {
        var interpreter = Interpreter.get();
        if (label != null)
            interpreter.getCurrentScope().addEntry(label, this);
        Object result = JSValue.undefined();

        while (true) {
            var valueOrError = conditionExpression.execute();
            Assert(valueOrError instanceof JSValue);
            var value = (JSValue) valueOrError;
            if (value.isFalsy())
                break;

            try {
                result = body.execute();
            } catch (BreakException exception) {
                if (this != exception.getTarget())
                    throw exception;
                break;
            } catch (ContinueException exception) {
                if (this != exception.getTarget())
                    throw exception;

                // continue; but redundant to specify.
            }
        }
        return result;
    }

    public WhileStatement setConditionExpression(Expression expression) {
        Objects.requireNonNull(expression);
        conditionExpression = expression;
        return this;
    }

    public CompoundStatement setLabel(Identifier label) {
        Objects.requireNonNull(label);
        this.label = label;
        return this;
    }

    public Identifier getLabel() {
        return label;
    }

    private Expression conditionExpression;
    private Identifier label;
}
