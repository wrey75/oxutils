package com.oxande.commons.oxutils;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Optional;

public class DateUtil {
    private static final Logger LOG = LoggerFactory.getLogger(DateUtil.class);

    public static final long ONE_SECOND = 1000L;
    public static final long ONE_MINUTE = 60 * ONE_SECOND;
    public static final long ONE_HOUR = 60L * ONE_MINUTE;
    public static final long ONE_DAY = 24 * ONE_HOUR;
    public static final long ONE_WEEK = 7L * ONE_DAY;
    public static final long ONE_MONTH = 30L * ONE_DAY; // about
    public static final long ONE_QUARTER = 92L * ONE_DAY; // about (91 is a primer)
    public static final long ONE_YEAR = 365L * ONE_DAY; // leap year

    public static Date of(LocalDateTime ldt) {
        return Date.from(ldt.atZone(ZoneId.systemDefault()).toInstant());
    }

    public static DateTimeFormatter[] DATETIME_FORMATERS = {
            DateTimeFormatter.RFC_1123_DATE_TIME,
            DateTimeFormatter.ISO_INSTANT,
            DateTimeFormatter.BASIC_ISO_DATE,
            DateTimeFormatter.ISO_DATE
    };

    public static final String[] ISO_FORMATS = {
            "yyyy-MM-dd'T'HH:mm:ss.SSSZ",
            "yyyy-MM-dd'T'HH:mm:ssZ",
            "yyyy-MM-dd'T'HH:mm:ss.SSS",
            "yyyy-MM-dd'T'HH:mm:ss",
            "yyyy-MM-dd HH:mm:ss.SSSZ",
            "yyyy-MM-dd HH:mm:ssZ",
            "yyyy-MM-dd HH:mm:ss",
            "yyyy-MM-dd HH:mm",
            "yyyy-MM-dd",
    };

    /**
     * Clone the current date. Note if the input is a null pointer,
     * the returned value is also a null npointer.
     *
     * @param d the original date.
     * @return a copy of the same date.
     */
    public static Date clone(Date d) {
        return (d == null ? null : new Date(d.getTime()));
    }

    /**
     * Add monthes.
     *
     * @param date the date
     * @param amount the amount of monthes.
     * @return the date with the added months
     */
    public static Date addMonths(Date date, int amount) {
        return DateUtils.addMonths(date, amount);
    }

    /**
     * Add years.
     *
     * @param date the date
     * @param amount the amount of years.
     * @return the new date with the years added.
     */
    public static Date addYears(Date date, int amount) {
        return DateUtils.addYears(date, amount);
    }

    /**
     * Add days.
     *
     * @param date the date
     * @param amount the amount of days.
     * @return the date with the added days
     */
    public static Date addDays(Date date, int amount) {
        return DateUtils.addDays(date, amount);
    }

    /**
     * Add the number of milliseconds.
     *
     * @param date the date, if null, we use the current date.
     * @param time the number of milliseconds
     * @return the new date.
     */
    public static Date add(Date date, long time) {
        return new Date(Optional.ofNullable(date).orElseGet(Date::new).getTime() + time);
    }

    /**
     * Remove the number of milliseconds.
     *
     * @param date the date
     * @param time the number of milliseconds
     * @return the new date.
     */
    public static Date sub(Date date, long time) {
        return add(date, -1L * time);
    }

    /**
     * Returns the first day of the month.
     *
     * @param date the current date
     * @return the first day in the month at midnight local time
     */
    public static Date firstDayOfTheMonth(Date date) {
        Calendar cal = new GregorianCalendar();
        cal.setTime(date);
        cal.set(Calendar.DAY_OF_MONTH, 1);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    }


    public static Date lastDayOfTheMonth(Date date) {
        Calendar cal = new GregorianCalendar();
        cal.setTime(date);
        cal.add(Calendar.MONTH, 1);
        Date d = DateUtil.firstDayOfTheMonth(cal.getTime());
        return DateUtil.addDays(d, -1);
    }

    /**
     * Convert to Midnight in local time.
     * <p>
     *
     * @param date the time
     * @return the same date at midnight (localtime)
     */
    public static Date toMidnight(final Date date) {
        Calendar cal = new GregorianCalendar();
        cal.setTime(date);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    }

