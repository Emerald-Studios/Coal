package dk.sebsa.sandbox;

import dk.sebsa.coal.graph.text.Language;

/**
 * @author sebs
 */
public class LangStatic {
    public static Language lang;

    public final String sandboxTest1 = lang.get("sandbox.test.1");
    public final String sandboxTest2 = lang.get("sandbox.test.2");

    public static LangStatic genStatic(Language language) {
        lang = language;
        return new LangStatic();
    }
}
