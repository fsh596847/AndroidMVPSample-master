package com.fsh.zhaolong.retrofit;

import com.wuxiaolong.androidutils.library.LogUtil;
import retrofit2.adapter.rxjava.HttpException;
import rx.Subscriber;

public abstract class ApiScallback extends Subscriber<String> {

  public abstract void onSuccess(String model);

  public abstract void onFailure(String msg);

  public abstract void onFinish();
  @Override public void onCompleted() {
    onFinish();
  }

  @Override public void onError(Throwable e) {
      e.printStackTrace();
      if (e instanceof HttpException) {
        HttpException httpException = (HttpException) e;
        //httpException.response().errorBody().string()
        int code = httpException.code();
        String msg = httpException.getMessage();
        LogUtil.d("code=" + code);
        if (code == 504) {
          msg = "网络不给力";
        }
        if (code == 502 || code == 404) {
          msg = "服务器异常，请稍后再试";
        }
        onFailure(msg);
      } else {
        onFailure(e.getMessage());
      }
      onFinish();
  }

  @Override public void onNext(String o) {
    onSuccess(o);
  }
}
  //public abstract void onSuccess(String model);
  //
  //public abstract void onFailure(String msg);
  //
  //public abstract void onFinish();
  //
  //@Override
  //public void onError(Throwable e) {
  //  e.printStackTrace();
  //  if (e instanceof HttpException) {
  //    HttpException httpException = (HttpException) e;
  //    //httpException.response().errorBody().string()
  //    int code = httpException.code();
  //    String msg = httpException.getMessage();
  //    LogUtil.d("code=" + code);
  //    if (code == 504) {
  //      msg = "网络不给力";
  //    }
  //    if (code == 502 || code == 404) {
  //      msg = "服务器异常，请稍后再试";
  //    }
  //    onFailure(msg);
  //  } else {
  //    onFailure(e.getMessage());
  //  }
  //  onFinish();
  //}
  //
  //@Override
  //public void onNext(Object model) {
  //  String s=(String)model;
  //  onSuccess(s);
  //}
  //
  //@Override
  //public void onCompleted() {
  //  onFinish();
  //}
