package com.github.sukhvir41.core.classgenerator;

import org.junit.Assert;
import org.junit.Test;

public class TemplateDataTest {

    @Test
    public void testAppendToBody() {

        TemplateData templateData = new TemplateData();
        templateData.appendToBody(() -> "Test statement");

        Assert.assertEquals("\t\tTest statement\n", templateData.getRenderFunctionBody()
                .toString());
    }

    @Test
    public void testGetRenderFunctionBody() {
        try {
            TemplateData templateData = new TemplateData();
            templateData.getRenderFunctionBody();
            Assert.assertTrue(true);
            templateData.getRenderFunctionBody(); // this statement should fail.
            Assert.fail("render body should fail if called the second time");
        } catch (Exception e) {
            Assert.assertTrue(true);
        }
    }

    @Test
    public void testIncrementIndentation() {
        TemplateData templateData = new TemplateData();
        templateData.incrementRenderBodyIndentation();
        templateData.appendToBody(() -> "test statement");

        Assert.assertEquals("\t\t\ttest statement\n", templateData.getRenderFunctionBody()
                .toString());
    }

    @Test
    public void testDecrementIndentation() {
        TemplateData templateData = new TemplateData();
        templateData.decrementRenderBodyIndentation();
        templateData.appendToBody(() -> "test statement");

        Assert.assertEquals("\ttest statement\n", templateData.getRenderFunctionBody()
                .toString());
    }

    @Test
    public void testAddVariables() {
        TemplateData templateData = new TemplateData();
        templateData.addVariable("int", "count");

        Assert.assertEquals(1, templateData.getVariables().size());

        try {
            templateData.addVariable("String", "count");
            Assert.fail("should not allow to override variable type");
            templateData.getVariables()
                    .put("String", "name");
            Assert.fail("should not be able to modify variables from getter");
        } catch (Exception e) {
            Assert.assertTrue(true);
        }

    }

}
