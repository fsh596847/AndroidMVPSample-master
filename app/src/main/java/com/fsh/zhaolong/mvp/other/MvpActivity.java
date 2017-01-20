package com.fsh.zhaolong.mvp.other;

import android.os.Bundle;
import android.widget.Toast;
import com.fsh.zhaolong.ui.BaseActivity;
import com.fsh.zhaolong.util.NetUtil;

/**
 * Created by WuXiaolong on 2016/3/30. github:https://github.com/WuXiaolong/
 * weibo:http://weibo.com/u/2175011601 微信公众号：吴小龙同学 个人博客：http://wuxiaolong.me/
 */
public abstract class MvpActivity<P extends BasePresenter> extends BaseActivity {
  protected P mvpPresenter;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    mvpPresenter = createPresenter();
    super.onCreate(savedInstanceState);
  }

  protected abstract P createPresenter();

  @Override
  protected void onDestroy() {
    super.onDestroy();
    if (mvpPresenter != null) {
      mvpPresenter.detachView();
    }
  }

  public void showLoading() {
    if (NetUtil.isConnected(mActivity)) {
      showProgressDialog();
    } else {
      Toast.makeText(mActivity, "网络异常", Toast.LENGTH_SHORT).show();
    }
  }

  public void hideLoading() {
    dismissProgressDialog();
  }
}
