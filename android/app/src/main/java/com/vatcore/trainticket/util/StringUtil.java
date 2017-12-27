package com.vatcore.trainticket.util;

import java.util.Objects;
import java.util.function.Consumer;

/**
 * @author xzy
 * @version 20171020, 20171020
 * @since 1.8
 */
public final class StringUtil {

    private String value;

    private StringUtil(String string) {
        value = string;
    }

    public static StringUtil of(String string) {
        return new StringUtil(string);
    }

    public static StringUtil of(int i) {
        return new StringUtil(String.valueOf(i));
    }

    /**
     * @return
     */
    public StringUtil fillZero() {
        return fillZero(2);
    }

    /**
     * @param digit
     * @return
     */
    public StringUtil fillZero(int digit) {
        return fillChar(digit, '0');
    }

    /**
     * @param digit
     * @param c
     * @return
     */
    public StringUtil fillChar(int digit, char c) {
        if (digit < 0) throw new IndexOutOfBoundsException();
        if (digit > value.length()) {
            StringBuilder sb = new StringBuilder(value.length());
            for (int i = 0; i < digit - value.length(); i++) sb.append(c);
            sb.append(value);
            value = sb.toString();
        }
        return this;
    }

    @Deprecated
    public void forEach(Consumer<String> action) {
        Objects.requireNonNull(action);
        action.accept(value);
    }

    @Override
    public String toString() {
        return value;
    }

}
