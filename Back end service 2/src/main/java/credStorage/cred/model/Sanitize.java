package credStorage.cred.model;
import org.apache.commons.text.StringEscapeUtils;


public class Sanitize{

    public static String sanitizeInput(String input)
    {
        String openValid = "<a><abbr><address><article><aside><b><bdi><bdo><blockquote><body><button><canvas><caption><cite><code><col><colgroup><data><datalist><dd><del><details><dfn><dialog><div><dl><dt><em><fieldset><figcaption><figure><footer><form><h1><h2><h3><h4><h5><h6><head><header><html><i><iframe><ins><kbd><label><legend><li><main><map><mark><menu><meter><nav><noscript><object><ol><optgroup><option><output><p><picture><pre><progress><q><rb><rp><rt><rtc><ruby><samp><script><section><select><small><source><span><strong><sub><summary><sup><svg><table><tbody><td><template><textarea><tfoot><th><thead><time><title><tr><track><u><ul><var><video>";

        for (String openTag : openValid.split("<")) {
            if (openTag.endsWith(">")) {
                // Construct the corresponding close tag
                String closeTag = "</" + openTag.substring(0, openTag.length() - 1) + ">";
                // Find the indices of open and close tags
                int startIndex = input.indexOf("<" + openTag);
                int endIndex = input.indexOf(closeTag, startIndex);
                // If both open and close tags are found, remove the content between them
                if (startIndex != -1 && endIndex != -1) {
                    input = input.substring(0, startIndex) + input.substring(endIndex + closeTag.length());
                }
            }
        }
        input = removeSelfClosingTags(input);
       // input = StringEscapeUtils.unescapeHtml4(input);

        return input;
    }
    
	 public static String removeSelfClosingTags(String input) {
        if (input == null || input.isEmpty()) {
            System.out.println("Input string is null or empty.");
            return input;
        }

        String selfClosingTags = "<area><base><br><col><command><embed><hr><img><input><keygen><link><meta><param><source><track><wbr>";

        // Iterate over all self-closing tags
        for (String tag : selfClosingTags.split("<")) {
            if (tag.endsWith(">")) {
                // Remove self-closing tags from the input string
                input = input.replaceAll("<" + tag, "");
            }
        }
     // Escape HTML entities
        input = StringEscapeUtils.escapeHtml4(input);

        return input;
    }
}
