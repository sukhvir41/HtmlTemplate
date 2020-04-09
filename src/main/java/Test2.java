import java.io.Writer;
import java.io.IOException;

public class Test2 extends org.ht.template.Template {
	private static final String PLAIN_HTML_0 = "\n<!DOCTYPE html>\n<html lang=\"en\">\n	<head>\n		<meta charset=\"UTF-8\">\n		<title>\n			Title\n		</title>\n	</head>\n	<body>\n		<h1>\n			THIS IS IN TEST 2 FILE\n		</h1>\n		<h1>\n			Hello world from ";
	private static final String PLAIN_HTML_1 = "\n		</h1>\n	</body>\n</html>";

	private Test2 () {}

	private String name;

	public String name() {
		return this.name;
	}

	public Test2 name(String name) {
		this.name = name;
		return this;
	}

	@Override
	public void render(Writer writer) throws IOException {
		writer.append(PLAIN_HTML_0);
		writer.append(content(() -> String.valueOf(name())));
		writer.append(PLAIN_HTML_1);

	}

	public static Test2 getInstance() {
		return new Test2();
	}
}
