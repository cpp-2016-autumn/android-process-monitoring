package com.appmon.control;

import com.appmon.control.models.user.IUserModel;
import com.appmon.control.presenters.SettingsPresenter;
import com.appmon.control.views.ISettingsView;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class SettingsPresenterUnitTest {

    @Mock
    private IUserModel mockedModel;
    @Mock
    private ISettingsView mockedView;

    @Captor
    private ArgumentCaptor<IUserModel.IChangePasswordListener> changePasswordListener;
    @Captor
    private ArgumentCaptor<IUserModel.IChangeAppPinListener> changeAppPinListener;
    @Captor
    private ArgumentCaptor<IUserModel.IChangeClientPinListener> changeClientPinListener;

    private SettingsPresenter presenter;

    @Before
    public void setup() {
        presenter = new SettingsPresenter(mockedModel);
        presenter.attachView(mockedView);
    }
    @Test
    public void signOutTest() {
        presenter.signOut();
        verify(mockedModel).signOut();
        verify(mockedView).clearInputErrors();
        verify(mockedView).startWelcomeActivity();
    }

    @Test
    public void changePassword() {
        verify(mockedModel).addChangePasswordListener(changePasswordListener.capture());
        // clear focus (hide keyboard) and show success message on success
        changePasswordListener.getValue().onSuccess();
        verify(mockedView).clearFocus();
        verify(mockedView).showMessage(ISettingsView.Message.PASSWORD_CHANGED);
        // on fail show input error
        changePasswordListener.getValue().onFail(IUserModel.ChangePasswordError.WEAK_PASSWORD);
        verify(mockedView).showInputError(ISettingsView.InputError.WEAK_PASSWORD);
        // Test for "different passwords" - only check that presenter makes for passwords
        reset(mockedModel, mockedView); // reset mocks after previous test
        presenter.changePassword("Different", "Password");
        verify(mockedView).clearInputErrors();
        verify(mockedView).showInputError(ISettingsView.InputError.DIFFERENT_PASSWORDS);
        verify(mockedModel, never()).changePassword(any(String.class));
        // same password, all ok!
        reset(mockedModel, mockedView);
        presenter.changePassword("Same", "Same");
        verify(mockedView).clearInputErrors();
        verify(mockedModel).changePassword(any(String.class));
    }

    @Test
    public void changeAppPin() {
        verify(mockedModel).addChangeAppPinListener(changeAppPinListener.capture());
        // clear focus (hide keyboard) and show success message on success
        changeAppPinListener.getValue().onSuccess();
        verify(mockedView).clearFocus();
        verify(mockedView).showMessage(ISettingsView.Message.APP_PIN_CHANGED);
        // on fail show input error
        changeAppPinListener.getValue().onFail(IUserModel.ChangeAppPinError.WEAK_PIN);
        verify(mockedView).showInputError(ISettingsView.InputError.WEAK_APP_PIN);
        // Test for "different pins" - only check that presenter makes for pins
        reset(mockedModel, mockedView); // reset mocks after previous test
        presenter.changeAppPin("321321", "123123");
        verify(mockedView).clearInputErrors();
        verify(mockedView).showInputError(ISettingsView.InputError.DIFFERENT_APP_PINS);
        verify(mockedModel, never()).changeAppPin(any(String.class));
        // same pins
        reset(mockedModel, mockedView);
        presenter.changeAppPin("4242", "4242");
        verify(mockedView).clearInputErrors();
        verify(mockedModel).changeAppPin(any(String.class));
    }

    @Test
    public void changeClientPin() {
        verify(mockedModel).addChangeClientPinListener(changeClientPinListener.capture());
        // clear focus (hide keyboard) and show success message on success
        changeClientPinListener.getValue().onSuccess();
        verify(mockedView).clearFocus();
        verify(mockedView).showMessage(ISettingsView.Message.CLIENT_PIN_CHANGED);
        // on fail show input error
        changeClientPinListener.getValue().onFail(IUserModel.ChangeClientPinError.WEAK_PIN);
        verify(mockedView).showInputError(ISettingsView.InputError.WEAK_CLIENT_PIN);
        reset(mockedModel, mockedView);
        presenter.changeClientPin("4242");
        verify(mockedView).clearInputErrors();
        verify(mockedModel).changeClientPin(any(String.class));
    }

    @Test
    public void listeners() {
        verify(mockedModel).addChangePasswordListener(
                any(IUserModel.IChangePasswordListener.class));
        verify(mockedModel).addChangeAppPinListener(
                any(IUserModel.IChangeAppPinListener.class));
        verify(mockedModel).addChangeClientPinListener(
                any(IUserModel.IChangeClientPinListener.class));
        presenter.detachView();
        verify(mockedModel).removeChangePasswordListener(
                any(IUserModel.IChangePasswordListener.class));
        verify(mockedModel).removeChangeAppPinListener(
                any(IUserModel.IChangeAppPinListener.class));
        verify(mockedModel).removeChangeClientPinListener(
                any(IUserModel.IChangeClientPinListener.class));
    }
}
