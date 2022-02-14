package test;

import ast.Program;
import org.js.Interpreter;
import org.junit.jupiter.api.Test;
import parser.Parser;

import static org.junit.jupiter.api.Assertions.assertEquals;

class MainTest {
    private static final Interpreter interpreter = Interpreter.get();

    private Program constructFizzBuzz(int input) {
        var code = "function fizzbuzz(input) {\n" +
                "    let value = \"\";\n" +
                "    if (input % 3 == 0) {\n" +
                "        value += \"fizz\";\n" +
                "    }\n" +
                "    if (input % 5 == 0) {\n" +
                "        value += \"buzz\";\n" +
                "    }\n" +
                "    return value;\n" +
                "}\n" +
                "\n" +
                "fizzbuzz(" + input + ");";
        return (Program) Parser.get(code).parse();
    }

    @Test
    public void testFizzBuzz() {
        var fizzBuzz = constructFizzBuzz(2);
        assertEquals("", interpreter.run(fizzBuzz).toString());

        fizzBuzz = constructFizzBuzz(3);
        assertEquals("fizz", interpreter.run(fizzBuzz).toString());

        fizzBuzz = constructFizzBuzz(10);
        assertEquals("buzz", interpreter.run(fizzBuzz).toString());

        fizzBuzz = constructFizzBuzz(30);
        assertEquals("fizzbuzz", interpreter.run(fizzBuzz).toString());
    }

    private Program constructIsGoodEven(int input) {
        var code = "function isGoodEven(input) {\n" +
                "    if (input % 5 == 0) {\n" +
                "        if (input % 3 == 0) {\n" +
                "            if (input % 2 == 0) {\n" +
                "                return true;\n" +
                "            }\n" +
                "        }\n" +
                "    }\n" +
                "    return false;\n" +
                "}\n" +
                "\n" +
                "isGoodEven(" + input + ");";
        return (Program) Parser.get(code).parse();
    }

    @Test
    public void testIsGoodEven() {
        var isGoodEven = constructIsGoodEven(5);
        assertEquals("false", interpreter.run(isGoodEven).toString());

        isGoodEven = constructIsGoodEven(6);
        assertEquals("false", interpreter.run(isGoodEven).toString());

        isGoodEven = constructIsGoodEven(4);
        assertEquals("false", interpreter.run(isGoodEven).toString());

        isGoodEven = constructIsGoodEven(30);
        assertEquals("true", interpreter.run(isGoodEven).toString());
    }

    private Program constructFibonacci(int input) {
        var code = "function fibonacci(num) {\n" +
                "    if (num < 2) {\n" +
                "        return num;\n" +
                "    } else {\n" +
                "        return fibonacci(num - 1) + fibonacci(num - 2);\n" +
                "    }\n" +
                "}\n" +
                "\n" +
                "fibonacci(" + input + ");";
        return (Program) Parser.get(code).parse();
    }

    @Test
    public void testFibonacci() {
        var fibonacci = constructFibonacci(10);
        assertEquals("55.0", interpreter.run(fibonacci).toString());

        fibonacci = constructFibonacci(0);
        assertEquals("0.0", interpreter.run(fibonacci).toString());

        fibonacci = constructFibonacci(2);
        assertEquals("1.0", interpreter.run(fibonacci).toString());
    }

    private Program constructGreatestFactor(int input) {
        var code = "function greatestFactor(input) {\n" +
                "    let current = 1;\n" +
                "    let lastGreatest = 1;\n" +
                "    let stopPoint = input / 2;\n" +
                "    while (current <= stopPoint) {\n" +
                "        if ((input % current) == 0) {\n" +
                "            lastGreatest = current;\n" +
                "        }\n" +
                "        current += 1;\n" +
                "    }\n" +
                "    return lastGreatest;\n" +
                "}\n" +
                "\n" +
                "greatestFactor(" + input + ");";
        return (Program) Parser.get(code).parse();
    }

    @Test
    public void testGreatestFactor() {
        var greatestFactor = constructGreatestFactor(343);
        assertEquals("49.0", interpreter.run(greatestFactor).toString());
    }

