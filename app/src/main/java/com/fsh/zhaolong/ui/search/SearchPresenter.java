package com.fsh.zhaolong.ui.search;

import com.fsh.zhaolong.bean.AddResponse;
import com.fsh.zhaolong.mvp.other.BasePresenter;
import com.fsh.zhaolong.retrofit.ApiCallback;
import java.util.Map;

/**
 * Created by HIPAA on 2016/12/16.
 */

public class SearchPresenter extends BasePresenter<SearchView> {

  public SearchPresenter(SearchView view) {
    attachView(view);
  }


  public void phoneueryMakCodeDatail(Map<String, String> map) {
    mvpView.showLoading();
    addSubscription(apiStores.phoneueryMakCodeDatail(map),
        new ApiCallback<AddResponse>() {
          @Override
          public void onSuccess(AddResponse model) {
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
  public void queryVariety(int p) {
    mvpView.showLoading();
    addSubscription(apiStores.queryVariety(p,200),
        new ApiCallback<AddResponse>() {
          @Override
          public void onSuccess(AddResponse model) {
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
