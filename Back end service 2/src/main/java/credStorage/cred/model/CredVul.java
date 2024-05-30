package credStorage.cred.model;


import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CredVul {

    private static final Pattern HTML_JS_TAG_PATTERN = Pattern.compile("<\\s*(?:script|\\w+).*?>", Pattern.CASE_INSENSITIVE);
    private static final Pattern JS_EVENT_PATTERN = Pattern.compile("on(?:load|click|mouseover|\\w+)", Pattern.CASE_INSENSITIVE);

    public static boolean containsHtmlOrJsTag(String input) {
        Matcher matcher = HTML_JS_TAG_PATTERN.matcher(input);
        return matcher.find();
    }

    public static boolean containsJsEvent(String input) {
        Matcher matcher = JS_EVENT_PATTERN.matcher(input);
        return matcher.find();
    }

    public static String sanitizeInput(String input) {
        return input.replaceAll("<", "&lt;").replaceAll(">", "&gt;");
    }
}
