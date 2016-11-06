package com.appmon.control;

import com.appmon.control.persistence.ModelPresenterManager;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

public class ModelPresenterManagerUnitTest {

    private ModelPresenterManager mpm;

    @Before
    public void setup() {
        mpm = ModelPresenterManager.getInstance();
    }

    @Test
    public void instancesEquals() {
        assertEquals(mpm, ModelPresenterManager.getInstance());
        assertEquals(mpm.getWelcomePresenter(), mpm.getWelcomePresenter());
        assertEquals(mpm.getLoginPresenter(), mpm.getLoginPresenter());
        assertEquals(mpm.getRegisterPresenter(), mpm.getRegisterPresenter());
        assertEquals(mpm.getSettingsPresenter(), mpm.getSettingsPresenter());
    }

    @Test
    public void instancesNonNull() {
        assertNotEquals(null, mpm);
        assertNotEquals(null, mpm.getWelcomePresenter());
        assertNotEquals(null, mpm.getLoginPresenter());
        assertNotEquals(null, mpm.getRegisterPresenter());
        assertNotEquals(null, mpm.getSettingsPresenter());
    }
}
