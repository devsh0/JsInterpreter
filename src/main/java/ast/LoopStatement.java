package ast;

import ast.value.JSValue;
import org.js.Interpreter;

import java.util.Objects;

import static myutils.Macro.verify;

public abstract class LoopStatement extends CompoundStatement {
    protected Identifier label;
    protected ExpressionStatement conditionStatement;

    public LoopStatement() {
        this.conditionStatement = new ExpressionStatement().setSource(JSValue.from(true));
    }

    @Override
    public Object execute() {
        if (label != null)
            Interpreter.get().getCurrentScope().addOrUpdateEntry(label, this);
        Object result = JSValue.undefined();
        while (true) {
            var valueOrError = conditionStatement.execute();
            verify(valueOrError instanceof JSValue);
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
                if (this instanceof ForStatement forStmt) {
                    var updateStatement = forStmt.getUpdateStatement();
                    if (updateStatement != null)
                        updateStatement.execute();
                }
            }
        }
        return result;
    }

    @Override
    public LoopStatement setBody(Block body) {
        Objects.requireNonNull(body);
        this.body = body;
        return this;
    }

    public CompoundStatement setLabel(Identifier label) {
        this.label = label;
        return this;
    }

    public CompoundStatement setConditionStatement(ExpressionStatement statement) {
        if (statement != null)
            conditionStatement = statement;
        return this;
    }

    public Identifier getLabel() {
        return label;
    }

    @Override
    abstract public String getPrettyString(int indent);
}
