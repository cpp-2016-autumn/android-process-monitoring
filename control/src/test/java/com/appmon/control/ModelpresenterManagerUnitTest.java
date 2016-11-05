package com.appmon.control;

import com.appmon.control.persistence.ModelPresenterManager;

import org.junit.Test;

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

public class ModelpresenterManagerUnitTest {

    @Test
    public void instancesEquals() {
        ModelPresenterManager mpm = ModelPresenterManager.getInstance();
        assertEquals(mpm, ModelPresenterManager.getInstance());
        assertEquals(mpm.getWelcomePresenter(), mpm.getWelcomePresenter());
        assertEquals(mpm.getLoginPresenter(), mpm.getLoginPresenter());
        assertEquals(mpm.getRegisterPresenter(), mpm.getRegisterPresenter());
        assertEquals(mpm.getSettingsPresenter(), mpm.getSettingsPresenter());
    }

    @Test
    public void instancesNonNull() {
        ModelPresenterManager mpm = ModelPresenterManager.getInstance();
        assertNotEquals(null, mpm);
        assertNotEquals(null, mpm.getWelcomePresenter());
        assertNotEquals(null, mpm.getLoginPresenter());
        assertNotEquals(null, mpm.getRegisterPresenter());
        assertNotEquals(null, mpm.getSettingsPresenter());
    }
}
