package com.appmon.control;

import com.appmon.control.models.user.IUserModel;
import com.appmon.control.presenters.LoginPresenter;
import com.appmon.control.presenters.WelcomePresenter;
import com.appmon.control.views.ILoginView;
import com.appmon.control.views.IWelcomeView;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class LoginPresenterUnitTest {

    @Mock
    private IUserModel mockedModel;
    @Mock
    private ILoginView mockedView;

    @Captor
    private ArgumentCaptor<IUserModel.IResetPasswordListener> passwordResetListener;
    @Captor
    private ArgumentCaptor<IUserModel.ISignInListener> signInListener;

    private LoginPresenter presenter;

    @Before
    public void setup() {
        presenter = new LoginPresenter(mockedModel);
        presenter.attachView(mockedView);
    }

    @Test
    public void resetPasswordTest() {
        // reset Password
        presenter.resetPassword("Test");
        verify(mockedModel).resetPassword(any(String.class));
        // check callback code. capturing listener from model mock.
        verify(mockedModel).addResetPasswordListener(passwordResetListener.capture());
        // check success and fail model callback results
        passwordResetListener.getValue().onSuccess();
        verify(mockedView).showMessage(ILoginView.Message.PASSWORD_RESET_SENT);
        // at the moment only one error type available
        passwordResetListener.getValue().onFail(IUserModel.ResetPasswordError.INVALID_USER);
        verify(mockedView).showInputError(ILoginView.InputError.INVALID_EMAIL);
    }

    @Test
    public void signInTest() {
        // test callback code
        verify(mockedModel).addSignInListener(signInListener.capture());
        signInListener.getValue().onSuccess();
        // if signed in => hide progress (below) and go to Device List
        verify(mockedView).startDeviceListActivity();
        verify(mockedView).showProgress(false);
        reset(mockedView);
        // failures
        signInListener.getValue().onFail(IUserModel.SignInError.WRONG_PASSWORD);
        verify(mockedView).showInputError(ILoginView.InputError.WRONG_PASSWORD);
        verify(mockedView).showProgress(false);
        reset(mockedView);

        signInListener.getValue().onFail(IUserModel.SignInError.INVALID_EMAIL);
        verify(mockedView).showInputError(ILoginView.InputError.INVALID_EMAIL);
        verify(mockedView).showProgress(false);
        // test sign In action code. It must invoke model method, show Progress and clear errors
        presenter.signInWithEmail("Login", "Password");
        verify(mockedModel).signInWithEmail(any(String.class), any(String.class));
        verify(mockedView).showProgress(true);
        verify(mockedView).clearInputErrors();
    }

    @Test
    public void listenersTest() {
        //add listeners on attach
        verify(mockedModel).addSignInListener(
                any(IUserModel.ISignInListener.class));
        verify(mockedModel).addResetPasswordListener(
                any(IUserModel.IResetPasswordListener.class));
        // remove them on detach
        presenter.detachView();
        verify(mockedModel).removeSignInListener(
                any(IUserModel.ISignInListener.class));
        verify(mockedModel).removeResetPasswordListener(
                any(IUserModel.IResetPasswordListener.class));
    }
}