    /**
     * Convert a date to its local date counterpart.
     *
     * @param d the date to convert. If null is provided, returns null.
     * @return the {@link LocalDate}.
     */
    public static LocalDate toLocalDate(Date d) {
        if (d == null) return null;
        Calendar cal = new GregorianCalendar();
        cal.setTime(d);
        return LocalDate.of(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH) + 1, cal.get(Calendar.DAY_OF_MONTH));
    }


    public static String humanDuration(long duration) {
        if (duration > DateUtil.ONE_YEAR) {
            return "about " + duration / DateUtil.ONE_YEAR / 12 + " months";
        } else if (duration > DateUtil.ONE_WEEK * 4) {
            return "about " + duration / DateUtil.ONE_WEEK + " weeks";
        } else if (duration > DateUtil.ONE_DAY * 3) {
            return "about " + duration / DateUtil.ONE_DAY + " day";
        } else if (duration > DateUtil.ONE_DAY) {
            return "about " + duration / DateUtil.ONE_HOUR + " hours";
        } else if (duration > DateUtil.ONE_HOUR * 2) {
            long minutes = duration / DateUtil.ONE_MINUTE;
            return minutes / 60 + " hours " + minutes % 60 + " min";
        } else if (duration > DateUtil.ONE_MINUTE * 2) {
            long sec = duration / DateUtil.ONE_SECOND;
            return sec / 60 + " min " + sec % 60 + " sec";
        } else if (duration > DateUtil.ONE_SECOND * 5) {
            long sec = duration / DateUtil.ONE_SECOND;
            return sec + " seconds";
        } else if (duration > 500) {
            NumberFormat numberFormat = new DecimalFormat("0.0");
            double sec = (double)duration / DateUtil.ONE_SECOND;
            return numberFormat.format(sec) + " sec.";
        }
        return duration + " milliseconds";
    }

    /**
     * Create a date based on the year, the month and the day.
     *
     * @param year the year
     * @param month the month (from 1 to 12)
     * @param day the day
     * @return the date based on input (at midnight, local time)
     * @deprecated
     */
    @Deprecated
    public static Date of(int year, int month, int day, int hour, int minute, int seconds) {
        Calendar cal = new GregorianCalendar();
        cal.clear();
        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.MONTH, month - 1);
        cal.set(Calendar.DAY_OF_MONTH, day);
        cal.set(Calendar.HOUR_OF_DAY, hour);
        cal.set(Calendar.MINUTE, minute);
        cal.set(Calendar.SECOND, seconds);
        return cal.getTime();
    }

    public static Date of(int year, Month month, int day, int hour, int minute, int seconds) {
        return of(year, month.getValue(), day, hour, minute, seconds);
    }

    public static Date of(int year, Month month, int day) {
        return of(year, month.getValue(), day, 0, 0, 0);
    }


    /**
     * Try to decode a date using new Java-8 techniques.
     *
     * @param value the string value.
     * @return the converted date or null if not ISO date.
     */
    public static Date toDate(String value) {
        if (value != null) {
            for (DateTimeFormatter formatter : DATETIME_FORMATERS) {
                try {
                    TemporalAccessor ta = formatter.parse(value);
                    Instant i = Instant.from(ta);
                    return Date.from(i);
                } catch (DateTimeException ex) {
                    // Format not supported, try the next one
                }
            }
        }
        return null;
    }

    /**
     * Returns the current date.
     *
     * @return the current date.
     */
    public static Date now() {
        return new Date(System.currentTimeMillis());
    }

    /**
     * @see #future(long)
     * @deprecated
     */
    @Deprecated
    public static Date now(long inc) {
        return future(inc);
    }

    /**
     * Returns a date in the future.
     *
     * @param delay the delay in milliseconds.
     * @return the date.
     */
    public static Date future(long delay) {
        return DateUtil.add(DateUtil.now(), +delay);
    }

    public static Date past(long delay) {
        return DateUtil.add(DateUtil.now(), -delay);
    }

    /**
     * Parse a date expressed as ISO format.
     *
     * @param dateStr the date as a String
     * @return the date
     */
    public static Date parse(String dateStr) {
        if (StringUtils.isBlank(dateStr)) return null;
        Date d = toDate(dateStr);
        if (d == null) {
            dateStr = dateStr.replaceFirst("Z$", "+0000");
            for (String isoFormat : ISO_FORMATS) {
                SimpleDateFormat formatter = new SimpleDateFormat(isoFormat);
                try {
                    return formatter.parse(dateStr);
                } catch (ParseException ex) {
                    // Try the next one
                }
            }
            throw new IllegalArgumentException("Date " + StringUtil.q(dateStr) + " bad formatted.");
        }
        return d;
    }

    /**
     * Format the date in ISO format for a compatible parsing.
     *
     * @param d the date
     * @return the date as a String
     */
    public static String format(Date d) {
        if (d == null) return null;
        SimpleDateFormat formatter = new SimpleDateFormat(ISO_FORMATS[0]);
        return formatter.format(d);
    }

    /**
     * Return a date in the future. Currently, returns the current date plus 24 hours.
     *
     * @return a date in the future.
     */
    public static Date nearFuture() {
        return DateUtil.future(DateUtil.ONE_DAY);
    }

    /**
     * Convert a String to duration.
     *
     * @param time the time to use
     * @return the time is milliseconds.
     */
    public static long durationOf(String time) {
        long unit = DateUtil.ONE_SECOND;
        Assert.notNull(time, "time");
        StringBuilder buf = new StringBuilder();
        int len = time.length();
        int i = 0;
        while (i < len && Character.isDigit(time.charAt(i))) {
            buf.append(time.charAt(i++));
        }
        String remaining = time.substring(i).trim();
        if (remaining.length() > 0) {
            char c = remaining.charAt(0);
            switch (c) {
                case 'd':
                    unit = DateUtil.ONE_DAY;
                    break;
                case 's':
                    unit = DateUtil.ONE_SECOND;
                    break;
                case 'm':
                    unit = DateUtil.ONE_MINUTE;
                    break;
                case 'h':
                    unit = DateUtil.ONE_HOUR;
                    break;
                case 'w':
                    unit = DateUtil.ONE_WEEK;
                    break;
            }
        }
        return NumberUtils.toLong(buf.toString(), 1) * unit;
    }
}
