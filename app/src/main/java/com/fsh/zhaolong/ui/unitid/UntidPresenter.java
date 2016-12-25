package com.fsh.zhaolong.ui.unitid;

import com.fsh.zhaolong.bean.UntidResponse;
import com.fsh.zhaolong.mvp.other.BasePresenter;
import com.fsh.zhaolong.retrofit.ApiCallback;

/**
 * Created by HIPAA on 2016/12/16.
 */

public class UntidPresenter extends BasePresenter<UnitidView> {

  public UntidPresenter(UnitidView view) {
    attachView(view);
  }

  public void queryDeliveryunit(int p) {
    mvpView.showLoading();
    addSubscription(apiStores.queryDeliveryunit(p),
        new ApiCallback<UntidResponse>() {
          @Override
          public void onSuccess(UntidResponse model) {
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
