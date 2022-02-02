package test.ast;

import ast.*;
import ast.operator.BinaryOperator;
import ast.operator.RelationalOperator;
import ast.value.JSBoolean;
import ast.value.JSString;
import ast.value.JSValue;
import org.js.Interpreter;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class IfStatementTest {
    private static final Interpreter interpreter = Interpreter.get();

    /**
     * function fizzBuzz(number)
     * {
     * let value = "";
     * if (number % 3 == 0)
     * value = value + "fizz";
     * if (number % 5 == 0)
     * value = value +"buzz";
     * return value;
     * }
     * fizzBuzz(input);
     */
    private String getFizzBuzzOutput(double input) {
        var fizzBuzzFun = Identifier.from("fizzBuzz");
        var functionDeclaration = new FunctionDeclaration()
                .setIdentifier(fizzBuzzFun)
                .setParameters(Identifier.from("number"));

        var functionBody = new Block(functionDeclaration);
        var valueId = Identifier.from("value");
        var valueDeclaration = new VariableDeclaration().setIdentifier(valueId).setInitializer(JSValue.from(""));
        functionBody.append(valueDeclaration);

        var ifStatement = new IfStatement().setConditionExpression(BinaryExpression.from(
                RelationalOperator.Equals,
                BinaryExpression.from(BinaryOperator.Mod, Identifier.from("number"), JSValue.from(3)),
                JSValue.from(0)
        ));
        var ifBody = new Block(ifStatement);
        var assignment = new AssignmentExpression().setDestination(valueId);
        assignment.setSource(BinaryExpression.from(
                BinaryOperator.Plus,
                valueId,
                JSValue.from("fizz")
        ));
        ifBody.append(assignment);
        ifStatement.setBody(ifBody);
        functionBody.append(ifStatement);

        ifStatement = new IfStatement().setConditionExpression(BinaryExpression.from(
                RelationalOperator.Equals,
                BinaryExpression.from(BinaryOperator.Mod, Identifier.from("number"), JSValue.from(5)),
                JSValue.from(0)
        ));
        ifBody = new Block(ifStatement);
        assignment = new AssignmentExpression().setDestination(valueId);
        assignment.setSource(BinaryExpression.from(
                BinaryOperator.Plus,
                valueId,
                JSValue.from("buzz")
        ));
        ifBody.append(assignment);
        ifStatement.setBody(ifBody);
        functionBody.append(ifStatement).append(new ReturnStatement(functionDeclaration).setExpression(valueId));
        functionDeclaration.setBody(functionBody);

        var program = new Program();
        var programBody = new Block(program);
        programBody.append(functionDeclaration);
        programBody.append(new CallExpression().setCallee(fizzBuzzFun).setArguments(JSValue.from(input)));
        program.setBody(programBody);

        var retVal = interpreter.run(program);
        assertTrue(retVal instanceof JSString);
        var jsString = (JSString) retVal;
        return (String) jsString.getValue();
    }

    /**
     * function nestedIfHelper(number) {
     * if (number % 2 == 0)
     * if (number % 3 == 0)
     * if (number % 5 == 0)
     * return true;
     * return false;
     * }
     * nestedIfHelper(input);
     */
    private boolean getNestedIfHelperOutput(double input) {
        var program = new Program();
        var programBody = new Block(program);

        var nestedIfHelperFun = Identifier.from("nestedIfHelper");
        var parameterNumber = Identifier.from("number");
        var functionDeclaration = new FunctionDeclaration().setIdentifier(nestedIfHelperFun).setParameters(parameterNumber);
        var functionBody = new Block(functionDeclaration);

        var ifStatement = new IfStatement().setConditionExpression(BinaryExpression.from(
                RelationalOperator.Equals,
                BinaryExpression.from(BinaryOperator.Mod, parameterNumber, JSValue.from(2)),
                JSValue.from(0)
        ));

        var innerIfStatement = new IfStatement().setConditionExpression(BinaryExpression.from(
                RelationalOperator.Equals,
                BinaryExpression.from(BinaryOperator.Mod, parameterNumber, JSValue.from(3)),
                JSValue.from(0)
        ));

        var innermostIfStatement = new IfStatement().setConditionExpression(BinaryExpression.from(
                RelationalOperator.Equals,
                BinaryExpression.from(BinaryOperator.Mod, parameterNumber, JSValue.from(5)),
                JSValue.from(0)
        ));

        var ifBody = new Block(ifStatement);
        ifBody.append(innerIfStatement);
        ifStatement.setBody(ifBody);

        var innerIfBody = new Block(innerIfStatement);
        innerIfBody.append(innermostIfStatement);
        innerIfStatement.setBody(innerIfBody);

        var innermostIfBody = new Block(innermostIfStatement);
        innermostIfBody.append(new ReturnStatement(functionDeclaration).setExpression(JSValue.from(true)));
        innermostIfStatement.setBody(innermostIfBody);
        functionBody.append(ifStatement).append(new ReturnStatement(functionDeclaration).setExpression(JSValue.from(false)));
        functionDeclaration.setBody(functionBody);

        programBody.append(functionDeclaration);
        programBody.append(new CallExpression().setCallee(nestedIfHelperFun).setArguments(JSValue.from(input)));

        var retVal = interpreter.run(program.setBody(programBody));
        assertTrue(retVal instanceof JSBoolean);
        return (Boolean) (((JSBoolean) retVal).getValue());
    }

    @Test
    public void testFizzBuzz() {
        assertEquals("", getFizzBuzzOutput(2));
        assertEquals("fizz", getFizzBuzzOutput(12));
        assertEquals("buzz", getFizzBuzzOutput(20));
        assertEquals("fizzbuzz", getFizzBuzzOutput(30));
    }

    @Test
    public void testNestedIf() {
        assertFalse(getNestedIfHelperOutput(2));
        assertFalse(getNestedIfHelperOutput(3));
        assertFalse(getNestedIfHelperOutput(25));

        assertFalse(getNestedIfHelperOutput(6));
        assertFalse(getNestedIfHelperOutput(15));
        assertFalse(getNestedIfHelperOutput(10));

        assertTrue(getNestedIfHelperOutput(30));
    }
}
