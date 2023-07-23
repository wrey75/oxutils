package com.oxande.commons.oxutils;

import org.junit.Assert;
import org.junit.Test;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class SafeMapTest {
    private static final Map<String,Object> EMPTY_MAP = Collections.emptyMap();

    @Test
    public void mapNullTest() {
        Assert.assertNull(SafeMap.empty().getUrl("url"));
    }

    @Test
    public void booleanTest() {
        Assert.assertNull(SafeMap.empty().getBoolean("mykey"));
        Assert.assertFalse(SafeMap.empty().getBoolean("mykey", false));
        Assert.assertTrue(SafeMap.empty().getBoolean("mykey", true));
    }

    @Test
    public void safetyTest() {
        Map<String, Object> yo = new HashMap<>();
        yo.put("$val", "liste");
        Assert.assertEquals("liste", SafeMap.serialized(yo).get("val"));
    }
}