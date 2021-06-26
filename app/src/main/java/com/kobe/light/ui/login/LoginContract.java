package com.kobe.light.ui.login;

import com.kobe.lib_base.IBasePresenter;
import com.kobe.lib_base.IBaseView;
import com.kobe.light.request.LoginRequest;
import com.kobe.light.response.LoginResponse;


/**
 * <p>Class: com.zilla.android.product.dsp.app.login.NewLoginContract</p>
 * <p>Description: </p>
 * <pre>
 *
 *  </pre>
 *
 * @author lujunjie
 * @date 2018/10/19/16:06.
 */
public interface LoginContract {

    interface view extends IBaseView {

        void loginSuccess(LoginResponse loginResponse);

        void loginFailed(LoginResponse loginResponse);

        void showError();


    }

    interface presenter extends IBasePresenter {

        void login(LoginRequest loginBean);


    }
}
