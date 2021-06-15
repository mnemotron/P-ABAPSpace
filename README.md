# ABAPSpace
ABAP Namespace Refactoring

## Features
+ <b>GENERAL</b><br>
  + simple namespace refactoring
  + add a specific supplement to the new object name (i.e. project ID and/or package ID etc.)

+ <b>GUI/CONFIGURATION</b><br>
  + the refactoring is configurable via XML preset files
  + found object names can be manually changed before refactoring is performed

+ <b>CHECKS</b><br>
  + the maximum object name length can be checked (if the extended object naming policy is used)
  
+ <b>EXTENDED OBJECT POLICY</b><br>
  + classes, interfaces, exception classes, packages
  + more to come ....
  
## Get Started
[Read the manual](https://github.com/mnemotron/P-ABAPSpace/blob/master/abapspace.doc/manual/abapspace_manual.pdf)

## Build
The following projects can be build with [Gradle](https://gradle.org) (v4.6) and Java JDK (v8)

+ abapspace.core
+ abapspace.gui (uses the abapspace.core library)

Command line:
```bash
gradle build
```
  
## Basis for Refactoring
Use the abapGit client and its created object files as basis for the refactoring.

URL: [abapGit](https://github.com/larshp/abapGit)
