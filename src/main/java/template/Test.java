package template;

public class Test {
    public static String render() {
        StringBuilder builder = new StringBuilder();
        builder.append("<!DOCTYPE html>\n");
        builder.append("<html lang=\"en\">\n");
        builder.append("	<head>\n");
        builder.append("		<title>\n");
        builder.append("			Document\n");
        builder.append("		</title>\n");
        builder.append("	</head>\n");
        builder.append("	<body>\n");
        builder.append("		<style>\n");
        builder.append("			    jnjgjdfgndfjg\n");
        builder.append("			    dkfgkdf\n");
        builder.append("		</style>\n");
        builder.append("		<h1>\n");
        builder.append("			<b>\n");
        builder.append("				this is test\n");
        builder.append("			</b>\n");
        builder.append("		</h1>\n");
        builder.append("		<style>\n");
        builder.append("			gjbfdjgjfdgdfgfgjfhg\n");
        builder.append("		</style>\n");
        builder.append("		<!-- this is a comment test  1-->\n");
        builder.append("		<a href=\"www.google.com\">\n");
        builder.append("			this ia a test link a edge case\n");
        builder.append("		</a>\n");
        builder.append("		<!-- this is alo a comment test 2  -->\n");
        builder.append("		<a>\n");
        builder.append("			<h2>\n");
        builder.append("				this is a\n");
        builder.append("				<b>\n");
        builder.append("					test\n");
        builder.append("				</b>\n");
        builder.append("				sdfasdf\n");
        builder.append("			</h2>\n");
        builder.append("		</a>\n");
        builder.append("		<!-- this is alo a comment test 3  -->\n");
        builder.append("		<h2>\n");
        builder.append("			after the comment\n");
        builder.append("		</h2>\n");
        builder.append("		<!--\n");
        builder.append("		 this is a comment block\n");
        builder.append("		 jsdnjsdfs <h3> this is comment html</h3>\n");
        builder.append("		 <a jfdjg\n");
        builder.append("		 -->\n");
        builder.append("		<h1>\n");
        builder.append("			this is after comment block\n");
        builder.append("		</h1>\n");
        builder.append("	</body>\n");
        builder.append("</html>\n");
        return builder.toString();
    }
}