    private Program constructWhileWithBreak() {
        var code = "function whileWithBreak() {\n" +
                "    let number = 3;\n" +
                "    while (true) {\n" +
                "        number += 10;\n" +
                "        if (number >= 100) {\n" +
                "            break;\n" +
                "        }\n" +
                "    }\n" +
                "    return number;\n" +
                "}\n" +
                "\n" +
                "whileWithBreak();";
        return (Program) Parser.get(code).parse();
    }

    @Test
    public void testWhileWithBreak() {
        var program = constructWhileWithBreak();
        assertEquals("103.0", interpreter.run(program).toString());
    }

    private Program constructWhileWithContinue() {
        var code = "function whileWithContinue() {\n" +
                "    let accumulated = 0;\n" +
                "    while(true) {\n" +
                "        accumulated += 3;\n" +
                "        if (accumulated % 17 != 0) {\n" +
                "            continue;\n" +
                "        }\n" +
                "        break;\n" +
                "    }\n" +
                "    return accumulated;\n" +
                "}\n" +
                "whileWithContinue();";
        return (Program) Parser.get(code).parse();
    }

    @Test
    public void testWhileWithContinue() {
        var program = constructWhileWithContinue();
        assertEquals("51.0", interpreter.run(program).toString());
    }

    private Program constructSingleStatementInIfBlock() {
        var code = "let number = 27;\n" +
                "let status = \"\";\n" +
                "if (number % 2 == 0)\n" +
                "    status = \"even\";\n" +
                "else status = \"odd\";\n" +
                "status;";
        return (Program) Parser.get(code).parse();
    }

    @Test
    public void testSingleStatementInIfBlock() {
        var program = constructSingleStatementInIfBlock();
        assertEquals("odd", interpreter.run(program).toString());
    }

    private Program constructSingleStatementInWhileBlock() {
        var code = "let number = 2;\n" +
                "while (number < 200)\n" +
                "    number += 20;\n" +
                "number;";
        return (Program) Parser.get(code).parse();
    }

    @Test
    public void testSingleStatementInWhileBlock() {
        var program = constructSingleStatementInWhileBlock();
        assertEquals("202.0", interpreter.run(program).toString());
    }

    private Program constructBreakLabeledWhile() {
        var code = "let i = 2;\n" +
                "top:\n" +
                "    while (i < 5) {\n" +
                "        while (i < 100) {\n" +
                "            i *= i;\n" +
                "            if (i % 4 == 0)\n" +
                "                break top;\n" +
                "        }\n" +
                "    }\n" +
                "\n" +
                "i;";
        return (Program) Parser.get(code).parse();
    }

    @Test
    public void testBreakLabeledWhile() {
        var program = constructBreakLabeledWhile();
        assertEquals("4.0", interpreter.run(program).toString());
    }

    private Program constructContinueLabeledWhile() {
        var code = "let i = 2;\n" +
                "top:\n" +
                "    while (i < 5) {\n" +
                "        while (i < 100) {\n" +
                "            i *= i;\n" +
                "            if (i % 4 == 0)\n" +
                "                continue top;\n" +
                "        }\n" +
                "    }\n" +
                "\n" +
                "i;";
        return (Program) Parser.get(code).parse();
    }

    @Test
    public void testContinueLabeledWhile() {
        var program = constructContinueLabeledWhile();
        assertEquals("16.0", interpreter.run(program).toString());
    }

    private Program constructForLoop() {
        var code = "let accumulator = 0;\n" +
                "for (let i = 0; i < 100; i+=1)\n" +
                "    accumulator += i;\n" +
                "accumulator;";
        return (Program) Parser.get(code).parse();
    }

    @Test
    public void testForLoop() {
        var program = constructForLoop();
        assertEquals("4950.0", interpreter.run(program).toString());
    }

    private Program constructForLoopInitializerShadowsParentScopeVariable() {
        var code = "let i = 10;\n" +
                "\n" +
                "function sum() {\n" +
                "    let accumulator = 0;\n" +
                "    for (let i = 0; i < 10; i += 1)\n" +
                "        accumulator += i;\n" +
                "    return accumulator;\n" +
                "}\n" +
                "\n" +
                "let accumulator = 0;\n" +
                "for (let i = 0; i < 10; i += 1)\n" +
                "    accumulator += i;\n" +
                "\n" +
                "sum() + accumulator;";
        return (Program) Parser.get(code).parse();
    }

