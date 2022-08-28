package parser;

import ast.BinaryExpression;
import ast.Expression;
import ast.Identifier;
import ast.UnaryExpression;
import ast.value.JSBoolean;
import ast.value.JSNumber;
import ast.value.JSString;
import lexer.TokenStream;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

public class ExpressionParserTest {

    private Expression parseExpression(String code) {
        var stream = new TokenStream(code);
        return new ExpressionParser(stream, null).parse();
    }

    @Test
    public void testNumberLiteral() {
        var code = "10;";
        var ten = (JSNumber) parseExpression(code);
        assertEquals("10", ten.toString());
    }

    @Test
    public void testStringLiteral() {
        var code = "\"alice in wonderland\";";
        var alice = (JSString) parseExpression(code);
        assertEquals("alice in wonderland", alice.toString());
    }

    @Test
    public void testBooleanLiteral() {
        String[] codes = {"true;", "false;"};
        Arrays.stream(codes).forEach(code -> {
            var bool = (JSBoolean) parseExpression(code);
            assertEquals(code.replace(";", ""), bool.toString());
        });
    }

    @Test
    public void testAdditiveExpressionWithNumericOperands() {
        String[] operators = {"+", "-"};
        Arrays.stream(operators).forEach(operator -> {
            String code = "1" + operator + "2;";
            var oneOpTwo = (BinaryExpression) parseExpression(code);
            var one = (JSNumber) oneOpTwo.getLHS();
            var op = oneOpTwo.getOperator();
            var two = (JSNumber) oneOpTwo.getRHS();
            assertEquals("1", one.toString());
            assertEquals(operator, op.toString());
            assertEquals("2", two.toString());
        });
    }

    @Test
    public void testAdditiveExpressionWithMultipleOperators() {
        var code = "1 + 2 - 3 + 6;";
        var onePlusTwoMinusThreePlusSix = (BinaryExpression) parseExpression(code);
        var onePlusTwoMinusThree = (BinaryExpression) onePlusTwoMinusThreePlusSix.getLHS();
        var onePlusTwo = (BinaryExpression) onePlusTwoMinusThree.getLHS();
        var one = (JSNumber) onePlusTwo.getLHS();
        var plus = onePlusTwo.getOperator();
        var two = (JSNumber) onePlusTwo.getRHS();
        var minus = onePlusTwoMinusThree.getOperator();
        var three = (JSNumber) onePlusTwoMinusThree.getRHS();

        assertEquals("1", one.toString());
        assertEquals("+", plus.toString());
        assertEquals("2", two.toString());
        assertEquals("-", minus.toString());
        assertEquals("3", three.toString());


        plus = onePlusTwoMinusThreePlusSix.getOperator();
        var six = (JSNumber) onePlusTwoMinusThreePlusSix.getRHS();
        assertEquals("+", plus.toString());
        assertEquals("6", six.toString());
    }

    @Test
    public void testGroupPrecedesFactor() {
        var code = "(1 + 6) * 2;";
        var onePlusSixTimesTwo = (BinaryExpression) parseExpression(code);
        var onePlusSix = (BinaryExpression) onePlusSixTimesTwo.getLHS();
        var one = (JSNumber) onePlusSix.getLHS();
        var plus = onePlusSix.getOperator();
        var six = (JSNumber) onePlusSix.getRHS();
        var times = onePlusSixTimesTwo.getOperator();
        var two = (JSNumber) onePlusSixTimesTwo.getRHS();

        assertEquals("1", one.toString());
        assertEquals("+", plus.toString());
        assertEquals("6", six.toString());
        assertEquals("*", times.toString());
        assertEquals("2", two.toString());
    }

    @Test
    public void testFactorPrecedesTerm() {
        var code = "1 + 2 * 3;";
        var onePlusTwoTimesThree = (BinaryExpression) parseExpression(code);
        var one = (JSNumber) onePlusTwoTimesThree.getLHS();
        var plus = onePlusTwoTimesThree.getOperator();
        var twoTimesThree = (BinaryExpression) onePlusTwoTimesThree.getRHS();
        var two = (JSNumber) twoTimesThree.getLHS();
        var times = twoTimesThree.getOperator();
        var three = (JSNumber) twoTimesThree.getRHS();

        assertEquals("1", one.toString());
        assertEquals("+", plus.toString());
        assertEquals("2", two.toString());
        assertEquals("*", times.toString());
        assertEquals("3", three.toString());
    }

