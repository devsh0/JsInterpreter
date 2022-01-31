package org.js;

import ast.*;
import ast.operator.BinaryOperator;
import ast.value.JSValue;
import myutils.Assertable;

public class Main implements Assertable {
    private static FunctionDeclaration defineFunctionHelper(final Identifier functionId) {
        var functionDeclaration = new FunctionDeclaration();
        var functionBody = new Block(functionDeclaration);
        var returnStatement = ReturnStatement.from(BinaryExpression.from(BinaryOperator.Plus,
                JSValue.from("one"), JSValue.from(2)));
        functionBody.append(returnStatement);
        return functionDeclaration.setId(functionId).setBody(functionBody);
    }

    public static void main(String[] args) {
        var interpreter = Interpreter.get();
        Program program = new Program();
        var programBody = new Block(program);
        Identifier mainFunctionId = Identifier.from("main");
        programBody.append(defineFunctionHelper(mainFunctionId));
        programBody.append(new CallExpression()
                .setCallee(mainFunctionId)
                .setArguments(JSValue.from(10), JSValue.from(20)));
        program.setBody(programBody);
        var value = interpreter.run(program);
        ((JSValue) value).dump();
    }
}
