# CFH Java Coding Standards

# File Structure & Formatting
Here you'll find everything you'll need to know about the expected structure of your source file, and its formatting.

## Source File
All source files must follow a specific naming convention, and structure.

### Source File Naming
All source files should be named(case-sensitive) after the top-level class they contain.

### Source File Structure
All source files should be organized similarly to provide an easier experience to fellow developers.

The common structure, in order, is as follows:

- License information
- Package statement
- Import Statements
- Class Javadoc comment
- Only one top-level class

## Indentation & Line Wrapping
You should use a unit of indentation of two spaces.

### Length
Lines longer than 80 characters should be avoided, and wrapped when present.

### Wrapping
When you exceed the suggested length specified above for an expression you must break it
into multiple lines.


When to break:

- After commas
- Before operators


Indentation for breaks:

- Each new line for an expression that must be broken should be indented four spaces
from the start of the expression.

## Comments
Comments are formatted in a specific way to improve readability, and help guide developers to writing cleaning
code that requires less comments.

### General
Some general rules to follow for comments, no matter the type.
- Never have any one line in a comment be of length greater than 80
- Never use "leetspeak", or anything of the same nature, keep comments classy.
- Be straight forward and to the point.

### Inline Comments
Inline comments are meant to be used when you only require one to two lines for describing a class, method, and/or variable.

When to use inline comments:
- When one to two line comments are sufficient documentation for what you're describing.
- Inline comments should always use the "//" comment declaration characters.

