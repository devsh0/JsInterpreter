package ast;

import org.js.Interpreter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

// Fixme: Is there a difference between function declaration and definition in JS?
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

    @Override
    public String getDump(int indent) {
        var builder = getIndentedBuilder(indent);
        builder.append(String.format("function %s(", id));
        for (var parameter : parameters)
            builder.append(String.format("%s, ", parameter));
        if (parameters.size() > 0)
            builder = new StringBuilder(builder.substring(0, builder.lastIndexOf(",")));
        builder.append(")");
        builder.append(body.getDump(indent));
        return builder.toString();
    }

    public Identifier getId() {
        return id;
    }

    public List<Identifier> getParameters() {
        return parameters;
    }

    private Identifier id;
    private List<Identifier> parameters = new ArrayList<>();
}
