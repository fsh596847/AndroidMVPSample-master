package com.fsh.zhaolong.ui.login;

import com.fsh.zhaolong.mvp.other.BasePresenter;
import com.fsh.zhaolong.retrofit.ApiScallback;
import java.util.Map;

/**
 * Created by HIPAA on 2016/12/16.
 */

public class LoginPresenter extends BasePresenter<LoginView> {

  public LoginPresenter(LoginView view) {
    attachView(view);
  }

  public void login(String username,String pasw) {
    mvpView.showLoading();
    addSubscription(apiStores.login(username, pasw),
        new ApiScallback() {
          @Override public void onSuccess(String model) {
            mvpView.getDataSuccess(model);
          }

          @Override public void onFailure(String msg) {
            mvpView.getDataFail(msg);
          }

          @Override public void onFinish() {
            mvpView.hideLoading();
          }
        });
  }

  public void login(Map<String, String> map) {
    mvpView.showLoading();
    addSubscription(apiStores.login(map),
        new ApiScallback() {
          @Override public void onSuccess(String model) {
            mvpView.getDataSuccess(model);
          }

          @Override public void onFailure(String msg) {
            mvpView.getDataFail(msg);
          }

          @Override public void onFinish() {
            mvpView.hideLoading();
          }
        });
  }
}
