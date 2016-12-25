package com.fsh.zhaolong.ui.detail;

import com.fsh.zhaolong.bean.DetailResponse;
import com.fsh.zhaolong.mvp.other.BasePresenter;
import com.fsh.zhaolong.retrofit.ApiCallback;
import java.util.Map;

/**
 * Created by HIPAA on 2016/12/16.
 */

public class DetailPresenter extends BasePresenter<DetailView> {

  public DetailPresenter(DetailView view) {
    attachView(view);
  }


  public void phoneueryMakCodeDatail(Map<String, String> map) {
    mvpView.showLoading();
    addSubscription(apiStores.phoneueryMakCodeDatail(map),
        new ApiCallback<DetailResponse>() {
          @Override
          public void onSuccess(DetailResponse model) {
            mvpView.getDataSuccess(model);
          }

          @Override
          public void onFailure(String msg) {
            mvpView.getDataFail(msg);
          }

          @Override
          public void onFinish() {
            mvpView.hideLoading();
          }
        });
  }


}
