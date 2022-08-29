package ast.operator;

import ast.ASTNode;
import ast.Expression;
import lexer.Token;

import static myutils.Macro.unimplemented;

public interface UnaryOperator extends ASTNode {
    public Expression getOperand();

    public static UnaryOperator construct(Token opToken, Expression operand, boolean isPrefix) {
        switch (opToken.getValue()) {
            case "++": return isPrefix ? new PrefixIncrement(operand) : new PostfixIncrement(operand);
            case "--": return isPrefix ? new PrefixDecrement(operand) : new PostfixDecrement(operand);
            case "!":
                return new BooleanNot(operand);
        }

        unimplemented();
        return null;
    }
}
