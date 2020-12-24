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

package com.github.sukhvir41;

import org.joor.Reflect;

import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;

public class TestUtils {


    public static String strip(String s) {
        return s.replace(" ", "")
                .replace("\n", "")
                .replace("\t", "");
    }

    public static Path getFile(String fileName) throws URISyntaxException {
        return Paths.get(TestUtils.class.getClassLoader().getResource(fileName).toURI());
    }

    public static Reflect getTestReflectClass() {
        return Reflect.compile("TestTemplate", "" +
                "import java.io.Writer;\n" +
                "public class TestTemplate extends com.github.sukhvir41.template.HtTemplate {\n" +
                "\n" +
                "        public static TestTemplate getInstance() {\n" +
                "            return new TestTemplate();\n" +
                "        }\n" +
                "\n" +
                "        private TestTemplate() {\n" +
                "        }\n" +
                "\n" +
                "        private String name;\n" +
                "\n" +
                "        private String name() {\n" +
                "            return this.name;\n" +
                "        }\n" +
                "\n" +
                "        public TestTemplate name(String name) {\n" +
                "            this.name = name;\n" +
                "            return this;\n" +
                "        }\n" +
                "\n" +
                "        @Override\n" +
                "        protected void renderImpl(Writer writer) {\n" +
                "            try {\n" +
                "                writer.append(\"Greetings \");\n" +
                "                writer.append(name());\n" +
                "            } catch (Exception e) {\n" +
                "                throw new RuntimeException(e);\n" +
                "            }\n" +
                "        }\n" +
                "    }");
    }


}
