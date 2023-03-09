BinEd - Binary/Hexadecimal Editor - Autopsy Plugin
==================================================

Hexadecimal viewer/editor plugin module for Autopsy digital forensics platform.

Homepage: https://bined.exbin.org/autopsy-plugin  

Published as: TODO

Autopsy homepage: https://sleuthkit.org/autopsy/

Screenshot
----------

![Plugin Screenshot](images/bined-screenshot.png?raw=true)

Features
--------

  * Visualize data as numerical (hexadecimal) codes and text representation
  * Codes can be also binary, octal or decimal
  * Support for Unicode, UTF-8 and other charsets
  * Insert and overwrite edit modes
  * Searching for text / hexadecimal code with found matches highlighting
  * Support for undo/redo
  * Support for files with size up to exabytes

Compiling
---------

Java Development Kit (JDK) version 8 or later is required to build this project.

For project compiling Gradle build system is used: https://gradle.org

You can either download and install gradle or use gradlew or gradlew.bat scripts to download separate copy of gradle to perform the project build.

Build command: "gradle build nbm"

Current deployment version is built with Java 8 and Gradle 4.6. 

License
-------

Apache License, Version 2.0 - see LICENSE-2.0.txt
