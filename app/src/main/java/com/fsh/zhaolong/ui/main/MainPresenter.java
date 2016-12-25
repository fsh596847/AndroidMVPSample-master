package com.fsh.zhaolong.ui.main;

import com.fsh.zhaolong.bean.MainResponse;
import com.fsh.zhaolong.mvp.other.BasePresenter;
import com.fsh.zhaolong.retrofit.ApiCallback;

/**
 * Created by HIPAA on 2016/12/16.
 */

public class MainPresenter extends BasePresenter<MainView> {

  public MainPresenter(MainView view) {
    attachView(view);
  }

  //public void projectList(Map<String, String> map) {
  //  mvpView.showLoading();
  //  addSubscription(apiStores.projectList(map),
  //      new ApiCallback<MainResponse>() {
  //        @Override
  //        public void onSuccess(MainResponse model) {
  //          mvpView.getDataSuccess(model);
  //        }
  //
  //        @Override
  //        public void onFailure(String msg) {
  //          mvpView.getDataFail(msg);
  //        }
  //
  //        @Override
  //        public void onFinish() {
  //          mvpView.hideLoading();
  //        }
  //      });
  //}

  public void projectList(String userid, String unitid, String varietyid, String varietyname,String P, String pagesiz) {
    mvpView.showLoading();
    addSubscription(apiStores.projectList(userid,unitid,varietyid,varietyname, P,  pagesiz),
        new ApiCallback<String>() {
          @Override
          public void onSuccess(String model) {
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

  //public void projectList(String userid, String unitid, String varietyid, String varietyname,String P, String pagesiz) {
  //  mvpView.showLoading();
  //  addSubscription(apiStores.projectList(userid,unitid,varietyid,varietyname, P,  pagesiz),
  //      new ApiScallback() {
  //        @Override
  //        public void onSuccess(String model) {
  //          mvpView.getDataSuccess(model);
  //        }
  //
  //        @Override
  //        public void onFailure(String msg) {
  //          mvpView.getDataFail(msg);
  //        }
  //
  //        @Override
  //        public void onFinish() {
  //          mvpView.hideLoading();
  //        }
  //      });
  //}
}
