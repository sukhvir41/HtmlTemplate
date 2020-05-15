/*
 * Copyright 2020 Sukhvir Thapar
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.github.sukhvir41.template;

import com.github.sukhvir41.processors.HtmlLineProcessor;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Testing {


    public static void main(String[] args) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, IOException {

        //createClass();
        //
        //printLine();
        //System.out.println(HtStringUtils.findIndex(0,"c","\"abc\"c"));

        //printNewClass();''


    }

    private static void printNewClass() {
        var theClass = new TemplateClass("test", "test", new HtmlTemplate());

        theClass.addVariable("theClassName", "String");

        theClass.appendPlainHtml("<html>");
        theClass.addCode("if (true) {");
        theClass.incrementFunctionIndentation();
        theClass.appendPlainHtml("<head>");
        theClass.appendPlainHtml("</head>");
        theClass.decrementFunctionIndentation();
        theClass.addCode("}");
        theClass.appendPlainHtml("</html>");

        System.out.println(theClass.generateReflectionClass());


    }


    static void test(Object o) {
        System.out.println(o.getClass().getName());
    }

    static void createClass() throws IOException {

        var string = new HtmlTemplate()
                .setTemplate(new File("test.html").toPath())
                .render();

        System.out.println(string);
    }


    static void printLine() throws IOException {
        try (BufferedReader reader = Files.newBufferedReader(Paths.get("test.html"))) {
            String line;

            var proc = new HtmlLineProcessor();

            while ((line = reader.readLine()) != null) {
                //System.out.println("processing line ->" + line);
                proc.setLine(line);

                while (proc.hasNextSection()) {
                    System.out.println("|" + proc.getNextSection() + "|");
                }

                proc.carryForwardUnprocessedString();
            }

        }
    }


}
