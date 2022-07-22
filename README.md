## Interpreter
An interpreter that can recognize a JS-like language. It can parse a significant part of JavaScript but is missing
a bunch of features. I intend to finish it once I have some time off freelancing. The plan is to have the parser recognize
all the syntactical elements of ECMA-262 and once there, generate Java bytecode (or LLVM IR) based on the AST which can
be interpreter by a VM (JVM or LLVM).

## Building and Running the Tests
1. Clone the repository
``` 
$ git clone https://github.com/devsh0/JsInterpreter.git
```

2. Run tests
```
$ cd JsInterpreter
$ mvn test
```

**Note:** Ubuntu doesn't play nice with maven 3.6.x. If you encounter an error, upgrade to maven 3.8.x.
