package org.js;

import myutils.Assertable;
import parser.Parser;

import java.io.IOException;
import java.nio.file.Paths;

public class Main implements Assertable {

    public static void main(String[] args) throws IOException {
        var parser = Parser.get(Paths.get("test.js"));
        var program = parser.parse();
        System.out.println(Interpreter.get().run(program));
    }
}
