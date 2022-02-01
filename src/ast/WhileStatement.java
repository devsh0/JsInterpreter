package ast;

import ast.value.JSValue;
import org.js.Interpreter;

import java.util.Objects;

public class WhileStatement extends BreakAndContinueSupportingBlock {
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

            result = body.execute();
            // Fixme: exit flag also should be defined on the compound statement rather than its body.
            if (body.testAndClearExitFlag()) {
                if (testAndClearContinueFlag())
                    continue;
                if (testAndClearBreakFlag())
                    break;
                break;
            }
        }
        return result;
    }

    public WhileStatement setConditionExpression(Expression expression) {
        Objects.requireNonNull(expression);
        conditionExpression = expression;
        return this;
    }

    @Override
    public BreakAndContinueSupportingBlock setLabel(Identifier label) {
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
