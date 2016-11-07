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
        presenter = new WelcomePresenter(mockedModel);
        presenter.attachView(mockedView);
    }

    @Test
    public void userCheckTest() {
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
        presenter.login();
        verify(mockedView).startLoginActivity();
        presenter.register();
        verify(mockedView).startRegisterActivity();
        // model must be untouched
        verifyZeroInteractions(mockedModel);
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
