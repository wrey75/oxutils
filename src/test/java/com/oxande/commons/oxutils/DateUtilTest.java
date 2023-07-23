package com.oxande.commons.oxutils;

import org.junit.Assert;
import org.junit.Test;

import java.time.LocalDate;
import java.time.Month;
import java.util.Date;
import java.util.TimeZone;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

public class DateUtilTest {
    Date MARCH_9 = new Date(1520626403000L); // Friday 9 March 2018 21:13:23 GMT+1

    public DateUtilTest(){
        // Consider test for Paris date/times.
        TimeZone.setDefault(TimeZone.getTimeZone("Europe/Paris"));
    }

    @Test
    public void setToMidnightTest(){
        assertEquals(new Date(1520550000000L), DateUtil.toMidnight(MARCH_9));
        assertEquals(new Date(1520550000000L), DateUtil.toMidnight(new Date(1520550000000L)));
        assertEquals(new Date(1520550000000L), DateUtil.toMidnight(new Date(1520550000001L)));
        assertEquals(new Date(1520550000000L - 24 * 3600 * 1000), DateUtil.toMidnight(new Date(1520549999999L)));
    }

    @Test
    public void cloneTest(){
        Date now = new Date();

        // NULL -> NULL by contract
        assertEquals(null, DateUtil.clone(null));

        // Current date
        Date cloneNow = DateUtil.clone(now);
        assertFalse("Clone must be a different object", now == cloneNow);
        assertEquals("But must be equals", now, cloneNow);

        // March 9th
        Date cloneMarch9 = DateUtil.clone(MARCH_9);
        assertFalse("Clone must be a different object", MARCH_9 == cloneMarch9);
        assertEquals("But must be equals", MARCH_9, cloneMarch9);
    }

    @Test
    public void lastDayOfTheMonthTest() {
        Date d = DateUtil.lastDayOfTheMonth(MARCH_9);
        assertEquals(new Date(1522447200000L), d);
    }

    @Test
    public void firstDayOfTheMonthTest() {
        Date d = DateUtil.firstDayOfTheMonth(MARCH_9);
        assertEquals(new Date(1519858800000L), d);
    }

    @Test
    public void toLocalDateTest(){
        assertEquals(LocalDate.of(2018, Month.MARCH, 9), DateUtil.toLocalDate(MARCH_9));
    }

    @Test
    public void humanDateTest(){
        assertEquals("1,0 sec.", DateUtil.humanDuration(1000));
        assertEquals("4 min 0 sec", DateUtil.humanDuration(240138));
        assertEquals("4 min 8 sec", DateUtil.humanDuration(248638));
        assertEquals("3 min 0 sec", DateUtil.humanDuration(180000));
        assertEquals("67 seconds", DateUtil.humanDuration(67000));
    }

    @Test
    public void durationTest() {
        org.junit.Assert.assertEquals(12 * DateUtil.ONE_SECOND, DateUtil.durationOf("12"));
        org.junit.Assert.assertEquals(14 * DateUtil.ONE_SECOND, DateUtil.durationOf("14 "));
        org.junit.Assert.assertEquals(14 * DateUtil.ONE_DAY, DateUtil.durationOf("14 d"));
        org.junit.Assert.assertEquals(14 * DateUtil.ONE_DAY, DateUtil.durationOf("14days"));
        org.junit.Assert.assertEquals(28 * DateUtil.ONE_MINUTE, DateUtil.durationOf("28m"));
        Assert.assertEquals(17 * DateUtil.ONE_HOUR, DateUtil.durationOf("17h"));
    }

    @Test
    public void parseDateTest() {
        Date d = DateUtil.of(2021, Month.FEBRUARY, 28, 15,21,42);
        Assert.assertEquals(d, DateUtil.parse("2021-02-28T14:21:42.000Z"));
    }
}
