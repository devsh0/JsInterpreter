package parser;

import ast.Identifier;
import ast.WhileStatement;

public class LabeledStatementParser extends Parser {
    @Override
    public WhileStatement parse() {
        var label = Identifier.from(stream().consumeIdentifier().getValue()); // the label
        stream().consumeAndMatch(":");
        // FIXME: This can also be a switch or a for loop.
        return new WhileStatementParser(label).parse();
    }
}
