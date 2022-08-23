package ast.operator;

import ast.ASTNode;
import ast.Expression;
import lexer.Token;

import static myutils.Macro.unimplemented;

public interface UnaryOperator extends ASTNode {
    public Expression getOperand();
    public static UnaryOperator construct(Token opToken, Expression operand, boolean isPrefix) {
        switch (opToken.getValue()) {
            case "++":
                if (isPrefix)
                    return new PrefixIncrement(operand);
            case "--":
                if (isPrefix)
                    return new PrefixDecrement(operand);
            case "!":
                return new BooleanNot(operand);
        }

        unimplemented();
        return null;
    }
}
