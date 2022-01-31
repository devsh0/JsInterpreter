package ast;

import ast.value.JSValue;
import org.js.Interpreter;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Block implements Statement {
    public Block (ASTNode owner) {
        scope = new Scope(owner);
    }

    @Override
    public Object execute() {
        var interpreter = Interpreter.get();
        interpreter.enterScope(scope);
        Object returnValue = JSValue.undefined();
        for (var statement : statementList) {
            returnValue = statement.execute();
            lastStatement = statement;

            if (lastStatement instanceof CompoundStatement compoundStatement)
                lastStatement = compoundStatement.getLastStatement();

            if (lastStatement instanceof ReturnStatement)
                return returnValue;
        }

        if (lastStatement instanceof ReturnStatement)
            interpreter.exitCurrentFunctionScope();
        else interpreter.exitCurrentScope();
        return returnValue;
    }

    public Block append(final Statement node) {
        Objects.requireNonNull(node);
        statementList.add(node);
        return this;
    }

    public Statement getLastStatement() {
        return lastStatement;
    }

    public Scope getScope() {
        return scope;
    }
    private List<Statement> statementList = new ArrayList<>();
    private Scope scope;
    private Statement lastStatement;
}