    @Test
    public void testForLoopInitializerShadowsParentScopeVariable() {
        var program = constructForLoopInitializerShadowsParentScopeVariable();
        assertEquals("90.0", interpreter.run(program).toString());
    }

    private Program constructForLoopInitializerIsNotVariableDeclaration() {
        var code = "let i = 0;\n" +
                "let accumulator = 0;\n" +
                "for (i = 10; i < 100; i += 1)\n" +
                "    accumulator += i;\n" +
                "accumulator;";
        return (Program) Parser.get(code).parse();
    }

    @Test
    public void testForLoopInitializerIsNotVariableDeclaration() {
        var program = constructForLoopInitializerIsNotVariableDeclaration();
        assertEquals("4905.0", interpreter.run(program).toString());
    }

    private Program constructForLoopInitializerIsEmpty() {
        var code = "let i = 0;\n" +
                "let accumulator = 0;\n" +
                "for (; i < 100; i += 1)\n" +
                "    accumulator += i;\n" +
                "accumulator;";
        return (Program) Parser.get(code).parse();
    }

    @Test
    void testForLoopInitializerIsEmpty() {
        var program = constructForLoopInitializerIsEmpty();
        assertEquals("4950.0", interpreter.run(program).toString());
    }

    private Program constructForLoopConditionExpressionIsEmpty() {
        var code = "let accumulator = 0;\n" +
                "for (let i = 0; ;i += 1) {\n" +
                "    if (i > 100)\n" +
                "        break;\n" +
                "    accumulator += i;\n" +
                "}\n" +
                "accumulator;";
        return (Program) Parser.get(code).parse();
    }

    @Test
    void testForLoopConditionExpressionIsEmpty() {
        var program = constructForLoopConditionExpressionIsEmpty();
        assertEquals("5050.0", interpreter.run(program).toString());
    }

    private Program constructForLoopUpdateExpressionIsEmpty() {
        var code = "let accumulator = 0;\n" +
                "for (let i = 0; i < 100;) {\n" +
                "    accumulator += i;\n" +
                "    i += 1;\n" +
                "}\n" +
                "accumulator;";
        return (Program) Parser.get(code).parse();
    }

    @Test
    void testForLoopUpdateExpressionIsEmpty() {
        var program = constructForLoopUpdateExpressionIsEmpty();
        assertEquals("4950.0", interpreter.run(program).toString());
    }

    private Program constructForLoopAllHeaderExpressionsAreEmpty() {
        var code = "let accumulator = 0;\n" +
                "let i = 0;\n" +
                "for (;;) {\n" +
                "    if (accumulator > 10)\n" +
                "        accumulator += accumulator;\n" +
                "    else accumulator += i;\n" +
                "    i += 1;\n" +
                "    if (accumulator > 2000)\n" +
                "        break;\n" +
                "}\n" +
                "accumulator;";
        return (Program) Parser.get(code).parse();
    }

    @Test
    void testForLoopAllHeaderExpressionsAreEmpty() {
        var program = constructForLoopAllHeaderExpressionsAreEmpty();
        assertEquals("3840.0", interpreter.run(program).toString());
    }

    private Program constructLogicalAnd() {
        var code = "let number = 10;\n" +
                "let isPositiveAndEven = (number > 0) && (number % 2 == 0);\n" +
                "isPositiveAndEven;";
        return (Program) Parser.get(code).parse();
    }

    // FIXME: This test does not cover all the cases.
    @Test
    void testLogicalAnd() {
        var program = constructLogicalAnd();
        assertEquals("true", interpreter.run(program).toString());
    }

    private Program constructLogicalOr() {
        var code = "let number = 10 - 20;\n" +
                "let isPositiveOrEven = (number > 0) || (number % 2 == 0);\n" +
                "isPositiveOrEven;";
        return (Program) Parser.get(code).parse();
    }

    // FIXME: This test does not cover all the cases.
    @Test
    public void testLogicalOr() {
        var program = constructLogicalOr();
        assertEquals("true", interpreter.run(program).toString());
    }
}
