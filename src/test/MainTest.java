package test;

import ast.Program;
import org.js.Interpreter;
import org.junit.jupiter.api.Test;
import parser.Parser;

import static org.junit.jupiter.api.Assertions.*;

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
}
