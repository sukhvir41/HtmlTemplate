package template;

public class Test {
    public static String render() {
        StringBuilder builder = new StringBuilder();
        builder.append("<html lang=\"en\">\n");
        builder.append("	<head>\n");
        builder.append("		<title>\n");
        builder.append("				Document\n");
        builder.append("		</title>\n");
        builder.append("	</head>\n");
        builder.append("	<body>\n");
        builder.append("		<h1>\n");
        builder.append("			<b>\n");
        builder.append("					this is test\n");
        builder.append("			</b>\n");
        builder.append("		</h1>\n");
        builder.append("		<a href=\"www.google.com\">\n");
        builder.append("				this ia a test link a edge case\n");
        builder.append("		</a>\n");
        builder.append("		<a>\n");
        builder.append("			<h2>\n");
        builder.append("					this is a test\n");
        builder.append("			</h2>\n");
        builder.append("		</a>\n");
        builder.append("	</body>\n");
        builder.append("</html>\n");
        return builder.toString();
    }
}
