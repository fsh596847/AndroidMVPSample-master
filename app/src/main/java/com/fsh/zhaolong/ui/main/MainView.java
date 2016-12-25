package com.fsh.zhaolong.ui.main;

import com.fsh.zhaolong.mvp.main.BaseView;

/**
 * Created by HIPAA on 2016/12/16.
 */

public interface MainView extends BaseView{

  void getDataSuccess(String model);

  void getDataFail(String msg);
}
