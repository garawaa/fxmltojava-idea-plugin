# fxmltojava-idea-plugin
fxml to java converter intellij plugin.
Sometimes you may meet trouble with fxml files like me. For example fxml based javafx applications are difficult to obfuscate. 
This problem can be solved by converting fxml files to java code.
For this purpose I implemented this small plugin. Currently it only supported until intellij 2020.2 version. For latest version of intellij I recommend to use [fxml2javaconverter](https://github.com/garawaa/fxml2javaconverter) app.
You also can freely use it. 
The application also supports command line argument as input file. So you can use it as external tool in intellij idea. First you need to convert the application to your os executable app. If your os is windows you can use launch4j for converting to exe file.
## Download
[link](https://raw.githubusercontent.com/garawaa/fxmltojava-idea-plugin/master/fxmltojava-idea-plugin.zip)
Install it File->Settings->Plugins->Install from disk
## Usage
Once you generated java class, create a new class and extend it from generated java class. Use your created class insteed of fxml load.

## Built tools:
Intellij IDE Community 2020.1.4
JDK 8

Thank you.
