package org.ht.template;

import java.util.*;
import java.io.Writer;
import java.io.IOException;

public class ForEachTest extends org.ht.template.HtTemplate {
    private static final String PLAIN_HTML_0 = "<!DOCTYPE html>\n<html lang=\"en\">\n	<head>\n		<meta charset=\"UTF-8\">\n		<title>\n			Title\n		</title>\n	</head>\n	<body>\n		<h1>\n			looping names\n		</h1>\n";
    private static final String PLAIN_HTML_1 = "		<h1>\n			";
    private static final String PLAIN_HTML_2 = "\n		</h1>\n";
    private static final String PLAIN_HTML_3 = "		<h1>\n			looping names with index\n		</h1>\n";
    private static final String PLAIN_HTML_4 = "		<h1>\n			";
    private static final String PLAIN_HTML_5 = ",";
    private static final String PLAIN_HTML_6 = "\n		</h1>\n";
    private static final String PLAIN_HTML_7 = "		<h1>\n			looping ages\n		</h1>\n";
    private static final String PLAIN_HTML_8 = "		<h1>\n			";
    private static final String PLAIN_HTML_9 = "\n		</h1>\n";
    private static final String PLAIN_HTML_10 = "		<h1>\n			looping ages with index\n		</h1>\n";
    private static final String PLAIN_HTML_11 = "		<h1>\n			";
    private static final String PLAIN_HTML_12 = ",";
    private static final String PLAIN_HTML_13 = "\n		</h1>\n";
    private static final String PLAIN_HTML_14 = "		<h1>\n			looping counter\n		</h1>\n";
    private static final String PLAIN_HTML_15 = "		<h1>\n			";
    private static final String PLAIN_HTML_16 = "\n		</h1>\n";
    private static final String PLAIN_HTML_17 = "		<h1>\n			looping counter with index\n		</h1>\n";
    private static final String PLAIN_HTML_18 = "		<h1>\n			";
    private static final String PLAIN_HTML_19 = ",";
    private static final String PLAIN_HTML_20 = "\n		</h1>\n";
    private static final String PLAIN_HTML_21 = "	</body>\n</html>\n";

    private ForEachTest () {}

    private String[] names;

    public String[] names() {
        return this.names;
    }

    public ForEachTest names(String[] names) {
        this.names = names;
        return this;
    }

    private int[] counters;

    public int[] counters() {
        return this.counters;
    }

    public ForEachTest counters(int[] counters) {
        this.counters = counters;
        return this;
    }

    private List<Integer> ages;

    public List<Integer> ages() {
        return this.ages;
    }

    public ForEachTest ages(List<Integer> ages) {
        this.ages = ages;
        return this;
    }

    @Override
    public void render(Writer writer) throws IOException {
        writer.append(PLAIN_HTML_0);
        forEach(names(), (name) -> {
            writer.append(PLAIN_HTML_1);
            writer.append(content(() -> String.valueOf(name)));
            writer.append(PLAIN_HTML_2);
        });
        writer.append(PLAIN_HTML_3);
        forEach(names(), (index, name) -> {
            writer.append(PLAIN_HTML_4);
            writer.append(content(() -> String.valueOf(name)));
            writer.append(PLAIN_HTML_5);
            writer.append(content(() -> String.valueOf(index)));
            writer.append(PLAIN_HTML_6);
        });
        writer.append(PLAIN_HTML_7);
        forEach(ages, (age) -> {
            writer.append(PLAIN_HTML_8);
            writer.append(content(() -> String.valueOf(age)));
            writer.append(PLAIN_HTML_9);
        });
        writer.append(PLAIN_HTML_10);
        forEach(ages, (index, age) -> {
            writer.append(PLAIN_HTML_11);
            writer.append(content(() -> String.valueOf(age)));
            writer.append(PLAIN_HTML_12);
            writer.append(content(() -> String.valueOf(index)));
            writer.append(PLAIN_HTML_13);
        });
        writer.append(PLAIN_HTML_14);
        forEach(counters, (counter) -> {
            writer.append(PLAIN_HTML_15);
            writer.append(content(() -> String.valueOf(counter)));
            writer.append(PLAIN_HTML_16);
        });
        writer.append(PLAIN_HTML_17);
        forEach(counters, (index, counter) -> {
            writer.append(PLAIN_HTML_18);
            writer.append(content(() -> String.valueOf(counter)));
            writer.append(PLAIN_HTML_19);
            writer.append(content(() -> String.valueOf(index)));
            writer.append(PLAIN_HTML_20);
        });
        writer.append(PLAIN_HTML_21);

    }

    public static ForEachTest getInstance() {
        return new ForEachTest();
    }
}