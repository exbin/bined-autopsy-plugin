BinEd - Binary/Hex Editor - Autopsy Plugin
==========================================

Hex viewer/editor plugin module for Autopsy digital forensics platform.

Homepage: https://bined.exbin.org/autopsy-plugin  

Published as: https://github.com/sleuthkit/autopsy_addon_modules/tree/master/ContentViewerModules/BinEd_Binary_Viewer

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

Building is currently broken as it requires dependencies deployed in local Maven repository from various repositories:

You can try to run following commands. Start at parent directory to "bined" repo directory.

    git clone https://github.com/exbin/exbin-auxiliary-java.git
    cd exbin-auxiliary-java
    gradlew build publish
    cd ..
    git clone https://github.com/exbin/bined-lib-java.git
    cd bined-lib-java
    gradlew build publish
    cd ..
    git clone https://github.com/exbin/jaguif.git
    cd jaguif
    gradlew build publish
    cd .. 
    git clone https://github.com/exbin/bined.git
    cd bined
    gradlew build publish
    cd .. 

License
-------

Apache License, Version 2.0 - see LICENSE-2.0.txt
