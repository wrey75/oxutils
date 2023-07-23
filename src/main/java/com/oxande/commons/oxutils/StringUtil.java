package com.oxande.commons.oxutils;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.text.Normalizer;
import java.util.*;

public final class StringUtil {
    public final static char[] HEXA = "0123456789abcdef".toCharArray();
    public final static String EMPTY_SLUG = "-";

    /**
     * Convert to hexadecimal.
     *
     * @param v the value
     * @param len the maximum length
     * @return the hexadecimal value
     */
    public static String toHex(int v, int len) {
        char[] buf = new char[len];
        while (len-- > 0) {
            buf[len] = HEXA[v & 0xf];
            v = v >> 4;
        }
        return new String(buf);
    }

    public static String toHex(byte[] buf) {
        return toHex(buf, 0, buf.length);
    }

    public static String toHex(byte[] data, int off, int len) {
        Assert.isTrue(len >= 0, "The length must be positive");
        Assert.isTrue(off >= 0, "The offset must be positive");
        Assert.isTrue(off + len <= data.length, "The offset + the length is outside the data array");
        char[] buf = new char[len * 2];
        int i = 0;
        while (off < len) {
            buf[i++] = HEXA[(data[off] & 0xf0) >> 4];
            buf[i++] = HEXA[data[off] & 0x0f];
            off++;
        }
        return new String(buf);
    }

    public static String cstring(CharSequence text) {
        if (text == null) {
            return null;
        }
        int len = text.length();
        StringBuilder buf = new StringBuilder(len + 12);
        buf.append('\"');
        for (int i = 0; i < len; i++) {
            char c = text.charAt(i);
            switch (c) {
                case '\t':
                    buf.append("\\t");
                    break;
                case '\b':
                    buf.append("\\b");
                    break;
                case '\n':
                    buf.append("\\n");
                    break;
                case '\r':
                    buf.append("\\r");
                    break;
                case '\\':
                    buf.append("\\\\");
                    break;
                default:
                    if (c < 32 || c > 127) {
                        buf.append("\\u").append(toHex(c, 4));
                    } else {
                        buf.append(c);
                    }
            }
        }
        buf.append('\"');
        return buf.toString();
    }

    public static String q(Object o) {
        if (o == null) return "<null>";
        String str = o.toString();
        int len = str.length();
        StringBuilder buf = new StringBuilder(len + 12);
        buf.append('\"');
        for (int i = 0; i < len; i++) {
            char c = str.charAt(i);
            switch (c) {
                case '\t':
                    buf.append("\\t");
                    break;
                case '\b':
                    buf.append("\\b");
                    break;
                case '\n':
                    buf.append("\\n");
                    break;
                case '\r':
                    buf.append("\\r");
                    break;
                case '\\':
                    buf.append("\\\\");
                    break;
                default:
                    if (c < 32) {
                        buf.append("\\u").append(toHex(c, 4));
                    } else {
                        buf.append(c);
                    }
            }
        }
        buf.append('\"');
        return buf.toString();
    }

