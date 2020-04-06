package views;

import java.io.IOException;
import com.fizzed.rocker.ForIterator;
import com.fizzed.rocker.RenderingException;
import com.fizzed.rocker.RockerContent;
import com.fizzed.rocker.RockerOutput;
import com.fizzed.rocker.runtime.DefaultRockerTemplate;
import com.fizzed.rocker.runtime.PlainTextUnloadedClassLoader;

/*
 * Auto generated code to render template views/index.rocker.html
 * Do not edit this file. Changes will eventually be overwritten by Rocker parser!
 */
@SuppressWarnings("unused")
public class index extends com.fizzed.rocker.runtime.DefaultRockerModel {

    static public com.fizzed.rocker.ContentType getContentType() { return com.fizzed.rocker.ContentType.HTML; }
    static public String getTemplateName() { return "index.rocker.html"; }
    static public String getTemplatePackageName() { return "views"; }
    static public String getHeaderHash() { return "-113994851"; }
    static public long getModifiedAt() { return 1586154806069L; }
    static public String[] getArgumentNames() { return new String[] { "message", "items" }; }

    // argument @ [1:2]
    private String message;
    // argument @ [1:2]
    private int[] items;

    public index message(String message) {
        this.message = message;
        return this;
    }

    public String message() {
        return this.message;
    }

    public index items(int[] items) {
        this.items = items;
        return this;
    }

    public int[] items() {
        return this.items;
    }

    static public index template(String message, int[] items) {
        return new index()
            .message(message)
            .items(items);
    }

    @Override
    protected DefaultRockerTemplate buildTemplate() throws RenderingException {
        // optimized for convenience (runtime auto reloading enabled if rocker.reloading=true)
        return com.fizzed.rocker.runtime.RockerRuntime.getInstance().getBootstrap().template(this.getClass(), this);
    }

    static public class Template extends com.fizzed.rocker.runtime.DefaultRockerTemplate {

        // <h1>Hello 
        static private final byte[] PLAIN_TEXT_0_0;
        // !</h1>\n\n
        static private final byte[] PLAIN_TEXT_1_0;
        // : 
        static private final byte[] PLAIN_TEXT_2_0;
        // \n
        static private final byte[] PLAIN_TEXT_3_0;

        static {
            PlainTextUnloadedClassLoader loader = PlainTextUnloadedClassLoader.tryLoad(index.class.getClassLoader(), index.class.getName() + "$PlainText", "UTF-8");
            PLAIN_TEXT_0_0 = loader.tryGet("PLAIN_TEXT_0_0");
            PLAIN_TEXT_1_0 = loader.tryGet("PLAIN_TEXT_1_0");
            PLAIN_TEXT_2_0 = loader.tryGet("PLAIN_TEXT_2_0");
            PLAIN_TEXT_3_0 = loader.tryGet("PLAIN_TEXT_3_0");
        }

        // argument @ [1:2]
        protected final String message;
        // argument @ [1:2]
        protected final int[] items;

        public Template(index model) {
            super(model);
            __internal.setCharset("UTF-8");
            __internal.setContentType(getContentType());
            __internal.setTemplateName(getTemplateName());
            __internal.setTemplatePackageName(getTemplatePackageName());
            this.message = model.message();
            this.items = model.items();
        }

        @Override
        protected void __doRender() throws IOException, RenderingException {
            // ValueClosureBegin @ [3:1]
            __internal.aboutToExecutePosInTemplate(3, 1);
            __internal.renderValue(views.main.template("Home").__body(() -> {
                // PlainText @ [3:34]
                __internal.aboutToExecutePosInTemplate(3, 34);
                __internal.writeValue(PLAIN_TEXT_0_0);
                // ValueExpression @ [4:11]
                __internal.aboutToExecutePosInTemplate(4, 11);
                __internal.renderValue(message, false);
                // PlainText @ [4:19]
                __internal.aboutToExecutePosInTemplate(4, 19);
                __internal.writeValue(PLAIN_TEXT_1_0);
                // ForBlockBegin @ [6:1]
                __internal.aboutToExecutePosInTemplate(6, 1);
                try {
                    com.fizzed.rocker.runtime.Java8Iterator.forEach(items, (i,item) -> {
                        try {
                            // ValueExpression @ [7:1]
                            __internal.aboutToExecutePosInTemplate(7, 1);
                            __internal.renderValue(i.index(), false);
                            // PlainText @ [7:11]
                            __internal.aboutToExecutePosInTemplate(7, 11);
                            __internal.writeValue(PLAIN_TEXT_2_0);
                            // ValueExpression @ [7:13]
                            __internal.aboutToExecutePosInTemplate(7, 13);
                            __internal.renderValue(item, false);
                            // PlainText @ [7:18]
                            __internal.aboutToExecutePosInTemplate(7, 18);
                            __internal.writeValue(PLAIN_TEXT_3_0);
                            // ForBlockEnd @ [6:1]
                            __internal.aboutToExecutePosInTemplate(6, 1);
                        } catch (com.fizzed.rocker.runtime.ContinueException e) {
                            // support for continuing for loops
                        }
                    }); // for end @ [6:1]
                } catch (com.fizzed.rocker.runtime.BreakException e) {
                    // support for breaking for loops
                }
                // PlainText @ [8:2]
                __internal.aboutToExecutePosInTemplate(8, 2);
                __internal.writeValue(PLAIN_TEXT_3_0);
                // ValueClosureEnd @ [3:1]
                __internal.aboutToExecutePosInTemplate(3, 1);
            }), false); // value closure end @ [3:1]
        }
    }

    private static class PlainText {

        static private final String PLAIN_TEXT_0_0 = "<h1>Hello ";
        static private final String PLAIN_TEXT_1_0 = "!</h1>\n\n";
        static private final String PLAIN_TEXT_2_0 = ": ";
        static private final String PLAIN_TEXT_3_0 = "\n";

    }

}
