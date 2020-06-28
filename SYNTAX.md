# HtmlTemplate Syntax
As name states the template syntax relies on tags and attributes. 
A lot of inspiration for the syntax was taken from [Vue.js](https://github.com/vuejs/vue) and 
[ThymeLeaf](https://github.com/thymeleaf/thymeleaf). As we go through the syntax you will see how similar the syntax is with 
the previously mentioned framework/library if you have worked with them. For the internal working of HtmlTemplate inspiration from
[Rocker Template](https://github.com/fizzed/rocker) was taken where the template converts html to a java pojo,
and the static content in the template gets loaded only into memory once and is reused for every render of the template.
HtmlTemplate is statically typed as is boiled down to java. This helps us to catch most of the errors in compile time. 
Consider this template as combination of Vue.js/Thymeleaf and Rocker Template. 

### Preamble
The template is purely based on html tags and attributes and does not use custom html tags. Depending on what attribute 
or html tag used either or any combination the entire tag, attribute or part of an attribute will be removed after the conversion.
All the custom attributes that have a value have to in double quotes(").    


### Import statements
As any java class it may have import statements and as our html template gets converted to a java class it too has 
a way to add optional import statement. There are a few rules to keep in mind when adding an import statement.
The "ht-import" attribute helps to add import statements. The attribute has to in a meta tag if not it will have a
completely different behaviour (explained in [dynamic attributes](#dynamic-attributes)).You can specify more than one 
import in value of the attribute separated by a comma as mentioned in the example below. This tag will be removed when 
converted to a class so when the template is rendered the meta will not show up. It is not necessary for the tag to be on top 
of the file but for consistency it is a good idea to put them on the op of teh file.     

```html
<html>
    <meta ht-import="java.util.List">
    <meta ht-import="java.util.Map ,java.util.Set">
    <meta ht-import='java.util.Collection'> <!-- this will not work as the value is in single quotes --> 
</html>
```

Considering only the top html, the output once rendered will be

```html
<html>
    <meta ht-import='java.util.Collection'> 
    <!-- this will not work as the value is in single quotes --> 
</html>
```

### Declaring Variables
As Html template is statically typed so with the name of the argument its type also needs to be mentioned.
"ht-variables" attribute helps to specify the arguments the template will take. The attribute expects pairs starting
with the type of the variable then the name and more than one can specified separated by a comma. Similar to 
"[ht-import](#import-statements)" the "ht-variables" attribute needs to in meta tag other it will behave differently
(refer [dynamic attributes](#dynamic-attributes)). The tag will be removed when template is converted to a java class(pojo).
The tag can be mentioned anywhere in the file.  

```html
<html>
    <meta ht-import="java.util.*" >
        
    <meta ht-variables="String, name">
    <meta ht-variables="List<String>, names, int[] ,numbers">
    <meta ht-variables='int , age'> <!-- this will not work as the value is in single quotes -->
</html>
```

Considering only the top html, the html output once rendered will be

```html
<html>
    <meta ht-variables='int , age'>
    <!-- this will not work as the value is in single quotes -->
</html>
```

### Content
Now that we have learned how to declare variables let us see how to use those variables but before we do that the template supports
two different types of contents one that gets html encoded/escaped and one that does not. 

##### HTML encoded content 
```html
<html>
    <meta ht-variables = "String, name, String, greeting">
    <h1> {{ @greeting.toUpperCase() }} {{ @name}} ! </h1>
<html>
```
The contents within "{{ }}" are html encoded.
Any valid one line java code that returns a string can be inside "{{ }}"
The variable declared in ht-variables attribute can be used by appending a "@" in front of it.

##### HTML non encoded content
```html   
<html>
    <meta ht-variables = "String, greetingHtml">
    {{{ @greetingHtml }}}
</html> 
```
The contents within "{{{  }}}" are not HTML encoded and are written as is.
If you want to insert some html then this can be used but be cautious about cross site scripting. 
Other than that the same rules of encoded content apply here. 

### Control Statements

##### If, else if and else statements
The same rules of if , else if and else statements in java apply here as they get converted to one. 
The attributes used for control flow are removed when the template is rendered.

**If statement**
```html
<html>
    <meta ht-variables = "boolean, show">
    
    <h1 ht-if="@show">
        show is true
    </h1>
</html>
```
In the above example the h1 tag will be rendered only if the condition is true. 

**Else if statement**
```html
<html>
    <meta ht-varibles="boolean show1, boolean show2 ">
    
    <h1 ht-if="@show1">
        show1 is true
    </h1>
    <h1 ht-elseIf="@show2">
        show 2 is true
    </h1>
</html>
```
**Else statement** 

```html
<html>
    <meta ht-variables="boolean,show">
    
    <h1 ht-if="@show">
        show is true
    </h1>
    <h1 ht-else>
        show is false
    </h1>
</html>
```

```html
<html>
    <meta ht-varibles="boolean show1, boolean show2 ">
    
    <h1 ht-if="@show1">
        show1 is true
    </h1>
    <h1 ht-elseIf="@show2">
        show 2 is true
    </h1>
    <h1 ht-else>
        show 1 and show 2 are false
    </h1>
</html>
```

### Dynamic Attributes
"ht-" prefix to an attribute denotes that the value of the attribute is dynamic. Any java code that returns a 
string is valid as the value if the attribute. The value will be html encoded. The prefix will be removed from the 
attribute name when the template is rendered.

```html
<html>
    <meta ht-varibles="String, className ">
    
    <h1 ht-class = "@className">
        Hello World !
    </h1>
    
    <h1  ht-class = '@className'> <!-- this will not work -->
        Hello World !
    </h1>

</html>
```

Considering only the top html, the html output once rendered will be

```html
<html> 
    <h1 class = "heading">
        Hello World !
    </h1>
    
    <h1  ht-class = '@className'> 
    <!-- this will not work -->
        Hello World !
    </h1>

</html>
```
For this example consider the value passed to the variable className is "heading".

### Include template
There may be sections in the html page that are common in all the pages eg. the header and footer of a page. 
you can put them in another file and reference them another template to include them.

```html
<html> 
        
    <meta ht-include = "header.html">
    
    <h1 class = "heading">
        Hello World !
    </h1>
    
</html>
```

Path mentioned in the "ht-include" attribute has to be relative to the path of the current file. eg: consider the top html
is the file Page1.html in the folder Pages. The header.html file should also be located in that folder. if it is not then
a relative path from the folder Pages to the file header.html should be the mentioned.
  



      