package ast;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class Scope implements ASTNode {
    @Override
    public Object execute() {
        return null;
    }

    public Scope addEntry(final Identifier identifier, final ASTNode symbolEntity) {
        this.identifierMap.put(identifier, symbolEntity);
        return this;
    }

    public Optional<ASTNode> getIdentifierEntity(final Identifier identifier) {
        var entity = identifierMap.get(identifier);
        if (entity == null)
            return Optional.empty();
        return Optional.of(entity);
    }

    private Map<Identifier, ASTNode> identifierMap = new HashMap<>();
}
