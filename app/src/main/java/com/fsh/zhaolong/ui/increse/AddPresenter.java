package com.fsh.zhaolong.ui.increse;

import com.fsh.zhaolong.bean.AddProjrctResponse;
import com.fsh.zhaolong.bean.AddResponse;
import com.fsh.zhaolong.bean.AddResponseSuccessful;
import com.fsh.zhaolong.mvp.other.BasePresenter;
import com.fsh.zhaolong.retrofit.ApiCallback;
import java.util.Map;

/**
 * Created by HIPAA on 2016/12/16.
 */

public class AddPresenter extends BasePresenter<AddView> {

  public AddPresenter(AddView view) {
    attachView(view);
  }

  public void phoneAddMakCode(Map<String, String> map) {
    mvpView.showLoading();
    addSubscription(apiStores.phoneAddMakCode(map),
        new ApiCallback<AddResponseSuccessful>() {
          @Override
          public void onSuccess(AddResponseSuccessful model) {
            mvpView.getRecponse(model);
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

  public void queryVariety(int p,int pagesize) {
    mvpView.showLoading();
    addSubscription(apiStores.queryVariety(p,pagesize),
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
  public void getProjectRecponse() {
    mvpView.showLoading();
    addSubscription(apiStores.phoneQueryProject(),
        new ApiCallback<AddProjrctResponse>() {
          @Override
          public void onSuccess(AddProjrctResponse model) {
            mvpView.getProjectRecponse(model);
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

  public void phoneAddMakCode(String userid,
      String unitname,
      String unitid,
      double totalwaste,
      String remark,
      String mea,
      String peel,
      String deliveryaddress,
      String dataList,String projectid) {
    mvpView.showLoading();
    addSubscription(apiStores.phoneAddMakCode(userid,
        unitname,
        unitid,
        totalwaste,
        remark,
        mea,
        peel,
        deliveryaddress,
        dataList, projectid),
        new ApiCallback<AddResponseSuccessful>() {
          @Override
          public void onSuccess(AddResponseSuccessful model) {
            mvpView.getRecponse(model);
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
