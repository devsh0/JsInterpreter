## Interpreter
An interpreter that can recognize a JS-like language. It can parse very simple JavaScript programs but is still missing
a bunch of features. I intend to finish it once I have some time off freelancing. The plan is to have the parser recognize
all the syntactical elements of ECMA-262 and once there, generate Java bytecode (or LLVM IR) based on the AST which can
be interpreted by a VM (JVM or LLVM).

## Building and Running the Tests
Building and running the tests is a two-stage process.

#### Clone the repository
```shell 
git clone https://github.com/devsh0/JsInterpreter.git
```

#### Run tests
```shell
cd JsInterpreter
mvn test
```
**Note:** Ubuntu doesn't play nice with maven 3.6.x. If you encounter an error, upgrade to maven 3.8.x.

## Example Usecase
Although the interpreter isn't ready to be used as a standalone library, you can still take it for a test drive. Steps to
do that are pretty straight-forward:

1. Install the library to your local maven repository.
```shell
cd JsInterpreter
mvn install
```

2. Create new Maven project and add the following dependency in `pom.xml`.
```xml
<dependency>
    <groupId>org.devsh0</groupId>
    <artifactId>JsInterpreter</artifactId>
    <version>0.1</version>
</dependency>
```

Also add this property to help the exec plugin find your main class.
```xml
<properties>
    <exec.mainClass>Main</exec.mainClass>
</properties>
```

3. Create a JavaScript source file in your project root.
```javascript
// fizzBuzz.js
function fizzbuzz(input) {
    let value = "";
    if (input % 3 == 0)
        value += "fizz";
    if (input % 5 == 0)
        value += "buzz";
    return value;
}
// By default, the last expression is fed back to Java as output.
fizzbuzz(10);
```

4. Create `Main.java` for your main class in `src/java`.

```java
// Main.java
public class Main {
    public static void main(String[] args) {
        String code = Files.readString(Paths.get("fizzbuzz.js"));
        Program program = (Program)Parser.get(code).parse();
        
        Interpreter interpreter = Interpreter.get();
        var output = interpreter.run(program);
        System.out.println(output.toString()); // "buzz".
        return 0;
    }
}
```

5. Run the application.
```shell
mvn exec:java
```

This should run the application and print "buzz" somewhere in stdout.
