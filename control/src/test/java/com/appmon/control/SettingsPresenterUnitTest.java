package com.appmon.control;

import com.appmon.control.models.user.IUserModel;
import com.appmon.control.presenters.SettingsPresenter;
import com.appmon.control.views.ISettingsView;

import org.junit.Test;
import org.mockito.ArgumentCaptor;

import static org.mockito.Mockito.*;

public class SettingsPresenterUnitTest {

    @Test
    public void signOutTest() {
        IUserModel mockedModel = mock(IUserModel.class);
        ISettingsView mockedView = mock(ISettingsView.class);
        SettingsPresenter presenter = new SettingsPresenter(mockedModel);
        presenter.attachView(mockedView);
        presenter.signOut();
        verify(mockedModel).signOut();
        verify(mockedView).clearInputErrors();
        verify(mockedView).startWelcomeActivity();
    }

    @Test
    public void changePassword() {
        IUserModel mockedModel = mock(IUserModel.class);
        ISettingsView mockedView = mock(ISettingsView.class);
        SettingsPresenter presenter = new SettingsPresenter(mockedModel);
        presenter.attachView(mockedView);
        ArgumentCaptor<IUserModel.IChangePasswordListener> listener =
                ArgumentCaptor.forClass(IUserModel.IChangePasswordListener.class);
        verify(mockedModel).addChangePasswordListener(listener.capture());
        // clear focus (hide keyboard) and show success message on success
        listener.getValue().onSuccess();
        verify(mockedView).clearFocus();
        verify(mockedView).showMessage(ISettingsView.Message.PASSWORD_CHANGED);
        // on fail show input error
        listener.getValue().onFail(IUserModel.ChangePasswordError.WEAK_PASSWORD);
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
        IUserModel mockedModel = mock(IUserModel.class);
        ISettingsView mockedView = mock(ISettingsView.class);
        SettingsPresenter presenter = new SettingsPresenter(mockedModel);
        presenter.attachView(mockedView);
        ArgumentCaptor<IUserModel.IChangeAppPinListener> listener =
                ArgumentCaptor.forClass(IUserModel.IChangeAppPinListener.class);
        verify(mockedModel).addChangeAppPinListener(listener.capture());
        // clear focus (hide keyboard) and show success message on success
        listener.getValue().onSuccess();
        verify(mockedView).clearFocus();
        verify(mockedView).showMessage(ISettingsView.Message.APP_PIN_CHANGED);
        // on fail show input error
        listener.getValue().onFail(IUserModel.ChangeAppPinError.WEAK_PIN);
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
        IUserModel mockedModel = mock(IUserModel.class);
        ISettingsView mockedView = mock(ISettingsView.class);
        SettingsPresenter presenter = new SettingsPresenter(mockedModel);
        presenter.attachView(mockedView);
        ArgumentCaptor<IUserModel.IChangeClientPinListener> listener =
                ArgumentCaptor.forClass(IUserModel.IChangeClientPinListener.class);
        verify(mockedModel).addChangeClientPinListener(listener.capture());
        // clear focus (hide keyboard) and show success message on success
        listener.getValue().onSuccess();
        verify(mockedView).clearFocus();
        verify(mockedView).showMessage(ISettingsView.Message.CLIENT_PIN_CHANGED);
        // on fail show input error
        listener.getValue().onFail(IUserModel.ChangeClientPinError.WEAK_PIN);
        verify(mockedView).showInputError(ISettingsView.InputError.WEAK_CLIENT_PIN);
        reset(mockedModel, mockedView);
        presenter.changeClientPin("4242");
        verify(mockedView).clearInputErrors();
        verify(mockedModel).changeClientPin(any(String.class));
    }

    @Test
    public void listeners() {
        IUserModel mockedModel = mock(IUserModel.class);
        ISettingsView mockedView = mock(ISettingsView.class);
        SettingsPresenter presenter = new SettingsPresenter(mockedModel);
        presenter.attachView(mockedView);
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
