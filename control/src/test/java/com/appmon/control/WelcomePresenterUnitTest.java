package com.appmon.control;

import com.appmon.control.models.user.IUserModel;
import com.appmon.control.presenters.WelcomePresenter;
import com.appmon.control.views.IWelcomeView;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class WelcomePresenterUnitTest {

    @Mock
    private IUserModel mockedModel;
    @Mock
    private IWelcomeView mockedView;

    private WelcomePresenter presenter;

    @Before
    public void setup() {
        when(mockedModel.getUserID()).thenReturn(null, "user");
        when(mockedModel.getAppPin()).thenReturn(null, "1234");
        presenter = new WelcomePresenter(mockedModel);
        presenter.attachView(mockedView);
    }

    @Test
    public void userCheckTest() {
        // first invocation with null user
        presenter.checkUserState();
        verify(mockedView, never()).startDeviceListActivity();
        // not null user but null pin -> open devices
        presenter.checkUserState();
        verify(mockedView).startDeviceListActivity();
        verify(mockedView, never()).setPinFormVisibility(true);
        reset(mockedView);
        // opposite
        presenter.checkUserState();
        verify(mockedView, never()).startDeviceListActivity();
        verify(mockedView, never()).startDeviceListActivity();

        verify(mockedModel, times(3)).getUserID();
        // last 2 times
        verify(mockedModel, times(2)).getAppPin();
    }

    @Test
    public void loginRegisterActionsTest() {
        presenter.login();
        verify(mockedView).startLoginActivity();
        presenter.register();
        verify(mockedView).startRegisterActivity();
        // model must be untouched
        verifyZeroInteractions(mockedModel);
    }

    @Test
    public void logOutTest() {
        presenter.logOut();
        verify(mockedView).setPinFormVisibility(false);
        verify(mockedModel).signOut();
    }

    @Test
    public void postPinTest() {
        // with null user
        presenter.postPin("any symbols");
        verifyNoMoreInteractions(mockedView);
        // with not null user, when pin is not set
        presenter.postPin("some pin");
        verify(mockedView).startDeviceListActivity();
        reset(mockedView);
        // with not null user, with right pin
        presenter.postPin("1234");
        // with not null user, wrong pin
        verify(mockedView).startDeviceListActivity();
        presenter.postPin("4321");
        verifyNoMoreInteractions(mockedView);
    }
    @Test
    public void detachedViewTest() {
        // additional view
        IWelcomeView mockedView2 = mock(IWelcomeView.class);
        // in @Before "mockedView" already attached
        presenter.login();
        // select other view
        presenter.detachView();
        presenter.attachView(mockedView2);
        presenter.login();
        verify(mockedView).startLoginActivity();
        verify(mockedView2).startLoginActivity();
        presenter.detachView();
        presenter.login();
        verifyNoMoreInteractions(mockedView, mockedView2);
    }

}
