package com.oxande.commons.oxutils;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class PHPUtilText {
    @Test
    public void unquoteTest() {
        assertEquals(null, PHPUtil.unquote(null));
        assertEquals(null, PHPUtil.unquote("'bohéme"));
        assertEquals(null, PHPUtil.unquote("bohéme"));
        assertEquals("4 garçons dans le vent", PHPUtil.unquote("\"4 garçons dans le vent\""));
        assertEquals("4 garçons dans le vent", PHPUtil.unquote("'4 garçons dans le vent'"));
        assertEquals("L'ampoule de midi", PHPUtil.unquote("\"L'ampoule de midi\""));
    }

//    @Test
//    public void replaceTest() throws IOException {
//        URL url = this.getClass().getResource("/wp-config.php");
//        InputStream inputStream = url.openStream();
//        assertNotNull(url);
//
//        Map<String, String> map = new HashMap<>();
//        map.put("DB_USER", "William Rey");
//        String result = PHPUtil.phpReplace(IOUtil.loadContents(inputStream), map);
//        // String result = String.join("\n", list);
//        assertTrue(result.contains("'DB_USER'"));
//        assertTrue(result.contains("\"William Rey\""));
//    }
}
