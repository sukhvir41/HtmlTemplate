package com.github.sukhvir41.core.classgenerator;

import com.github.sukhvir41.TestUtils;
import com.github.sukhvir41.core.template.Template;
import com.github.sukhvir41.tags.HtmlTag;
import org.joor.Reflect;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

import java.net.URISyntaxException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class TemplateClassGeneratorTest {


    @Test
    public void testTemplateClassGenerator_packageNameAndClassNameTest() {
        String packageName = "testPackage";
        String className = "TestClass";
        TemplateClassGenerator classGenerator = new RuntimeClassGenerator(packageName, className);

        //test packageName
        Assert.assertEquals(packageName, classGenerator.getPackageName());

        //test className
        Assert.assertEquals(className, classGenerator.getClassName());
    }

    @Test
    public void testTemplateClassGenerator_writerVariableNameTest() {
        String packageName = "testPackage";
        String className = "TestClass";

        TemplateClassGenerator classGenerator = new RuntimeClassGenerator(packageName, className);

        Pattern pattern = Pattern.compile("writer\\d{1,4}", Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(classGenerator.getWriterVariableName());

        Assert.assertTrue("Writer variable name: " + classGenerator.getWriterVariableName(), matcher.find());

    }

    @Test
    public void testTemplateClassGenerator_importStatementsTest() {
        // check default import statements
        String packageName = "testPackage";
        String className = "TestClass";

        TemplateClassGenerator classGenerator = new RuntimeClassGenerator(packageName, className);
        Assert.assertEquals(2, classGenerator.getImports().size());

        Assert.assertTrue(classGenerator.getImports().contains("java.io.Writer"));
        Assert.assertTrue(classGenerator.getImports().contains("java.io.IOException"));

        // check add imports
        classGenerator.addImport("java.util.*");
        classGenerator.addImport("java.util.*");

        Assert.assertEquals(3, classGenerator.getImports().size());
        Assert.assertTrue(classGenerator.getImports().contains("java.util.*"));

        // check if the getImports is not modifiable

        try {
            classGenerator.getImports()
                    .add("java.util.*");
            Assert.fail("getImports should be unmodifiable");
        } catch (Exception e) {
            Assert.assertTrue(true);
        }

    }

    @Test
    public void testTemplateClassGenerator_getTemplate() throws URISyntaxException {
        String packageName = "testPackage";
        String className = "TestClass";

        TemplateClassGenerator classGenerator = new RuntimeClassGenerator(packageName, className);

        Template template = Mockito.mock(Template.class);
        Mockito.when(template.getFile())
                .thenReturn(TestUtils.getFile("SimpleTest1.html"));

        // checking template is not processed
        Assert.assertFalse(classGenerator.getTemplate(TestUtils.getFile("SimpleTest1.html")).isPresent());

        Reflect.on(classGenerator)
                .field("templateDataMap")
                .call("put", template, new TemplateData());
        // checking template is processed
        Assert.assertTrue(classGenerator.getTemplate(TestUtils.getFile("SimpleTest1.html")).isPresent());
    }

    @Test
    public void testTemplateClassGenerator_addStatement() {
        String packageName = "testPackage";
        String className = "TestClass";

        TemplateClassGenerator classGenerator = new RuntimeClassGenerator(packageName, className);
        Template template = Mockito.mock(Template.class);
        classGenerator.addStatement(template, () -> "Some statement");


        String templateString = classGenerator.getTemplateDataMap()
                .get(template)
                .getRenderFunctionBody().toString();

        //increments done later should not affect the previous statement indentation
        classGenerator.getTemplateDataMap()
                .get(template)
                .incrementRenderBodyIndentation();

        Assert.assertEquals("\t\tSome statement\n",
                templateString);

    }

    @Test
    public void testTemplateClassGenerator_incrementBodyIndentation() {
        String packageName = "testPackage";
        String className = "TestClass";

        TemplateClassGenerator classGenerator = new RuntimeClassGenerator(packageName, className);
        Template template = Mockito.mock(Template.class);

        classGenerator.incrementRenderBodyIndentation(template);
        classGenerator.addStatement(template, () -> "Some statement");

        String templateString = classGenerator.getTemplateDataMap()
                .get(template)
                .getRenderFunctionBody().toString();
        Assert.assertEquals("\t\t\tSome statement\n",
                templateString);

    }

    @Test
    public void testTemplateClassGenerator_decrementBodyIndentation() {
        String packageName = "testPackage";
        String className = "TestClass";

        TemplateClassGenerator classGenerator = new RuntimeClassGenerator(packageName, className);
        Template template = Mockito.mock(Template.class);

        classGenerator.decrementRenderBodyIndentation(template);
        classGenerator.addStatement(template, () -> "Some statement");

        String templateString = classGenerator.getTemplateDataMap()
                .get(template)
                .getRenderFunctionBody().toString();
        Assert.assertEquals("\tSome statement\n",
                templateString);

    }

    @Test
    public void testTemplateClassGenerator_appendPlainHtml() {
        String packageName = "testPackage";
        String className = "TestClass";
        HtmlTag htmlTag = Mockito.mock(HtmlTag.class);
        Template template = Mockito.mock(Template.class);

        TemplateClassGenerator classGenerator = Mockito.spy(new RuntimeClassGenerator(packageName, className));

        //test basic appendPlainHtml
        classGenerator.appendPlainHtml(template, "<html>");
        classGenerator.addHtmlTag(htmlTag);
        classGenerator.appendPlainHtml(template, "<body>");


        Assert.assertEquals("<html>\\n\\t<body>\\n", classGenerator.getPlainHtmlVariables()
                .get(0).toString());

        Assert.assertEquals("\t\t" + classGenerator.getWriterVariableName() + ".append(PLAIN_HTML_0);\n", classGenerator.getTemplateDataMap().get(template).getRenderFunctionBody().toString());
    }

    @Test
    public void testTemplateClassGenerator_appendPlainHtmlWithParams() {
        String packageName = "testPackage";
        String className = "TestClass";
        HtmlTag htmlTag = Mockito.mock(HtmlTag.class);
        Template template = Mockito.mock(Template.class);

        TemplateClassGenerator classGenerator = Mockito.spy(new RuntimeClassGenerator(packageName, className));

        classGenerator.appendPlainHtml(template, "<div ", true, false);
        classGenerator.appendPlainHtml(template, "test=\"test\">", false, true);
        classGenerator.addHtmlTag(htmlTag);

        Assert.assertEquals("<div test=\\\"test\\\">\\n", classGenerator.getPlainHtmlVariables()
                .get(0).toString());

        Assert.assertEquals("\t\t" + classGenerator.getWriterVariableName() + ".append(PLAIN_HTML_0);\n", classGenerator.getTemplateDataMap().get(template).getRenderFunctionBody().toString());
    }

    @Test
    public void testTemplateClassGenerator_appendPlainHtmlIndentation() {
        String packageName = "testPackage";
        String className = "TestClass";
        HtmlTag htmlTag = Mockito.mock(HtmlTag.class);
        Template template = Mockito.mock(Template.class);

        TemplateClassGenerator classGenerator = Mockito.spy(new RuntimeClassGenerator(packageName, className));

        classGenerator.addHtmlTag(htmlTag);
        classGenerator.appendPlainHtmlIndentation(template);

        Assert.assertEquals("\\t", classGenerator.getPlainHtmlVariables()
                .get(0).toString());

        Assert.assertEquals("\t\t" + classGenerator.getWriterVariableName() + ".append(PLAIN_HTML_0);\n", classGenerator.getTemplateDataMap().get(template).getRenderFunctionBody().toString());
    }

    @Test
    public void testTemplateClassGenerator_appendPlainHtmlNewLine() {
        String packageName = "testPackage";
        String className = "TestClass";

        Template template = Mockito.mock(Template.class);

        TemplateClassGenerator classGenerator = Mockito.spy(new RuntimeClassGenerator(packageName, className));

        classGenerator.appendPlainHtmlNewLine(template);

        Assert.assertEquals("\\n", classGenerator.getPlainHtmlVariables()
                .get(0).toString());

        Assert.assertEquals("\t\t" + classGenerator.getWriterVariableName() + ".append(PLAIN_HTML_0);\n", classGenerator.getTemplateDataMap().get(template).getRenderFunctionBody().toString());
    }


    @Test
    public void testTemplateClassGenerator_addVariable() {
        String packageName = "testPackage";
        String className = "TestClass";
        String type = "int";
        String name = "age";

        Template template = Mockito.mock(Template.class);
        TemplateClassGenerator classGenerator = Mockito.spy(new RuntimeClassGenerator(packageName, className));

        classGenerator.addVariable(template, type, name);

        classGenerator.getVariables(template)
                .forEach((k, v) -> System.out.println(k + "  " + v));

        Assert.assertTrue(classGenerator.getVariables(template)
                .containsKey(name));

        Assert.assertTrue(classGenerator.getVariables(template)
                .containsValue(type));
    }

    @Test
    public void testTemplateClassGenerator_getRootTemplate() {
        String packageName = "testPackage";
        String className = "TestClass";

        Template template = Mockito.mock(Template.class);
        Template anotherTemplate = Mockito.mock(Template.class);
        TemplateClassGenerator classGenerator = Mockito.spy(new RuntimeClassGenerator(packageName, className));

        Mockito.when(template.getRootTemplate())
                .thenReturn(anotherTemplate);
        Mockito.when(anotherTemplate.getRootTemplate())
                .thenReturn(template);

        classGenerator.addVariable(template, "int", "age");
        classGenerator.addVariable(anotherTemplate, "int", "age");

        try {
            classGenerator.getRootTemplate();
            Assert.fail();
        } catch (IllegalStateException illegalStateException) {
            Assert.assertTrue(true);
        }
    }


}
