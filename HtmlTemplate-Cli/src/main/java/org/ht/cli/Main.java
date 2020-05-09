package org.ht.cli;

import picocli.CommandLine;

public final class Main {


    //todo: have to write test cases for cli inputs
    public static void main(String[] args) {
        Settings settings = CommandLine.populateCommand(new Settings(), args);
        if (settings.isUsageHelpRequested()) {
            CommandLine.usage(new Settings(), System.out);
            return;
        }
        settings.setLoggingLevel();
        App app = new App(settings);
        app.createHtmlTemplateClass();

    }
}
