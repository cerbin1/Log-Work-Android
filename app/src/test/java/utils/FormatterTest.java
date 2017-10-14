package utils;

import org.junit.Test;

import static junit.framework.Assert.assertEquals;

public class FormatterTest {
    @Test
    public void shouldFormatStringAsDouble() {
        // when
        String string1 = Formatter.formatDoubleAsString("10");
        String string2 = Formatter.formatDoubleAsString("0");
        String string3 = Formatter.formatDoubleAsString("1.5");
        String string4 = Formatter.formatDoubleAsString("12.0");
        String string5 = Formatter.formatDoubleAsString("0.0");
        String string6 = Formatter.formatDoubleAsString("12222222.0");
        String string7 = Formatter.formatDoubleAsString("1243231.0");

        // then
        assertEquals("10", string1);
        assertEquals("0", string2);
        assertEquals("1.5", string3);
        assertEquals("12", string4);
        assertEquals("0", string5);
        assertEquals("12222222", string6);
        assertEquals("1243231", string7);
    }

    @Test
    public void shouldFormatDoubleAsDouble() {
        String double1 = Formatter.formatDoubleAsString(10);
        String double2 = Formatter.formatDoubleAsString(0);
        String double3 = Formatter.formatDoubleAsString(1.5);
        String double4 = Formatter.formatDoubleAsString(12.0);
        String double5 = Formatter.formatDoubleAsString(0.0);
        String double6 = Formatter.formatDoubleAsString(12222222.0);
        String double7 = Formatter.formatDoubleAsString(1243231.0);

        // then
        assertEquals("10", double1);
        assertEquals("0", double2);
        assertEquals("1.5", double3);
        assertEquals("12", double4);
        assertEquals("0", double5);
        assertEquals("12222222", double6);
        assertEquals("1243231", double7);
    }
}
