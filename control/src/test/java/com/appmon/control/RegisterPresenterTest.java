package com.appmon.control;

import com.appmon.control.models.user.IUserModel;
import com.appmon.control.presenters.RegisterPresenter;
import com.appmon.control.views.IRegisterView;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class RegisterPresenterTest {

    @Mock
    private IUserModel mockedModel;
    @Mock
    private IRegisterView mockedView;

    @Captor
    private ArgumentCaptor<IUserModel.IRegisterListener> listener;

    private RegisterPresenter presenter;

    @Before
    public void setup() {
        presenter = new RegisterPresenter(mockedModel);
        presenter.attachView(mockedView);
    }

    @Test
    public void testRegister() {
        // test callbacks code
        verify(mockedModel).addRegisterListener(listener.capture());
        // send success. is must start new activity and hide progress
        listener.getValue().onSuccess();
        verify(mockedView).startDeviceListActivity();
        verify(mockedView).setProgressVisible(false);
        verify(mockedModel).changeClientPin("0000");
        // fails
        reset(mockedView);
        listener.getValue().onFail(IUserModel.RegisterError.INVALID_EMAIL);
        verify(mockedView).showInputError(IRegisterView.InputError.INVALID_EMAIL);
        verify(mockedView).setProgressVisible(false);
        reset(mockedView);
        listener.getValue().onFail(IUserModel.RegisterError.USER_EXISTS);
        verify(mockedView).showInputError(IRegisterView.InputError.USER_EXISTS);
        verify(mockedView).setProgressVisible(false);
        reset(mockedView);
        listener.getValue().onFail(IUserModel.RegisterError.WEAK_PASSWORD);
        verify(mockedView).showInputError(IRegisterView.InputError.WEAK_PASSWORD);
        verify(mockedView).setProgressVisible(false);
        // test register action code
        presenter.registerWithEmail("Test", "String");
        verify(mockedView).clearInputErrors();
        verify(mockedView).setProgressVisible(true);
        verify(mockedModel).registerWithEmail(any(String.class), any(String.class));
    }

    @Test
    public void listenersTest() {
        verify(mockedModel).addRegisterListener(
                any(IUserModel.IRegisterListener.class));

        presenter.detachView();
        verify(mockedModel).removeRegisterListener(
                any(IUserModel.IRegisterListener.class));
    }

}
