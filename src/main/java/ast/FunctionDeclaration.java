package ast;

import org.js.Interpreter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class FunctionDeclaration extends CompoundStatement {
    @Override
    public Object execute() {
        Interpreter.get().getCurrentScope().addOrUpdateEntry(id, this);
        return this;
    }

    public FunctionDeclaration setIdentifier(final Identifier id) {
        Objects.requireNonNull(id);
        this.id = id;
        return this;
    }

    public FunctionDeclaration setParameters(Identifier... identifiers) {
        Objects.requireNonNull(identifiers);
        parameters.addAll(Arrays.asList(identifiers));
        return this;
    }

    public static FunctionDeclaration from(Identifier id, Identifier[] parameters, Block body) {
        var fun = new FunctionDeclaration().setIdentifier(id).setParameters(parameters);
        fun.setBody(body);
        for (var parameter : parameters)
            fun.body.append(VariableDeclaration.from(parameter));
        return fun;
    }

    public Identifier getId() {
        return id;
    }

    public List<Identifier> getParameters() {
        return parameters;
    }

    @Override
    public String getString(int indent) {
        var builder = new StringBuilder("\n");
        builder.append(" ".repeat(indent));
        builder.append("function ");
        builder.append(id);
        builder.append("(");

        for (int i = 0; i < parameters.size() - 1; i++) {
            builder.append(parameters.get(i).getString(indent));
            builder.append(", ");
        }

        builder.append(parameters.get(parameters.size() - 1).getString(indent));
        builder.append(")");
        builder.append(this.body.getString(indent));
        return builder.toString();
    }

    private Identifier id;
    private List<Identifier> parameters = new ArrayList<>();
}
