package com.oxande.commons.oxutils;

import org.springframework.util.FileCopyUtils;

import java.io.*;
import java.nio.charset.StandardCharsets;

public class IOUtil {

//    public static List<String> loadContents(InputStream inputStream) throws IOException {
//        List<String> text = new ArrayList<>();
//        StringBuilder textBuilder = new StringBuilder();
//        try (LineNumberReader reader = new LineNumberReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))){
//            String str = null;
//            while ((str = reader.readLine()) != null) {
//                text.add(str);
//            }
//        }
//
//        return text;
//    }

    public static String loadContents(InputStream inputStream) throws IOException {
        StringBuilder buff = new StringBuilder();
        try (LineNumberReader reader = new LineNumberReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))){
            String str;
            while ((str = reader.readLine()) != null) {
                buff.append(str).append("\n");
            }
        }
        return buff.toString();
    }

    public static byte[] loadContentsAsBytes(InputStream inputStream) throws IOException {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        FileCopyUtils.copy(inputStream, output);
        return output.toByteArray();
    }
}
