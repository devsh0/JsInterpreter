package org.js;

import ast.*;
import ast.operator.BinaryOperator;
import ast.value.JSValue;
import myutils.Assertable;

public class Main implements Assertable {
    private static FunctionDeclaration defineFunctionHelper(final Identifier functionId) {
        var functionDeclaration = new FunctionDeclaration().setParameters(Identifier.from("number"));
        var functionBody = new Block(functionDeclaration);
        var ifStatement = new IfStatement().setConditionExpression(BinaryExpression.from(
                BinaryOperator.Equals,
                BinaryExpression.from(BinaryOperator.Mod, Identifier.from("number"), JSValue.from(2)),
                JSValue.from(0)
        ));
        var ifBody = new Block(ifStatement);

        var innerIfStatement = new IfStatement().setConditionExpression(BinaryExpression.from(
                BinaryOperator.Equals,
                BinaryExpression.from(BinaryOperator.Mod, Identifier.from("number"), JSValue.from(4)),
                JSValue.from(0)
        ));
        var innerIfBody = new Block(innerIfStatement);

        var moreInnerIfStatement = new IfStatement().setConditionExpression(BinaryExpression.from(
                BinaryOperator.Equals,
                BinaryExpression.from(BinaryOperator.Mod, Identifier.from("number"), JSValue.from(5)),
                JSValue.from(0)
        ));
        var moreInnerIfBody = new Block(moreInnerIfStatement);
        moreInnerIfBody.append(ReturnStatement.from(JSValue.from(true)));
        moreInnerIfStatement.setBody(moreInnerIfBody);

        innerIfBody.append(moreInnerIfStatement);
        innerIfStatement.setBody(innerIfBody);
        ifBody.append(innerIfStatement);
        ifStatement.setBody(ifBody);

        var returnStatement = ReturnStatement.from(JSValue.from(false));
        functionBody.append(ifStatement).append(returnStatement);
        return functionDeclaration.setId(functionId).setBody(functionBody);
    }

    public static void main(String[] args) {
        var interpreter = Interpreter.get();
        Program program = new Program();
        var programBody = new Block(program);
        Identifier isEvenFunctionId = Identifier.from("isGoodEven");
        programBody.append(defineFunctionHelper(isEvenFunctionId));

        var ifStatement = new IfStatement().setConditionExpression(
                new CallExpression().setCallee(isEvenFunctionId).setArguments(JSValue.from(40)));
        var ifBody = new Block(ifStatement);
        ifBody.append(new ExpressionStatement().setExpression(JSValue.from(1)));
        ifStatement.setBody(ifBody);

        programBody.append(ifStatement);
        program.setBody(programBody);
        var value = interpreter.run(program);
        ((JSValue) value).dump();
    }
}
