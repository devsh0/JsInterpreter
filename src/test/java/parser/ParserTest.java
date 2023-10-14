package parser;

import org.js.Interpreter;
import org.junit.jupiter.api.Test;

public class ParserTest {
    @Test
    void canExecuteJS() {
        var interpreter = Interpreter.get();
        var source = "let value = 10;\n" +
                "\n" +
                "function wrapper() {\n" +
                "    let prevValue = value;\n" +
                "    let value = 20;\n" +
                "    prevValue + value;\n" +
                "}\n" +
                "\n" +
                "wrapper();";
        var program = Parser.parse(source);
        var result = interpreter.run(program);
        System.out.println(result.toString());
    }
}
