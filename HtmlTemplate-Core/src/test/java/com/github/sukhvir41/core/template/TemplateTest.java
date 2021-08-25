package com.github.sukhvir41.core.template;

import com.github.sukhvir41.TestUtils;
import com.github.sukhvir41.core.settings.SettingOptions;
import com.github.sukhvir41.core.settings.SettingsManager;
import org.junit.Assert;
import org.junit.Test;

import java.net.URISyntaxException;
import java.nio.file.Path;
import java.util.Map;

public class TemplateTest {

    @Test
    public void testGetRootTemplate_RuntimeTemplate() throws URISyntaxException {
        Template template = TemplateFactory.getTemplate(TestUtils.getFile("SimpleTest1.html"), TemplateType.RUN_TIME, SettingsManager.load());

        Template rootTemplate = template.getRootTemplate();

        Assert.assertEquals(rootTemplate, template);
    }

    @Test
    public void testGetRootTemplate_RuntimeSubTemplate() throws URISyntaxException {
        Template template = TemplateFactory.getTemplate(TestUtils.getFile("SimpleTest1.html"), TemplateType.RUN_TIME, SettingsManager.load());

        Template runtimeSubTemplate = new RuntimeSubTemplate(TestUtils.getFile("SimpleTest1.html"), template);

        Assert.assertEquals(runtimeSubTemplate.getRootTemplate(), template);
    }

    @Test
    public void testGetRootTemplate_CompileTimeTemplate() throws URISyntaxException {
        Path file = TestUtils.getFile("SimpleTest1.html");

        Template template = TemplateFactory.getTemplate(file, TemplateType.COMPILE_TIME, SettingsManager.load(Map.of(SettingOptions.ROOT_FOLDER, file.getParent())));

        Template rootTemplate = template.getRootTemplate();

        Assert.assertEquals(rootTemplate.getRootTemplate(), template);
    }

    @Test
    public void testGetRootTemplate_CompileTimeSubTemplate() throws URISyntaxException {
        Path file = TestUtils.getFile("SimpleTest1.html");

        Template template = TemplateFactory.getTemplate(file, TemplateType.COMPILE_TIME, SettingsManager.load(Map.of(SettingOptions.ROOT_FOLDER, file.getParent())));

        Template compileTimeTemplate = new CompileTimeSubTemplate(file, template);

        Assert.assertEquals(compileTimeTemplate.getRootTemplate(), template);

    }

    @Test
    public void testEquals_Template() throws URISyntaxException {
        Path file = TestUtils.getFile("SimpleTest1.html");

        Template rootTemplate = new RuntimeTemplate(file, SettingsManager.load());

        Template subTemplate = new RuntimeSubTemplate(file, rootTemplate);

        Assert.assertNotEquals(subTemplate, rootTemplate);
    }

}
