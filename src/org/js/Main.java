package org.js;

import ast.*;
import ast.operator.BinaryOperator;
import ast.operator.RelationalOperator;
import ast.value.JSValue;
import myutils.Assertable;

public class Main implements Assertable {
    private static FunctionDeclaration defineNSum(Identifier functionId) {
        var parameterN = Identifier.from("n");
        var functionDeclaration = new FunctionDeclaration().setParameters(parameterN);
        var functionBody = new Block(functionDeclaration);

        var localI = Identifier.from("i");
        var localSum = Identifier.from("sum");
        functionBody.append(new VariableDeclaration().setIdentifier(localI).setInitializer(JSValue.from(0)));
        functionBody.append(new VariableDeclaration().setIdentifier(localSum).setInitializer(JSValue.from(0)));

        var whileStatement = (new WhileStatement().setConditionExpression(new BinaryExpression()
                .setOperator(RelationalOperator.LessThan)
                .setLhs(localI)
                .setRhs(parameterN)
        ));
        var whileBody = new Block(whileStatement);
        whileBody.append(new AssignmentExpression().setDestination(localSum).setSource(
                new BinaryExpression().setOperator(BinaryOperator.Plus).setLhs(localSum).setRhs(localI)
        ));
        whileBody.append(new AssignmentExpression().setDestination(localI).setSource(
                new BinaryExpression().setOperator(BinaryOperator.Plus).setLhs(localI).setRhs(JSValue.from(1))
        ));
        whileStatement.setBody(whileBody);
        functionBody.append(whileStatement);
        functionBody.append(new ReturnStatement(functionDeclaration).setExpression(localSum));
        return (FunctionDeclaration) functionDeclaration.setId(functionId).setBody(functionBody);
    }

    private static FunctionDeclaration defineFizzBuzz(final Identifier functionId) {
        var functionDeclaration = new FunctionDeclaration().setParameters(Identifier.from("number"));
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
        return (FunctionDeclaration) functionDeclaration.setId(functionId).setBody(functionBody);
    }

    // Crazy nested if statements.
    private static FunctionDeclaration defineIsGoodEven(Identifier id) {
        var parameterNumber = Identifier.from("number");
        var functionDeclaration = new FunctionDeclaration().setId(id).setParameters(parameterNumber);
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
        return (FunctionDeclaration) functionDeclaration.setBody(functionBody);
    }

    public static void main(String[] args) {
        var interpreter = Interpreter.get();
        Program program = new Program();
        var programBody = new Block(program);
        Identifier isGoodEvenFun = Identifier.from("isGoodEven");
        programBody.append(defineIsGoodEven(isGoodEvenFun));
        programBody.append(new CallExpression().setCallee(isGoodEvenFun).setArguments(JSValue.from(60)));
        program.setBody(programBody);
        var value = interpreter.run(program);
        ((JSValue) value).dump();
    }
}
