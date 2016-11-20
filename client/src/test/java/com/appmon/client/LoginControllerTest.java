package com.appmon.client;

import com.appmon.client.initialization.login.ILoginController;
import com.appmon.client.initialization.login.ILoginActivity;
import com.appmon.client.initialization.login.LoginController;
import com.appmon.shared.IAuthService;
import com.appmon.shared.IUser;
import com.appmon.shared.ResultListener;
import com.appmon.shared.exceptions.AuthEmailTakenException;
import com.appmon.shared.exceptions.AuthException;
import com.appmon.shared.exceptions.AuthInvalidEmailException;
import com.appmon.shared.exceptions.AuthWrongPasswordException;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class LoginControllerTest {
    private ILoginController controller;

    @Mock
    private ILoginActivity mockLoginActivity;

    @Mock
    private IAuthService mockAuth;

    private IUser user;

    @Captor
    private ArgumentCaptor<ResultListener<IUser, Throwable>> authResult;

    @Before
    public void setup() {
        controller = new LoginController(mockAuth);
    }

    @Test
    public void testLoginController(){
        controller.hookActivity(mockLoginActivity);

        //Test empty email
        controller.attemptLogin("", "password");
        verify(mockLoginActivity).setError(ILoginActivity.Error.EMAIL_EMPTY);
        reset(mockLoginActivity);

        //Test invalid email
        controller.attemptLogin("Foo", "password");
        verify(mockLoginActivity).setError(ILoginActivity.Error.EMAIL_INVALID);
        reset(mockLoginActivity);

        //Test invalid password
        controller.attemptLogin("Foo", "");
        verify(mockLoginActivity).setError(ILoginActivity.Error.PASSWORD_INVALID);
        reset(mockLoginActivity);

        controller.attemptLogin("Foo@bar.test", "password");
        //Test wrong email
        verify(mockAuth).signInWithEmail(any(String.class), any(String.class),
                authResult.capture());
        authResult.getValue().onFailure(new AuthInvalidEmailException(null));
        verify(mockLoginActivity).setError(ILoginActivity.Error.EMAIL_INVALID);
        reset(mockLoginActivity);

        //Test wrong password
        authResult.getValue().onFailure(new AuthWrongPasswordException(null));
        verify(mockLoginActivity).setError(ILoginActivity.Error.PASSWORD_INVALID);
        reset(mockLoginActivity);

        //Test unknown error
        authResult.getValue().onFailure(new AuthEmailTakenException(null));
        authResult.getValue().onFailure(new AuthException(null));
        verify(mockLoginActivity, times(2)).setError(ILoginActivity.Error.UNKNOWN_ERROR);
        reset(mockLoginActivity);

        //Test proper login
        authResult.getValue().onSuccess(user);
        verify(mockLoginActivity).setError(ILoginActivity.Error.NO_ERROR);
        verify(mockLoginActivity).loginSuccessful();
        reset(mockLoginActivity);
    }
}
