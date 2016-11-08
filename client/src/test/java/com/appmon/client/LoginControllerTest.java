package com.appmon.client;

/**
 * Created by Mike on 11/7/2016.
 */

import com.appmon.client.initialization.login.ILoginController;
import com.appmon.client.initialization.login.ILoginActivity;
import com.appmon.client.initialization.login.LoginController;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class LoginControllerTest {
    private ILoginController controller;

    @Mock
    ILoginActivity mockActivity;

    @Before
    public void setup() {
        controller = LoginController.getInstance();
    }

    @Test
    public void testLoginController(){
        controller.hookActivity(mockActivity);
        //TODO: Finish test
    }
}
