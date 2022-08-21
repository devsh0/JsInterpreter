package parser;

import ast.BinaryExpression;
import ast.Identifier;
import ast.value.JSBoolean;
import ast.value.JSNumber;
import ast.value.JSString;
import lexer.TokenStream;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class BinaryExpressionParserTest {
    @Test
    public void testNumberLiteral() {
        var code = "10;";
        var stream = new TokenStream(code);
        var parser = new BinaryExpressionParser(stream, null);
        var ten = (JSNumber)parser.parse();
        assertEquals("10.0", ten.toString());
    }

    @Test
    public void testStringLiteral() {
        var code = "\"alice in wonderland\";";
        var stream = new TokenStream(code);
        var parser = new BinaryExpressionParser(stream, null);
        var alice = (JSString)parser.parse();
        assertEquals("alice in wonderland", alice.toString());
    }

    @Test
    public void testBooleanLiteral() {
        String[] codes = {"true;", "false;"};
        Arrays.stream(codes).forEach(code -> {
            var stream = new TokenStream(code);
            var parser = new BinaryExpressionParser(stream, null);
            var bool = (JSBoolean)parser.parse();
            assertEquals(code.replace(";", ""), bool.toString());
        });
    }

    @Test
    public void testAdditiveExpressionWithNumericOperands() {
        String[] operators = {"+", "-"};
        Arrays.stream(operators).forEach(operator -> {
            String code = "1" + operator + "2;";
            var stream = new TokenStream(code);
            var parser = new BinaryExpressionParser(stream, null);
            var oneOpTwo = (BinaryExpression)parser.parse();
            var one = (JSNumber)oneOpTwo.getLHS();
            var op = oneOpTwo.getOperator();
            var two = (JSNumber)oneOpTwo.getRHS();
            assertEquals("1.0", one.toString());
            assertEquals(operator, op.toString());
            assertEquals("2.0", two.toString());
        });
    }

    @Test
    public void testAdditiveExpressionWithMultipleOperators() {
        var code = "1 + 2 - 3 + 6;";
        var stream = new TokenStream(code);
        var parser = new BinaryExpressionParser(stream, null);
        var onePlusTwoMinusThreePlusSix = (BinaryExpression)parser.parse();

        var onePlusTwo = (BinaryExpression)onePlusTwoMinusThreePlusSix.getLHS();
        var minus = onePlusTwoMinusThreePlusSix.getOperator();
        var threePlusSix = (BinaryExpression)onePlusTwoMinusThreePlusSix.getRHS();
        assertEquals("-", minus.toString());

        var one = (JSNumber)onePlusTwo.getLHS();
        var plus = onePlusTwo.getOperator();
        var two = (JSNumber)onePlusTwo.getRHS();

        assertEquals("1.0", one.toString());
        assertEquals("+", plus.toString());
        assertEquals("2.0", two.toString());

        var three = (JSNumber)threePlusSix.getLHS();
        plus = threePlusSix.getOperator();
        var six = (JSNumber)threePlusSix.getRHS();

        assertEquals("3.0", three.toString());
        assertEquals("+", plus.toString());
        assertEquals("6.0", six.toString());
    }

    @Test
    public void factorPrecedesTerm() {
        var code = "1 + 2 * 3;";
        var stream = new TokenStream(code);
        var parser = new BinaryExpressionParser(stream, null);

        var onePlusTwoTimesThree = (BinaryExpression)parser.parse();
        var one = (JSNumber)onePlusTwoTimesThree.getLHS();
        var plus = onePlusTwoTimesThree.getOperator();
        var twoTimesThree = (BinaryExpression)onePlusTwoTimesThree.getRHS();
        var two = (JSNumber)twoTimesThree.getLHS();
        var times = twoTimesThree.getOperator();
        var three = (JSNumber)twoTimesThree.getRHS();

        assertEquals("1.0", one.toString());
        assertEquals("+", plus.toString());
        assertEquals("2.0", two.toString());
        assertEquals("*", times.toString());
        assertEquals("3.0", three.toString());
    }

    @Test
    public void testAssignmentWithBinaryExpressionOnTheRHS() {
        var code = "value = 10 + 2;";
        var stream = new TokenStream(code);
        var parser = new BinaryExpressionParser(stream, null);
        var expression = parser.parse();
        var binaryExpression = (BinaryExpression)expression;

        var value = (Identifier)binaryExpression.getLHS();
        var equal = binaryExpression.getOperator();
        var tenPlusTwo = (BinaryExpression)binaryExpression.getRHS();

        assertEquals("value", value.toString());
        assertEquals("=", equal.toString());

        var ten = (JSNumber)tenPlusTwo.getLHS();
        var plus = tenPlusTwo.getOperator();
        var two = (JSNumber)tenPlusTwo.getRHS();

        assertEquals("10.0", ten.toString());
        assertEquals("+", plus.toString());
        assertEquals("2.0", two.toString());
    }
}
