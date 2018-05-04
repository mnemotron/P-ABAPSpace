# ABAPSpace
ABAP Namespace Refactoring

## Features
+ <b>GENERAL</b><br>
  + simple namespace refactoring
  + add a specific supplement to the new object name (i.e. project ID and/or package ID etc.)

+ <b>CONFIGURATION</b><br>
  + the refactoring is configurable via a XML preset file

+ <b>CHECKS</b><br>
  + the maximum object name length can be checked (if the extended object naming policy is used)
  
+ <b>OBJECTS</b><br>
  + classes, interfaces
  + more to come ....
  
## Get Started
[Read the manual](https://github.com/SAPAssets/ABAPSpace/blob/master/abapspace.doc/manual/abapspace_manual.pdf)

## Build
The following projects can be build with [Gradle](https://gradle.org):

+ abapspace.core
+ abapspace.gui (uses the abapspace.core library)

Command line:
```bash
gradle build
```
  
## Basis for Refactoring
Use the abapGit client and its created object files as basis for the refactoring.

URL: [abapGit](https://github.com/larshp/abapGit)
