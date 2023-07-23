package com.oxande.commons.oxutils;

import org.junit.Assert;
import org.junit.Test;

import java.util.Map;

import static com.oxande.commons.oxutils.StringUtil.generatePassword;
import static com.oxande.commons.oxutils.StringUtil.q;
import static com.oxande.commons.oxutils.StringUtil.removeAccents;
import static com.oxande.commons.oxutils.StringUtil.slug;
import static com.oxande.commons.oxutils.StringUtil.toHex;
import static com.oxande.commons.oxutils.StringUtil.trimPunctuation;
import static com.oxande.commons.oxutils.StringUtil.unaccent;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class StringUtilTest {

    private static final String accents = "È,É,Ê,Ë,Û,Ù,Ï,Î,À,Â,Ô,è,é,ê,ë,û,ù,ï,î,à,â,ô,Ç,ç,Ã,ã,Õ,õ";
    private static final String expected = "E,E,E,E,U,U,I,I,A,A,O,e,e,e,e,u,u,i,i,a,a,o,C,c,A,a,O,o";

    private static final String accents2 = "çÇáéíóúýÁÉÍÓÚÝàèìòùÀÈÌÒÙãõñäëïöüÿÄËÏÖÜÃÕÑâêîôûÂÊÎÔÛ";
    private static final String expected2 = "cCaeiouyAEIOUYaeiouAEIOUaonaeiouyAEIOUAONaeiouAEIOU";

    private static final String accents3 = "Gisele Bündchen da Conceição e Silva foi batizada assim em homenagem à sua conterrânea de Horizontina, RS.";
    private static final String expected3 = "Gisele Bundchen da Conceicao e Silva foi batizada assim em homenagem a sua conterranea de Horizontina, RS.";

    private static final String accents4 = "/Users/rponte/arquivos-portalfcm/Eletron/Atualização_Diária-1.23.40.exe";
    private static final String expected4 = "/Users/rponte/arquivos-portalfcm/Eletron/Atualizacao_Diaria-1.23.40.exe";

    @Test
    public void replacingAllAccents() {
        assertEquals(expected, unaccent(accents));
        assertEquals(expected2, unaccent(accents2));
        assertEquals(expected3, unaccent(accents3));
        assertEquals(expected4, unaccent(accents4));
    }

    @Test
    public void quoteTest() {
        assertEquals("<null>", q(null));
        assertEquals("\"\"", q(""));
        assertEquals("\" \"", q(" "));
        assertEquals("\"\\t\"", q("\t"));
        assertEquals("\"Joe \\\"Battling\\\" Joe\"", q("Joe \"Battling\" Joe"));
        assertEquals("\"Une école de niñas àl'la Campagne\"", q("Une école de niñas àl'la Campagne"));
    }

    @Test
    public void generatePasswordTest() {
        String pwd = generatePassword(10);
        Assert.assertEquals(10, pwd.length());
    }

    @Test
    public void toHexTest() {
        assertEquals("00000000", toHex(0, 8));
        assertEquals("000", toHex(0, 3));
        assertEquals("010", toHex(16, 3));
        assertEquals("000", toHex(0, 3));
        assertEquals("123456", toHex(0x123456, 6));
        assertEquals("789abc", toHex(0x789abc, 6));
        assertEquals("d1eaff", toHex(0xd1eaff, 6));

        // Complement à 2
        assertEquals("ffff", toHex(-1, 4));
        assertEquals("fffe", toHex(-2, 4));
    }


    @Test
    public void removeAccentTest() {
        assertEquals("Alabama", removeAccents("Àlabàmä"));
    }

    @Test
    public void qTest() {
        assertEquals("\"Yo, man\"", q("Yo, man"));
    }

    @Test
    public void trimPunctuationTest() {
        assertEquals("5, rue alphonse", trimPunctuation("5, rue alphonse, "));
        assertEquals("rue alphonse", trimPunctuation(", rue alphonse"));
        assertEquals("ç", trimPunctuation(",.-+\"*ç%&/()="));
        assertEquals("", trimPunctuation(",.-+\"*%&/()="));
        assertEquals("", trimPunctuation(""));
        assertEquals("zaza", trimPunctuation("zaza"));
        assertEquals("L", trimPunctuation(", L"));
        assertEquals("F", trimPunctuation("F, "));
        assertNull(trimPunctuation(null));
    }

    @Test
    public void slugTest() {
        assertEquals("-", slug(null));
        assertEquals("-", slug(""));
        assertEquals("ea-e", slug(" ^'èà$¨é"));
        assertEquals("5-bld-st-marcel", slug("5, bld St Marcel."));
    }

    @Test
    public void splitTest() {
        Map.Entry<String, String> ret = StringUtil.split("alpha = 1", '=');
        Assert.assertEquals("alpha", ret.getKey());
        Assert.assertEquals("1", ret.getValue());

        ret = StringUtil.split("alpha ", '=');
        Assert.assertEquals("alpha", ret.getKey());
        Assert.assertEquals("", ret.getValue());

        ret = StringUtil.split("  = 698 ", '=');
        Assert.assertEquals("", ret.getKey());
        Assert.assertEquals("698", ret.getValue());
    }

    @Test
    public void versionTest() {
        Assert.assertEquals(1_0000_0000, StringUtil.versionAsLong("1.0.0"));
        Assert.assertEquals(3_0000_0000, StringUtil.versionAsLong("v3"));
        Assert.assertEquals(3_0001_0010, StringUtil.versionAsLong("v3.1.10"));
        Assert.assertEquals(3_0078_0010, StringUtil.versionAsLong("v3.078.10"));
    }

    @Test
    public void untrailTest() {
        Assert.assertEquals("tz", StringUtil.untrailingslashit("tz/"));
        Assert.assertEquals("tz", StringUtil.untrailingslashit("tz"));
        Assert.assertEquals(null, StringUtil.untrailingslashit(null));
        Assert.assertEquals("/123", StringUtil.untrailingslashit("/123"));
        Assert.assertEquals("123", StringUtil.untrailingslashit("123\\"));
    }

    @Test
    public void securePathTest() {
        Assert.assertEquals("/", StringUtil.securedPath("/"));
        Assert.assertEquals("/", StringUtil.securedPath("//"));
        Assert.assertEquals("/var/www/zaza", StringUtil.securedPath("/etc/../var/www/zaza"));
        Assert.assertEquals("var/www/zaza", StringUtil.securedPath("var/././www/zaza"));
        Assert.assertEquals("var/www/zozo/", StringUtil.securedPath("var/././www/zozo/"));
        Assert.assertEquals("var/www/zaza", StringUtil.securedPath("../var/www/zaza"));
        Assert.assertEquals("/var/www/zaza", StringUtil.securedPath("/../var/www/zaza"));
    }
}
