package com.oxande.commons.oxutils;

public class PHPUtil {

    public static String quote(CharSequence sequence) {
        if (sequence == null) return "null";
        int len = sequence.length();
        StringBuilder buf = new StringBuilder(len + 10);
        buf.append("\"");
        for (int i = 0; i < len; i++) {
            char c = sequence.charAt(i);
            switch (c) {
                case '\n': //	linefeed (LF or 0x0A (10) in ASCII)
                    buf.append("\\n");
                    break;
                case '\r': //	carriage return (CR or 0x0D (13) in ASCII)
                    buf.append("\\r");
                    break;
                case '\t': //	horizontal tab (HT or 0x09 (9) in ASCII)
                    buf.append("\\t");
                    break;
                case 0x0B: //	vertical tab (VT or 0x0B (11) in ASCII) (since PHP 5.2.5)
                    buf.append("\\v");
                    break;
                case 0x1B: //	escape (ESC or 0x1B (27) in ASCII) (since PHP 5.4.4)
                    buf.append("\\e");
                    break;
                case '\f': // form feed (FF or 0x0C (12) in ASCII) (since PHP 5.2.5)
                    buf.append("\\f");
                    break;
                case '\\': // backslash
                    buf.append("\\\\");
                    break;
                case '$': //	dollar sign
                    buf.append("\\$");
                    break;
                case '\"':  //	double-quote
                    buf.append("\\\"");
                    break;
                default:
                    // the sequence of characters matching the regular expression is a Unicode codepoint,
                    // which will be output to the string as that codepoint's UTF-8 representation
                    if(c > 31 && c < 127){
                        buf.append(c);
                    } else {
                        buf.append("\\u").append(StringUtil.toHex(c, 4));
                    }
                    break;
            }
        }
        buf.append("\"");
        return buf.toString();
    }


    /**
     * Unquoting a string from PHP.
     *
     * @param sequence the string.
     * @return an unquoted string.
     */
    public static String unquote(CharSequence sequence) {
        if (sequence == null) return null;
        String source = sequence.toString().trim();
        int len = source.length();

        if (len > 1) {
            if (source.startsWith("'")) {
                if (!source.endsWith("'")) return null;
                StringBuilder buf = new StringBuilder();
                for (int i = 1; i < len - 1; i++) {
                    char c = source.charAt(i);
                    buf.append(c);
                }
                return buf.toString();
            } else if (source.startsWith("\"")) {
                if (!source.endsWith("\"")) return null;
                StringBuilder buf = new StringBuilder();
                for (int i = 1; i < len - 1; i++) {
                    char c = source.charAt(i);
                    if (c == '\\') {
                        c = source.charAt(++i);
                        switch (c) {
                            case 'b':
                                buf.append("\b");
                                break;
                            case 't':
                                buf.append("\t");
                                break;
                            case 'r':
                                buf.append("\r");
                                break;
                            case 'n':
                                buf.append("\n");
                                break;
                            case '\\':
                                buf.append("\\");
                                break;
                            case 'f':
                                buf.append("\f");
                                break;
                            case 'u': {
                                String hexa = source.substring(i + 1, i + 5);
                                int unicode = Integer.parseInt(hexa, 16);
                                i += 4;
                                buf.append((char) unicode);
                            }
                            case 'x': {
                                String hexa = source.substring(i + 1, i + 3);
                                int ascii = Integer.parseInt(hexa, 16);
                                i += 2;
                                buf.append((char) ascii);
                                break;
                            }
                        }
                    }
                    else {
                        buf.append(c);
                    }
                }
                return buf.toString();
            }

        }
        return null;
    }

//    public static String phpReplace(String code, Map<String, String> changes) {
//        StringBuilder buf = new StringBuilder();
//        Pattern pattern = Pattern.compile("define\\((.+),(.+)\\);(.*)");
//        Arrays.asList(code.split("\n")).stream()
//                .map(line -> {
//                    String withoutSpaces = line.replaceAll("\\s", "");
//                    Matcher matcher = pattern.matcher(withoutSpaces);
//                    if (matcher.matches() && matcher.groupCount() > 1) {
//                        String var = unquote(matcher.group(1));
//                        String endOfLine = (matcher.groupCount() > 2 ? matcher.group(3) : "");
//                        if (var != null && changes.containsKey(var)) {
//                            String value = quote(changes.get(var));
//                            return String.format("define( '%s', %s );%s", var, value, endOfLine);
//                        }
//                    }
//                    return line; // Do not change the code!
//                })
//                .forEach(line -> buf.append(line).append("\n") );
//        return buf.toString();
//    }
}
