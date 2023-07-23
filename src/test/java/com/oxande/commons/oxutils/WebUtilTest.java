package com.oxande.commons.oxutils;

import org.junit.Assert;
import org.junit.Test;

public class WebUtilTest {

    @Test
    public void unhtmlTest() {
        Assert.assertEquals("", WebUtil.unhtml(null));
        Assert.assertEquals("Yo man", WebUtil.unhtml("Yo man"));
        Assert.assertEquals("Chêne", WebUtil.unhtml("Ch&234;ne"));
        Assert.assertEquals("Chêne", WebUtil.unhtml("Ch&#ea;ne"));
        Assert.assertEquals("Chêne", WebUtil.unhtml("Ch&ecirc;ne"));
        Assert.assertEquals("Chêne", WebUtil.unhtml("<b>Ch&ecirc;ne<b>"));
    }
}