    @Test
    public void testTermPrecedesRelational() {
        var code = "1 > 2 + 3;";
        var oneGtTwoPlusThree = (BinaryExpression) parseExpression(code);
        var one = (JSNumber) oneGtTwoPlusThree.getLHS();
        var gt = oneGtTwoPlusThree.getOperator();
        var twoPlusThree = (BinaryExpression) oneGtTwoPlusThree.getRHS();
        var two = (JSNumber) twoPlusThree.getLHS();
        var plus = twoPlusThree.getOperator();
        var three = (JSNumber) twoPlusThree.getRHS();

        assertEquals("1", one.toString());
        assertEquals(">", gt.toString());
        assertEquals("2", two.toString());
        assertEquals("+", plus.toString());
        assertEquals("3", three.toString());
    }

    @Test
    public void testRelationalPrecedesLogical() {
        var code = "1 || 2 > 6;";
        var oneOrTwoGtSix = (BinaryExpression) parseExpression(code);
        var one = (JSNumber) oneOrTwoGtSix.getLHS();
        var or = oneOrTwoGtSix.getOperator();
        var twoGtSix = (BinaryExpression) oneOrTwoGtSix.getRHS();
        var two = (JSNumber) twoGtSix.getLHS();
        var gt = twoGtSix.getOperator();
        var six = (JSNumber) twoGtSix.getRHS();

        assertEquals("1", one.toString());
        assertEquals("||", or.toString());
        assertEquals("2", two.toString());
        assertEquals(">", gt.toString());
        assertEquals("6", six.toString());
    }

    @Test
    public void testLogicalPrecedesAssignment() {
        var code = "value = 1 || 2;";
        var valueEqOneOrTwo = (BinaryExpression) parseExpression(code);
        var value = (Identifier) valueEqOneOrTwo.getLHS();
        var eq = valueEqOneOrTwo.getOperator();
        var oneOrTwo = (BinaryExpression) valueEqOneOrTwo.getRHS();
        var one = (JSNumber) oneOrTwo.getLHS();
        var or = oneOrTwo.getOperator();
        var two = oneOrTwo.getRHS();

        assertEquals("value", value.toString());
        assertEquals("=", eq.toString());
        assertEquals("1", one.toString());
        assertEquals("||", or.toString());
        assertEquals("2", two.toString());
    }

    @Test
    public void testAssignmentWithBinaryExpressionOnTheRHS() {
        var code = "value = 10 + 2;";
        var binaryExpression = (BinaryExpression) parseExpression(code);
        var value = (Identifier) binaryExpression.getLHS();
        var equal = binaryExpression.getOperator();
        var tenPlusTwo = (BinaryExpression) binaryExpression.getRHS();

        assertEquals("value", value.toString());
        assertEquals("=", equal.toString());

        var ten = (JSNumber) tenPlusTwo.getLHS();
        var plus = tenPlusTwo.getOperator();
        var two = (JSNumber) tenPlusTwo.getRHS();

        assertEquals("10", ten.toString());
        assertEquals("+", plus.toString());
        assertEquals("2", two.toString());
    }

    @Test
    public void testSimplePrefixIncrement() {
        var code = "++value;";
        var unaryExpression = (UnaryExpression) parseExpression(code);
        var plusPlus = unaryExpression.getOperator();
        var value = (Identifier) unaryExpression.getOperand();
        assertEquals("++", plusPlus.toString());
        assertEquals("value", value.toString());
    }

    @Test
    public void testSimplePrefixDecrement() {
        var code = "--value;";
        var unaryExpression = (UnaryExpression) parseExpression(code);
        var minusMinus = unaryExpression.getOperator();
        var value = (Identifier) unaryExpression.getOperand();
        assertEquals("--", minusMinus.toString());
        assertEquals("value", value.toString());
    }

    @Test
    public void testSimpleBooleanNot() {
        var code = "!value;";
        var unaryExpression = (UnaryExpression) parseExpression(code);
        var not = unaryExpression.getOperator();
        var value = (Identifier) unaryExpression.getOperand();
        assertEquals("!", not.toString());
        assertEquals("value", value.toString());
    }

    @Test
    public void testChainedBooleanNot() {
        var code = "!!value;";
        var unaryExpression = (UnaryExpression) parseExpression(code);
        var outerNot = unaryExpression.getOperator();
        unaryExpression = (UnaryExpression) outerNot.getOperand();
        var innerNot = unaryExpression.getOperator();
        var value = unaryExpression.getOperand();
        assertEquals("!", outerNot.toString());
        assertEquals("!", innerNot.toString());
        assertEquals("value", value.toString());
    }
}
