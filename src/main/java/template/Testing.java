package template;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

        var t = new template.Testing();

        var m = t.getClass().getMethod("test", (Class<?>[]) null);

        var r = (String) m.invoke(t, (Object[]) null);

        System.out.println(r);
*/

        //System.out.println(new template.TemplateClass("test").generateClass());

        System.out.println(new HtmlTemplate()
                .setTemplate(new File("test.html"))
                .render()
        );

       /* var test = "|hello |<!-- ehff -->|";
        System.out.println(test.substring(test.indexOf("<!--"),test.indexOf("-->")+2));*/


      /*  var pattern = Pattern.compile("(</script\\s*>)|(</SCRIPT\\s*>)");

        var s = "123457</script  >| efsdf";

        var m = pattern.matcher(s);
        System.out.println(m.find());
        System.out.println(s.substring(0,m.start()));*/


        //System.out.println(m.regionEnd());
        //System.out.println(m.find());
        //System.out.println(m.end());
        //System.out.println(m.start());


    }


}