    /**
     * Generates a string of the requested length made of random characters from
     * the array of characters provided. For passwords, we advice to use
     * {@link StringUtil#generatePassword(int)} instead.
     *
     * @param length the length of the resulting string.
     * @param acceptedChars the possible characters.
     * @return a randomized string.
     */
    public static String generateRandomString(int length, char[] acceptedChars) {
        Random rand = new Random();
        StringBuilder buf = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            int n = rand.nextInt(acceptedChars.length);
            buf.append(acceptedChars[n]);
        }
        return buf.toString();
    }

    /**
     * Generate a password of the specified length. The passord is made of lowercase letters
     * and digits in a random order. Note we use the {@link SecureRandom} class rather than
     * a classic random.
     *
     * @param length the length of the password
     * @return a password made of ASCII characters (letters and digits)
     */
    public static String generatePassword(int length) {
        char[] CHARS = "qwertzuopkhgfdsayxcvbnm023456789".toCharArray();
        SecureRandom random = new SecureRandom();
        byte[] bytes = new byte[length];
        random.nextBytes(bytes);
        StringBuilder buf = new StringBuilder(length);
        while (length-- > 0) {
            buf.append(CHARS[(bytes[length] + 128) % CHARS.length]);
        }
        return buf.toString();
    }


    public static String copy(CharSequence value, int times) {
        StringBuilder buf = new StringBuilder();
        for (int i = 0; i < times; i++) {
            buf.append(value);
        }
        return buf.toString();
    }

    /**
     * Convert a sequence of text into an UTF8 input stream.
     *
     * @param text the original text
     * @return the finite stream.
     */
    public static InputStream asUTF8Stream(CharSequence text) {
        return new ByteArrayInputStream(text.toString().getBytes(StandardCharsets.UTF_8));
    }

    public static String removeAccents(CharSequence input) {
        // Convert input string to decomposed Unicode (NFD) so that the
        // diacritical marks used in many European scripts (such as the
        // "C WITH CIRCUMFLEX" → ĉ) become separate characters.
        // Also use compatibility decomposition (K) so that characters,
        // that have the exact same meaning as one or more other
        // characters (such as "㎏" → "kg" or "ﾋ" → "ヒ"), match when
        // comparing them.
        String normalized = Normalizer.normalize(input, Normalizer.Form.NFKD);

        StringBuilder result = new StringBuilder();

        normalized.codePoints()
                .filter(character -> {
                    // Only process characters that are not combining Unicode
                    // characters. This way all the decomposed diacritical marks
                    // (and some other not-that-important modifiers), that were
                    // part of the original string or produced by the NFKD
                    // normalizer above, disappear.
                    switch (Character.getType(character)) {
                        case Character.NON_SPACING_MARK:
                        case Character.COMBINING_SPACING_MARK:
                            // Some combining character found
                            return false;
                        default:
                            return true;
                    }
                })
                .forEach(result::appendCodePoint);
        return result.toString();
    }


    public static String trimPunctuation(CharSequence input) {
        if (input == null) return null;
        int len0 = 0;
        int len1 = input.length();
        while (len0 < len1 && !Character.isJavaIdentifierPart(input.charAt(len0))) len0++;
        while (len1 > len0 && !Character.isJavaIdentifierPart(input.charAt(len1 - 1))) len1--;
        return input.subSequence(len0, len1).toString();
    }


    public static String q(CharSequence input) {
        if (input == null) return "<null>";
        int len = input.length();

        StringBuilder buf = new StringBuilder(len + 10);
        buf.append('"');
        for (int i = 0; i < len; i++) {
            char c = input.charAt(i);
            switch (c) {
                case '\b':
                    buf.append("\\b");
                    break;
                case '\f':
                    buf.append("\\f");
                    break;
                case '\n':
                    buf.append("\\n");
                    break;
                case '\r':
                    buf.append("\\r");
                    break;
                case '\t':
                    buf.append("\\t");
                    break;
                case '\"':
                    buf.append("\\\"");
                    break;
                case 'é':
                case 'è':
                case 'à':
                case 'ü':
                case 'ô':
                case 'ö':
                case 'ù':
                case 'ñ':
                case '€':
                    // Normal characters which are expected to be printable.
                    buf.append(c);
                    break;
                default:
                    if (c < 32 || c > 128) {
                        // Consider special characters
                        String s = Integer.toHexString(c);
                        while (s.length() < 4) s = "0" + s;
                        buf.append("\\u").append(s);
                    } else {
                        buf.append(c);
                    }
                    break;
            }
        }
        buf.append('"');
        return buf.toString();
    }

    /**
     * Return the stacktrace of the exception.
     *
     * @param t the exception
     * @return the stacktrace or null if the exception is null.
     */
    public static String stacktrace(Throwable t) {
        if (t == null) {
            // No stacktrace to return
            return null;
        }
        StringWriter writer = new StringWriter();
        t.printStackTrace(new PrintWriter(writer));
        return writer.getBuffer().toString();
    }

    /**
     * Convert a sequence of text into a MD5 hexadecimal value. The text is converted first
     * to an UTF8 stream.
     *
     * @param text the text to sign
     * @return the MD5 signin.
     */
    public static String md5(CharSequence text) {
        byte[] bytesOfMessage = text.toString().getBytes(StandardCharsets.UTF_8);
        return md5(bytesOfMessage);
    }


    public static UUID uuid(String text) {
        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            byte[] thedigest = md5.digest(text.getBytes(StandardCharsets.UTF_8));
            return UUID.nameUUIDFromBytes(thedigest);
        } catch (NoSuchAlgorithmException ex) {
            throw new UnsupportedOperationException("MD5 algorithm is not available in this JDK!!!", ex);
        }
    }

    public static String md5(byte[] buffer) {
        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            byte[] thedigest = md5.digest(buffer);
            return toHex(thedigest);
        } catch (NoSuchAlgorithmException ex) {
            throw new UnsupportedOperationException("MD5 algorithm is not available in this JDK!!!", ex);
        }
    }

    public static String sha1(CharSequence text) {
        try {
            byte[] bytesOfMessage = text.toString().getBytes(StandardCharsets.UTF_8);
            MessageDigest md5 = MessageDigest.getInstance("SHA-1");
            byte[] thedigest = md5.digest(bytesOfMessage);
            return toHex(thedigest);
        } catch (NoSuchAlgorithmException ex) {
            throw new UnsupportedOperationException("MD5 algorithm is not available in this JDK!!!", ex);
        }
    }

    public static String inputStreamToString(InputStream inputStream) throws IOException {
        StringBuilder sb = new StringBuilder(100);
        BufferedReader r = new BufferedReader(new InputStreamReader(inputStream));
        String str;
        while ((str = r.readLine()) != null) {
            sb.append(str);
        }
        return sb.toString();
    }

    /**
     * Remove all the accents of a String
     * by substituing the characters by their basic counterparts.
     */
    public static String unaccent(String src) {
        return Normalizer
                .normalize(src, Normalizer.Form.NFD)
                .replaceAll("[^\\p{ASCII}]", "");
    }

    /**
     * Convert a text in a slug (compatible with WordPress). When the calculated slug is empty, a simple dash
     * is returned. Zjis is the only case where a dash is found as a first or last character of the slug.
     *
     * @param seq any text.
     * @return a text made of dashes, lower ASCII letters and digits only.
     */
    public static String slug(String seq) {
        String input = unaccent(StringUtils.trimToEmpty(seq));
        int len = input.length();
        StringBuilder buf = new StringBuilder(len);
        for (int i = 0; i < len; i++) {
            char c = Character.toLowerCase(input.charAt(i));
            if ((c >= '0' && c < '9') || (c >= 'a' && c <= 'z')) {
                buf.append(c);
            } else {
                buf.append(' '); // consecutive spaces will be replaced by unique dash.
            }
        }
        String ret = buf.toString().trim().replaceAll("[ ]+", "-");
        return ret.length() == 0 ? EMPTY_SLUG : ret;
    }

    /**
     * Checks if 2 strings are the same (or do not differ so much).
     *
     * @param v1 the first string
     * @param v2 the second string
     * @return true if identical.
     */
    public static boolean same(String v1, String v2) {
        String slug1 = slug(v1);
        String slug2 = slug(v2);
        return StringUtils.equalsIgnoreCase(slug1, slug2);
    }


    /**
     * Convert a version as a long value to express the version.
     *
     * @param version the version major.minor.patch
     * @return the version as a lomg (0 if null or blank)
     */
    public static long versionAsLong(String version) {
        long value = 0;
        if (!StringUtils.isBlank(version)) {
            String[] array = version.replaceFirst("^[vV]", "").split("\\.");
            int bloc = 0;
            for (String part : array) {
                if (bloc < 3 && part.matches("[0-9]*")) {
                    int v = Math.min(9999, NumberUtils.toInt(part));
                    value = value * 10000 + v;
                    bloc++;
                } else {
                    return value;
                }
            }
            // if some parts are missing...
            while (bloc++ < 3) {
                value *= 10000;
            }
        }
        return value;
    }

    public static String nVal(int size, String singular, String multiple) {
        if (size < 2) {
            return singular.replace("%d", String.valueOf(size));
        } else {
            return multiple.replace("%d", String.valueOf(size));
        }
    }


    private static class KeyValue implements Map.Entry<String, String> {
        private final String key;
        private final String value;

        public KeyValue(String key, String value) {
            this.key = key;
            this.value = value;
        }

        @Override
        public String getKey() {
            return this.key;
        }

        @Override
        public String getValue() {
            return this.value;
        }

        @Override
        public String setValue(String value) {
            throw new UnsupportedOperationException();
        }
    }

    public static Map.Entry<String, String> split(String input, char key) {
        Assert.notNull(input, "The input is mandatory");
        int pos = input.indexOf(key);
        if (pos < 0) {
            return new KeyValue(input.trim(), "");
        } else {
            return new KeyValue(input.substring(0, pos).trim(), input.substring(pos + 1).trim());
        }
    }

    /**
     * Remove the last slash if necessary.
     *
     * @param s the string to analyse.
     * @return the string without the ending slash.
     */
    public static String untrailingslashit(String s) {
        if (s != null) {
            if (s.endsWith("/") || s.endsWith("\\")) {
                return s.substring(0, s.length() - 1);
            }
        }
        return s;
    }

    /**
     * Strip the ".." links inside a directory (or a filename) to avoid any piracy.
     *
     * @param str the directory
     * @return the fixed directory
     */
    public static String securedPath(final String str) {
        Assert.notNull(str, "A valid path is expected.");
        final String fixed = str.replaceAll("/+", "/");
        List<String> dirs = new ArrayList<>(Arrays.asList(fixed.split("/")));

        boolean modified = false;
        int i = 0;
        while (i < dirs.size()) {
            String dir = dirs.get(i);
            if (StringUtils.isEmpty(dir) || dir.equalsIgnoreCase(".")) {
                dirs.remove(i);
                modified = true;
            } else if (dir.equalsIgnoreCase("..")) {
                dirs.remove(i);
                modified = true;
                if (i > 0) dirs.remove(i - 1);
            } else {
                i++;
            }
        }
        if (modified) {
            StringBuilder buf = new StringBuilder();
            if (fixed.startsWith("/")) {
                buf.append("/");
            }
            dirs.forEach(d -> buf.append(d).append("/"));
            if (!fixed.endsWith("/")) {
                buf.deleteCharAt(buf.length() - 1);
            }
            return buf.toString();
        }
        return fixed;
    }

    /**
     * Remove the specified character if found at beginning or the end of the string.
     *
     * @param value the value
     * @param c the character
     * @return the trimmed value
     */
    public static String trim(String value, char c) {
        Assert.notNull(value, "value");
        int len = value.length();
        int st = 0;
        char[] val = value.toCharArray();    /* avoid getfield opcode */

        while ((st < len) && (val[st] == c)) {
            st++;
        }
        while ((st < len) && (val[len - 1] == c)) {
            len--;
        }
        return ((st > 0) || (len < value.length())) ? value.substring(st, len) : value;
    }
}
