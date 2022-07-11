## Interpreter
An interpreter built out of shear boredom. Initially the plan was to write a tree-walk JS engine, but then I just kept adding
language constructs that made sense at the time and now I am not even sure what does the parser recognize. Pretty sure it's
a close cousin of Javascript though. It's highly WIP and is missing a lot of features. I will come back to it when I have
enough time to take another look.

## Build
Neither Maven, nor Gradle...we do it the old way. Just clone the project, add Junit5 manually to your classpath and run
the tests in `MainTest.java`, a mixed-bag of 20 or so tests that kept on growing as I added new language features.
