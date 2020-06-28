# HtmlTemplate
HtmlTemplate is a preprocessor that converts html to Java. Template syntax Inspired by [ThymeLeaf](https://github.com/thymeleaf/thymeleaf), 
[Vue.js](https://github.com/vuejs/vue) and implementation inspired by [Rocker Template](https://github.com/fizzed/rocker).
HtmlTemplate is compatible with java 10+, is statically typed ,with no reflection and compiles along your project. 

## Features
+ Statically typed
+ Everything is in html tags
+ No reflection
+ Memory efficient (Maybe? needs testing)

## Syntax
Have a look at the <a href="SYNTAX.md">SYNTAX.md</a>. 

## Libraries used
+ [Apache commons lang](http://commons.apache.org/proper/commons-lang/)
+ [Apache commons text](http://commons.apache.org/proper/commons-text/)
+ [JOOR](https://github.com/jOOQ/jOOR)
+ [Mockito](https://github.com/mockito/mockito)
+ [Picocli](https://github.com/remkop/picocli)
+ [Project Lombok](https://projectlombok.org/)

## Getting Started

HtmlTemplate comprised of a parser/converter that converts html file to a java file.
For the only way to do this is use the command line tool. 

##### Create a html file
To get started lets create a html file named Hello.html which will contain the following html code.

Hello.html
```html
<html>
    <meta ht-variables="String, message">
    <body>
        Hello {{@message}}!
    </body>
</html>
```

For more information on the syntax please have a look at the [SYNTAX.md](SYNTAX.md)

Now that we have the html file lets use the cli tool to convert it to a java class

```
java -jar htmltemplatecli.jar -f Hello.html -o .
```
Now if you check the directory where the Hello.html file is located you should see a Hello.java file as well.
Move this file to your project with HtmlTemplate.jar as you dependency. 

##### Using the template
```java
import java.io.Writer;
class Main{
    static public void main(String[] args) {
        
        Writer writer = new StringWriter();
        
        Hello.getInstance()
            .message("World")
            .render(writer);
        
        System.out.println(writer.toString());
    }
}
```

##### Expected output

```html
<html>
    <body>
        Hello World!
    </body>
</html>
```


