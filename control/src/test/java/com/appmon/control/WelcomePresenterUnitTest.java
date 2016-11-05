package com.appmon.control;

import com.appmon.control.models.user.IUserModel;
import com.appmon.control.presenters.WelcomePresenter;
import com.appmon.control.views.IWelcomeView;

import org.junit.Test;

import static org.mockito.Mockito.*;

public class WelcomePresenterUnitTest {

    @Test
    public void userCheckTest() {
        IUserModel mockedModel = mock(IUserModel.class);
        when(mockedModel.getUserID()).thenReturn(null, "user");
        IWelcomeView mockedView = mock(IWelcomeView.class);
        WelcomePresenter presenter = new WelcomePresenter(mockedModel);
        presenter.attachView(mockedView);
        // first invocation with null
        presenter.checkUserState();
        verify(mockedView, never()).startDeviceListActivity();
        // second, with some value
        presenter.checkUserState();
        verify(mockedView).startDeviceListActivity();
        // in model only 2 getUserID calls must be registred
        verify(mockedModel, times(2)).getUserID();
    }

    @Test
    public void loginRegisterActionsTest() {
        IUserModel mockedModel = mock(IUserModel.class);
        IWelcomeView mockedView = mock(IWelcomeView.class);
        WelcomePresenter presenter = new WelcomePresenter(mockedModel);
        presenter.attachView(mockedView);
        presenter.login();
        verify(mockedView).startLoginActivity();
        presenter.register();
        verify(mockedView).startRegisterActivity();
        // model must be untouched
        verifyZeroInteractions(mockedModel);
    }

    @Test
    public void detachedViewTest() {
        IUserModel mockedModel = mock(IUserModel.class);
        IWelcomeView mockedView1 = mock(IWelcomeView.class);
        IWelcomeView mockedView2 = mock(IWelcomeView.class);
        WelcomePresenter presenter = new WelcomePresenter(mockedModel);
        // test attach/detach mechanism
        presenter.attachView(mockedView1);
        presenter.login();
        presenter.detachView();
        presenter.attachView(mockedView2);
        presenter.login();
        verify(mockedView1).startLoginActivity();
        verify(mockedView2).startLoginActivity();
        presenter.detachView();
        presenter.login();
        verifyNoMoreInteractions(mockedView1, mockedView2);
    }

}
