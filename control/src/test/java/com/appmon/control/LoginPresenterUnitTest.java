package com.appmon.control;

import com.appmon.control.models.user.IUserModel;
import com.appmon.control.presenters.LoginPresenter;
import com.appmon.control.views.ILoginView;

import org.junit.Test;
import org.mockito.ArgumentCaptor;

import static org.mockito.Mockito.*;

public class LoginPresenterUnitTest {

    @Test
    public void resetPasswordTest() {
        IUserModel mockedModel = mock(IUserModel.class);
        ArgumentCaptor<IUserModel.IResetPasswordListener> listener =
                ArgumentCaptor.forClass(IUserModel.IResetPasswordListener.class);
        ILoginView mockedView = mock(ILoginView.class);
        LoginPresenter presenter = new LoginPresenter(mockedModel);
        presenter.attachView(mockedView);
        // reset Password
        presenter.resetPassword("Test");
        verify(mockedModel).resetPassword(any(String.class));
        // check callback code. capturing listener from model mock.
        verify(mockedModel).addResetPasswordListener(listener.capture());
        // check success and fail model callback results
        listener.getValue().onSuccess();
        verify(mockedView).showMessage(ILoginView.Message.PASSWORD_RESET_SENT);
        // at the moment only one error type available
        listener.getValue().onFail(IUserModel.ResetPasswordError.INVALID_USER);
        verify(mockedView).showInputError(ILoginView.InputError.INVALID_EMAIL);
    }

    @Test
    public void signInTest() {
        IUserModel mockedModel = mock(IUserModel.class);
        ArgumentCaptor<IUserModel.ISignInListener> listener =
                ArgumentCaptor.forClass(IUserModel.ISignInListener.class);
        ILoginView mockedView = mock(ILoginView.class);
        LoginPresenter presenter = new LoginPresenter(mockedModel);
        presenter.attachView(mockedView);
        // test callback code
        verify(mockedModel).addSignInListener(listener.capture());
        listener.getValue().onSuccess();
        // if signed in => hide progress (below) and go to Device List
        verify(mockedView).startDeviceListActivity();
        verify(mockedView).showProgress(false);
        reset(mockedView);
        // failures
        listener.getValue().onFail(IUserModel.SignInError.WRONG_PASSWORD);
        verify(mockedView).showInputError(ILoginView.InputError.WRONG_PASSWORD);
        verify(mockedView).showProgress(false);
        reset(mockedView);

        listener.getValue().onFail(IUserModel.SignInError.INVALID_EMAIL);
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
        IUserModel mockedModel = mock(IUserModel.class);
        ILoginView mockedView = mock(ILoginView.class);
        LoginPresenter presenter = new LoginPresenter(mockedModel);
        presenter.attachView(mockedView);
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
