package utils;

import org.junit.Test;

import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;
import static utils.PatternValidator.isValid;

public class PatternValidatorTest {

    @Test
    public void testPatternValidator() {
        // then
        assertFalse(isValid("q[d-e=c"));
        assertFalse(isValid("3."));
        assertFalse(isValid("2,5"));
        assertFalse(isValid("-3"));
        assertFalse(isValid("."));
        assertFalse(isValid(" "));
        assertFalse(isValid(""));

        assertTrue(isValid("0"));
        assertTrue(isValid("10"));
        assertTrue(isValid("1.5"));
    }
}