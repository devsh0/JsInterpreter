package ast.operator;

import ast.ASTNode;
import ast.Expression;
import lexer.Token;

import static myutils.Macro.unimplemented;
import static myutils.Macro.unreachable;

public interface BinaryOperator extends ASTNode {
    public Expression getLHS();

    public Expression getRHS();

    @Override
    public String toString();

    public static enum Associativity {
        LTR, RTL
    }

    public static BinaryOperator construct(Token opToken, Expression lhs, Expression rhs) {
        switch (opToken.getValue()) {
            case "+":
                return new OperatorPlus(lhs, rhs);
            case "-":
                return new OperatorMinus(lhs, rhs);
            case "*":
                return new OperatorMultiply(lhs, rhs);
            case "/":
                return new OperatorDivide(lhs, rhs);
            case "%":
                return new OperatorMod(lhs, rhs);
            case "=":
                return new OperatorAssignment(lhs, rhs);
            case ">":
                return new OperatorGreaterThan(lhs, rhs);
            case "<":
                return new OperatorLessThan(lhs, rhs);
            case "<=":
                return new OperatorLessThanOrEqual(lhs, rhs);
            case ">=":
                return new OperatorGreaterThanOrEqual(lhs, rhs);
            case "==":
                return new OperatorEquals(lhs, rhs);
            case "+=":
                return new OperatorPlusEqual(lhs, rhs);
            case "-=":
                return new OperatorMinusEqual(lhs, rhs);
            case "*=":
                return new OperatorMultiplyEqual(lhs, rhs);
            case "/=":
                return new OperatorDivideEqual(lhs, rhs);
            case "%=":
                return new OperatorModEqual(lhs, rhs);
            case "!=":
                return new OperatorNotEquals(lhs, rhs);
            case "&&":
                return new OperatorAnd(lhs, rhs);
            case "||":
                return new OperatorOr(lhs, rhs);
            default:
                unimplemented();
                return null;
        }
    }

    public static boolean isAssignmentOperator(Token token) {
        var value = token.getValue();
        return switch (value) {
            case "=", "+=", "-=", "*=", "/=", "%=" -> true;
            default -> false;
        };
    }

    public static boolean isLogicalOperator(Token token) {
        var value = token.getValue();
        return switch (value) {
            case "&&", "||" -> true;
            default -> false;
        };
    }

    public static boolean isRelationalOperator(Token token) {
        var value = token.getValue();
        return switch (value) {
            case "<", ">", "<=", ">=", "==", "!=" -> true;
            default -> false;
        };
    }

    public static boolean isTermOperator(Token token) {
        var value = token.getValue();
        return switch (value) {
            case "+", "-" -> true;
            default -> false;
        };
    }

    public static boolean isFactorOperator(Token token) {
        var value = token.getValue();
        return switch (value) {
            case "*", "/", "%" -> true;
            default -> false;
        };
    }

    public static boolean isBinaryOperator(Token token) {
        return isAssignmentOperator(token) || isLogicalOperator(token) || isRelationalOperator(token)
                || isTermOperator(token) || isFactorOperator(token);
    }

    public static int getPrecedenceOf(Token token) {
        if (isAssignmentOperator(token))
            return 2;
        if (isLogicalOperator(token))
            return 3 + ((token.getValue().equals("&&")) ? 1 : 0);
        if (isRelationalOperator(token)) {
            var opString = token.getValue();
            return switch (opString) {
                case "==", "!=" -> 8;
                default -> 9;
            };
        }
        if (isTermOperator(token))
            return 11;
        if (isFactorOperator(token))
            return 12;

        unreachable();
        return 0;
    }

    public static Associativity getAssociativityOf(Token token) {
        if (isAssignmentOperator(token))
            return Associativity.RTL;
        return Associativity.LTR;
    }
}
