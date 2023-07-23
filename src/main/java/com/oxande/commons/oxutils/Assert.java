package com.oxande.commons.oxutils;

import org.apache.commons.lang3.StringUtils;

import java.util.Collection;
import java.util.Objects;

public final class Assert {
    public static void isTrue(boolean b, String message) {
        if (!b) {
            throw new IllegalArgumentException(message);
        }
    }

    public static void notNull(Object obj, String varName) {
        if (obj == null) {
            throw new IllegalArgumentException("Expected not null: " + varName);
        }
    }

    /**
     * Expect two objects are equal.
     *
     * @param a the first object
     * @param b the second object
     * @param message the message in case the objects are not equals
     * @see #expected(Object, Object, String)
     */
    public static void equals(Object a, Object b, String message) {
        if (!Objects.equals(a, b)) {
            throw new IllegalArgumentException("Expected " + a + " equals " + b + ": " + message);
        }
    }

    /**
     * Check an expected value. Throws an {@link IllegalArgumentException} in case the value has not the
     * expected value.
     *
     * @param expectedValue the expected value.
     * @param value the value to test
     * @param variable the variable name checked (part of the message if failed)
     */
    public static void expected(final Object expectedValue, Object value, String variable) {
        if (!Objects.equals(expectedValue, value)) {
            throw new IllegalArgumentException(variable + " expected to be " + StringUtil.q(expectedValue) + " (but is " + StringUtil.q(value) + ")");
        }
    }

    public static void notEmpty(Collection<?> coll, String varName) {
        Assert.notNull(coll, varName);
        if (coll.isEmpty()) {
            throw new IllegalArgumentException("Expected not empty: " + varName);
        }
    }

    public static void unique(Collection<?> list, String varName) {
        Assert.notNull(list, varName);
        if (list.size() != 1) {
            throw new IllegalArgumentException("Expected one element (not " + list.size() + "): " + varName);
        }
    }

    public static void notBlank(String str, String varName) {
        if (StringUtils.isBlank(str)) {
            throw new IllegalArgumentException((str == null ? "Null string" : "Empty string") + ": " + varName);
        }
    }

}
