package com.oxande.commons.oxutils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public final class WebUtil {
    static final Map<Character, String> ENTITIES = new HashMap<>();

    static {
        ENTITIES.put('<', "&lt;");
        ENTITIES.put('>', "&gt;");
        ENTITIES.put('&', "&amp;");
        ENTITIES.put('\n', "<br>");
        ENTITIES.put('\t', "&Tab;");
        ENTITIES.put('\u00A0', "&nbsp;");
        ENTITIES.put('"', "&quot;");

        ENTITIES.put('é', "&eacute;");
        ENTITIES.put('è', "&egrave;");
        ENTITIES.put('ë', "&euml;");
        ENTITIES.put('ê', "&ecirc;");
        ENTITIES.put('É', "&Eacute;");
        ENTITIES.put('È', "&Egrave;");
        ENTITIES.put('Ë', "&Euml;");
        ENTITIES.put('Ê', "&Ecirc;");
        ENTITIES.put('à', "&agrave;");
        ENTITIES.put('À', "&Agrave;");
        ENTITIES.put('â', "&acirc;");
        ENTITIES.put('ä', "&auml;");
        ENTITIES.put('ï', "&iuml;");
        ENTITIES.put('î', "&icirc;");
    }

    public static String makeUrl(String url, Map<String, String> params) {
        StringBuilder buf = new StringBuilder();
        buf.append(url);
        int nb = 0;
        for (Map.Entry<String, String> entry : params.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            if (value != null) {
                buf.append(nb == 0 ? '?' : '&');
                buf.append(key).append("=");
                try {
                    buf.append(URLEncoder.encode(value, StandardCharsets.ISO_8859_1.name()));
                } catch (UnsupportedEncodingException ex) {
                    throw new UnsupportedOperationException("Charset not supported!", ex);
                }
                nb++;
            }
        }
        return buf.toString();
    }

    public static String toHtml(String s) {
        return toHtml(s, ENTITIES);
    }

    /**
     * Convert the text to HTML following the specified map. Note the characters outside the ASCII
     * range are automatically converted to their unicode values.
     *
     * @param str the {@link String} to convert.
     * @param entities the entities to use. Entities can <i>any</i> replacement, including tags or any characters,
     * even empty strings.
     * @return the converted text.
     */
    public static String toHtml(String str, Map<Character, String> entities) {
        if (str == null) {
            return null;
        }
        int len = str.length();
        StringBuilder buf = new StringBuilder(len + 10);
        for (int i = 0; i < len; i++) {
            char c = str.charAt(i);
            String entity = entities.get(c);
            if (entity != null) {
                buf.append(entity);
            } else if (Character.isWhitespace(c)) {
                buf.append(' ');
            } else if ((c < 32 || c > 127) && c != '\n') {
                // To keep the data inside the ASCII limits
                buf.append("&#").append((int) c).append(';');
            } else {
                buf.append(c);
            }
        }
        return buf.toString();
    }

    public static String unhtml(String html) {
        if (html == null) return "";
        int len = html.length();
        StringBuilder buf = new StringBuilder(len);
        int i = 0;
        while (i < len) {
            char c = html.charAt(i++);
            if (c == '&') {
                int pos = html.indexOf(";", i);
                if (pos < i) return buf.toString();

                String val = html.substring(i, pos);
                if (val.charAt(0) == '#') {
                    int unicode = Integer.parseInt(val.substring(1), 16);
                    buf.append((char) unicode);
                    i = pos + 1;
                } else if (Character.isDigit(val.charAt(0))) {
                    int unicode = Integer.parseInt(val);
                    buf.append((char) unicode);
                    i = pos + 1;
                } else {
                    String entity = "&" + val + ";";
                    Character character = ENTITIES.entrySet().stream()
                            .filter(e -> e.getValue().equals(entity))
                            .map(Map.Entry::getKey)
                            .findAny()
                            .orElse('?');
                    buf.append(character);
                    i = pos + 1;
                }
            } else if (c == '<') {
                int pos = html.indexOf(">", i);
                if (pos < i) return buf.toString();
                i = pos + 1;
            } else buf.append(c);
        }
        return buf.toString();
    }

    /**
     * Generate an HTML tag.
     *
     * @param tagName the tag name
     * @param attrs the attributes
     * @return a String in HTML.
     */
    public static String tag(CharSequence tagName, Map<String, Object> attrs) {
        StringBuilder buf = new StringBuilder();
        buf.append("<").append(tagName);
        attrs.forEach((key, value) -> {
            buf.append(' ').append(key);
            if (value != null) {
                buf.append("=\"").append(toHtml(value.toString())).append("\"");
            }
        });
        buf.append('>');
        return buf.toString();
    }

    public static String tag(CharSequence tagName, Map<String, Object> attrs, String html) {
        return tag(tagName, attrs) + html + tag("/" + tagName);
    }

    public static String tag(CharSequence tagName, String html) {
        return tag(tagName) + html + tag("/" + tagName);
    }

    public static String tag(CharSequence tagName) {
        return tag(tagName, Collections.emptyMap());
    }

    public static String untagHtml(String value) {
        Assert.notNull(value, "value is mandatory");
        return value.replaceAll("<[a-zA-Z][^>]+>", "");
    }
}
