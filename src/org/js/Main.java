package org.js;

import ast.*;
import ast.operator.BinaryOperator;
import ast.value.JSValue;
import myutils.Assertable;

public class Main implements Assertable {
    private static FunctionDeclaration defineFunctionHelper(final Identifier functionId) {
        var functionDeclaration = new FunctionDeclaration().setParameters(Identifier.from("number"));
        var functionBody = new Block(functionDeclaration);
        var valueId = Identifier.from("value");
        var valueDeclaration = new VariableDeclaration().setIdentifier(valueId).setInitializer(JSValue.from(""));
        functionBody.append(valueDeclaration);

        var ifStatement = new IfStatement().setConditionExpression(BinaryExpression.from(
                BinaryOperator.Equals,
                BinaryExpression.from(BinaryOperator.Mod, Identifier.from("number"), JSValue.from(3)),
                JSValue.from(0)
        ));
        var ifBody = new Block(ifStatement);
        var assignment = new AssignmentExpression().setIdentifier(valueId);
        assignment.setExpression(BinaryExpression.from(
                BinaryOperator.Plus,
                valueId,
                JSValue.from("fizz")
        ));
        ifBody.append(assignment);
        ifStatement.setBody(ifBody);
        functionBody.append(ifStatement);

        ifStatement = new IfStatement().setConditionExpression(BinaryExpression.from(
                BinaryOperator.Equals,
                BinaryExpression.from(BinaryOperator.Mod, Identifier.from("number"), JSValue.from(5)),
                JSValue.from(0)
        ));
        ifBody = new Block(ifStatement);
        assignment = new AssignmentExpression().setIdentifier(valueId);
        assignment.setExpression(BinaryExpression.from(
                BinaryOperator.Plus,
                valueId,
                JSValue.from("buzz")
        ));
        ifBody.append(assignment);
        ifStatement.setBody(ifBody);

        functionBody.append(ifStatement).append(new ReturnStatement().setExpression(valueId));
        return (FunctionDeclaration) functionDeclaration.setId(functionId).setBody(functionBody);
    }

    public static void main(String[] args) {
        var interpreter = Interpreter.get();
        Program program = new Program();
        var programBody = new Block(program);
        Identifier fizzBuzzFun = Identifier.from("fizzBuzz");
        programBody.append(defineFunctionHelper(fizzBuzzFun));
        programBody.append(new CallExpression().setCallee(fizzBuzzFun).setArguments(JSValue.from(10)));
        program.setBody(programBody);
        var value = interpreter.run(program);
        ((JSValue) value).dump();
    }
}
