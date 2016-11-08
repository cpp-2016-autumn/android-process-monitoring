package com.appmon.shared;

import org.junit.Test;

import static org.junit.Assert.*;

import static com.appmon.shared.utils.Validator.*;

public class ValidatorUnitTest {
    @Test
    public void emails() {
        // good emails
        assertTrue(validateEmail("test@domain.com"));
        assertTrue(validateEmail("leveled@domain.wow.com"));
        assertTrue(validateEmail("leveled.login@domain.com"));
        assertTrue(validateEmail("leveled.login@leveled.domain.com"));
        assertTrue(validateEmail("under_score@domain.com"));
        assertTrue(validateEmail("hypen-login@domain.com"));
        // bad emails
        assertFalse(validateEmail("@onlydomain.com"));
        assertFalse(validateEmail("onlylogin@"));
        assertFalse(validateEmail("@"));
        assertFalse(validateEmail(""));
        assertFalse(validateEmail("double@at@email.com"));
        assertFalse(validateEmail("small@domain.x"));
        assertFalse(validateEmail("nodomain@.x"));
    }

    @Test
    public void passwords() {
        // password can contain any UTF-16 symbols
        assertTrue(validatePassword("goodPassword12"));
        assertTrue(validatePassword("even like this"));
        assertTrue(validatePassword("и так тоже"));
        // bit it can't be shorter than 6 chars
        assertFalse(validatePassword("tiny5"));
        assertFalse(validatePassword(""));
    }

    @Test
    public void pinCodes() {
        // good pins
        assertTrue(validatePin("0000"));
        assertTrue(validatePin("99999"));
        assertTrue(validatePin("0012300"));
        // bad pins
        assertFalse(validatePin("123"));
        assertFalse(validatePin(""));
        assertFalse(validatePin("ab123"));
        assertFalse(validatePin("123 23"));
    }
}
