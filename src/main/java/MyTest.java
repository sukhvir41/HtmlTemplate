import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;

public class MyTest {

    public static void main(String[] args) throws IOException {

        Writer w = new StringWriter();

        Test2.getInstance()
                .name("sukhvir")
                .render(w);

        System.out.println(w.toString());

    }
}
