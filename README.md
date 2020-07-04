# HtmlTemplate
HtmlTemplate is a Java Html Template as you may have guessed from the name. The template engine is memory efficient by 
loading the static parts in memory once and is statically typed with no reflection. This is achieved by converting 
the html template into a java template class. Syntax in based on Html attributes and Html tags(no custom html tag). 
Template syntax is inspired by [ThymeLeaf](https://github.com/thymeleaf/thymeleaf), [Vue.js](https://github.com/vuejs/vue) 
and implementation inspired by [Rocker Template](https://github.com/fizzed/rocker). HtmlTemplate is compatible with 
java 10+.

## Features
+ Statically typed
+ Everything is in html tags
+ No reflection
+ Memory efficient by loading static parts only once (zero copy)

## Why another Html templating engine in Java
This project was born by the frustration of using JSP(JSTL) and other templating engines. Some of the templating engines
are general purpose but none of them were good for Html templating IMO. The problem most of the faced were custom syntax 
(not tag based or attribute mixing languages) or either too verbose(JSTL) or need a servlet container to work and would 
not render well in the browser directly. These problems were solved by Thymeleaf but it did most of the things in runtime 
leading to errors could only be know during runtime basically not statically typed. HtmlTemplate is an attempt to solve 
these problems by having the syntax based html attributes and tags (when writing html stay in html), statically typed 
and memory optimization in mind by loading static parts of the template into memory once and reusing them for each render.
This is achieved by converting the html template file into a java template class.    

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

HtmlTemplate is comprised of a parser/converter that converts html file to a java template class.
For now the only way to do this is to use the command line tool. 

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


