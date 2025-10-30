## Interpreter
A toy tree-walk interpreter that can run code written in a JS-like language. It can parse simple JavaScript programs.
The long-term plan is to make it ECMA-262 compatible and once there, perhaps generate Java bytecode which can
run on the JVM.

## Building and Running the Tests

#### Clone the repository
```shell 
git clone https://github.com/devsh0/JsInterpreter.git
```

#### Run tests
```shell
cd JsInterpreter
mvn test
```

## Example Usecase
Although the interpreter isn't ready to be used as a standalone library, you can still take it for a test drive.
Here's how:

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

Also add this property to help the exec plugin find your main class. Replace `com.my.package` with your own package specifier.
```xml
<properties>
    <exec.mainClass>com.my.package.Main</exec.mainClass>
</properties>
```

3. Create a JavaScript source file in your project root and paste the following code.
```javascript
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

4. Create `Main.java` for your main class in `src/main/java` and paste the following code.

```java
public class Main {
    public static void main(String[] args) throws IOException  {
        String code = Files.readString(Paths.get("fizzbuzz.js"));
        Program program = Parser.parse(code);

        Interpreter interpreter = Interpreter.get();
        var output = interpreter.run(program);
        System.out.println(output.toString()); // "buzz".
    }
}
```

5. Run the application.
```shell
mvn exec:java
```

This should run the application and output "buzz" in stdout. If you encounter a `ClassNotFoundExecption` pointing
to `Main`, you probably need this instead:

```shell
mvn -X clean install exec:java -Dexec.mainClass="com.my.package.Main" -Dexec.classpathScope=test -e
```
