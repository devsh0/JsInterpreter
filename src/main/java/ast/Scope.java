package ast;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import static myutils.Macro.verify;

public class Scope implements ASTNode {
    private CompoundStatement owner;

    @Override
    public Object execute() {
        return null;
    }

    public Scope(CompoundStatement owner) {
        verify(owner != null);
        this.owner = owner;
    }

    public Scope addOrUpdateEntry(final Identifier identifier, final ASTNode symbolEntity) {
        this.identifierMap.put(identifier, symbolEntity);
        return this;
    }

    public Optional<ASTNode> getIdentifierEntity(final Identifier identifier) {
        var entity = identifierMap.get(identifier);
        if (entity == null)
            return Optional.empty();
        return Optional.of(entity);
    }

    public Scope clone() {
        var clone = new Scope(owner);
        for (var key : identifierMap.keySet())
            clone.identifierMap.put(key, identifierMap.get(key));
        return clone;
    }

    @Override
    public boolean equals(Object other) {
        if (this == other)
            return true;
        if (other == null || getClass() != other.getClass())
            return false;
        Scope scope = (Scope) other;
        return owner == scope.owner;
    }

    @Override
    public String getPrettyString(int indent) {
        return "";
    }

    @Override
    public int hashCode() {
        return Objects.hash(owner, identifierMap);
    }

    private Map<Identifier, ASTNode> identifierMap = new HashMap<>();
}
