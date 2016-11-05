package com.appmon.control;

import com.appmon.control.models.user.IUserModel;
import com.appmon.control.presenters.RegisterPresenter;
import com.appmon.control.views.IRegisterView;

import org.junit.Test;
import org.mockito.ArgumentCaptor;

import static org.mockito.Mockito.*;

public class RegisterPresenterTest {

    @Test
    public void testRegister() {
        IUserModel mockedModel = mock(IUserModel.class);
        IRegisterView mockedView = mock(IRegisterView.class);
        ArgumentCaptor<IUserModel.IRegisterListener> listener =
                ArgumentCaptor.forClass(IUserModel.IRegisterListener.class);
        RegisterPresenter presenter = new RegisterPresenter(mockedModel);
        presenter.attachView(mockedView);
        // test callbacks code
        verify(mockedModel).addRegisterListener(listener.capture());
        // send success. is must start new activity and hide progress
        listener.getValue().onSuccess();
        verify(mockedView).startDeviceListActivity();
        verify(mockedView).showProgress(false);
        // fails
        reset(mockedView);
        listener.getValue().onFail(IUserModel.RegisterError.INVALID_EMAIL);
        verify(mockedView).showInputError(IRegisterView.InputError.INVALID_EMAIL);
        verify(mockedView).showProgress(false);
        reset(mockedView);
        listener.getValue().onFail(IUserModel.RegisterError.USER_EXISTS);
        verify(mockedView).showInputError(IRegisterView.InputError.USER_EXISTS);
        verify(mockedView).showProgress(false);
        reset(mockedView);
        listener.getValue().onFail(IUserModel.RegisterError.WEAK_PASSWORD);
        verify(mockedView).showInputError(IRegisterView.InputError.WEAK_PASSWORD);
        verify(mockedView).showProgress(false);
        // test register action code
        presenter.registerWithEmail("Test", "String");
        verify(mockedView).clearInputErrors();
        verify(mockedView).showProgress(true);
        verify(mockedModel).registerWithEmail(any(String.class), any(String.class));
    }

    @Test
    public void listenersTest() {
        IUserModel mockedModel = mock(IUserModel.class);
        IRegisterView mockedView = mock(IRegisterView.class);
        RegisterPresenter presenter = new RegisterPresenter(mockedModel);

        presenter.attachView(mockedView);
        verify(mockedModel).addRegisterListener(
                any(IUserModel.IRegisterListener.class));

        presenter.detachView();
        verify(mockedModel).removeRegisterListener(
                any(IUserModel.IRegisterListener.class));
    }

}
