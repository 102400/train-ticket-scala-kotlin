package com.vatcore.trainticket.util;

import java.time.temporal.Temporal;
import java.util.Date;
import java.time.*;
import java.util.Objects;

/**
 * @author xzy
 * @version 20171020, 20170917
 * @since 1.8
 */
public final class DateUtil {

    private DateUtil(){
        throw new AssertionError();
    }

    /**
     * @param date java.util.Date
     * @return java.time.LocalDateTime
     */
    public static LocalDateTime toLocalDateTime(Date date) {
        return LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault());
    }

    /**
     * @param date java.util.Date
     * @return java.time.LocalDate
     */
    public static LocalDate toLocalDate(Date date) {
        return LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault()).toLocalDate();
    }

    /**
     * @param date java.util.Date
     * @return java.time.LocalTime
     */
    public static LocalTime toLocalTime(Date date) {
        return LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault()).toLocalTime();
    }

    /**
     * @param localDateTime java.time.LocalDateTime
     * @return java.util.Date
     */
    public static Date toDate(LocalDateTime localDateTime) {
        return Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
    }

    /**
     * @param localDate java.time.LocalDate
     * @return java.util.Date
     */
    public static Date toDate(LocalDate localDate) {
        return Date.from(localDate.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
    }

    /**
     * @param localDate java.time.LocalDate
     * @return Unix timestamp
     */
    public static long getLong(LocalDate localDate) {
        return getLong(localDate.atStartOfDay());
    }

    /**
     * @param localDateTime java.time.LocalDateTime
     * @return Unix timestamp
     */
    public static long getLong(LocalDateTime localDateTime) {
        return localDateTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
    }

    @Deprecated
    private static <T extends java.io.Serializable> long getLong(T date) throws IllegalArgumentException {
        long time = -1;
        if (date instanceof LocalDateTime) {
            time = ((LocalDateTime) date).atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
        }
        else if (date instanceof LocalDate) {
            time = ((LocalDate) date).atStartOfDay().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
        }
        else if (date instanceof Date) {
            time = ((Date) date).getTime();
        }
        else {
            throw new IllegalArgumentException("Class Not Support");
        }
        return time;
    }

    /**
     * @param number
     * @return java.lang.String
     */
    private static String fillZero(int number) {
        return StringUtil.of(number).fillZero().toString();
    }

    public enum FormatPattern {
        DATETIME  // example: 2017-10-20 15:46:23
    }

    /**
     * @param localDate java.time.LocalDate
     * @return java.lang.String
     */
    public static String format(LocalDate localDate) {
        return format(localDate.atStartOfDay());
    }

    /**
     * @param localDateTime java.time.LocalDateTime
     * @return java.lang.String
     */
    public static String format(LocalDateTime localDateTime) {
        return format(localDateTime, FormatPattern.DATETIME);
    }

    /**
     * @param localDateTime java.time.LocalDateTime
     * @param formatPattern FormatPattern
     * @return java.lang.String
     */
    public static String format(LocalDateTime localDateTime, FormatPattern formatPattern) {
        switch (formatPattern) {
            case DATETIME:
                return localDateTime.getYear()
                    + "-" + fillZero(localDateTime.getMonthValue())
                    + "-" + fillZero(localDateTime.getDayOfMonth())
                    + " " + fillZero(localDateTime.getHour())
                    + ":" + fillZero(localDateTime.getMinute())
                    + ":" + fillZero(localDateTime.getSecond());
            default: throw new IllegalArgumentException();
        }
    }

}
