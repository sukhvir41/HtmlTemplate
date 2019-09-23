import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.util.stream.Stream;

public class Testing {


    public static void main(String[] args) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
       /* var a = "<html>" +
                "   <a>" +
                "       <h1> this is a test </h1><br/>" +
                "   </a>" +
                "</html>";
        //var parts = a.split("<(\\S+\\s+)|(/\\s+\\S+)>", -1);
        var parts = a.split(">", -1);

        Stream.of(parts)
                .forEach(System.out::println);*/

       /*var test = "this is a test </h1>";

        System.out.println(test.substring(test.indexOf('<')));*/

/*

        var t = new Testing();

        var m = t.getClass().getMethod("test", (Class<?>[]) null);

        var r = (String) m.invoke(t, (Object[]) null);

        System.out.println(r);
*/

        //System.out.println(new TemplateClass("test").generateClass());


        System.out.println(new HtmlTemplate()
                .setTemplate(new File("test.html"))
                .render()
        );

    }


    public String test() {
        return "aadasdasd";
    }
}
