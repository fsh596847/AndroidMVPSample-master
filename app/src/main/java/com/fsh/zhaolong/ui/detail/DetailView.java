package com.fsh.zhaolong.ui.detail;

import com.fsh.zhaolong.bean.DetailResponse;
import com.fsh.zhaolong.mvp.main.BaseView;

/**
 * Created by HIPAA on 2016/12/16.
 */

public interface DetailView extends BaseView{

  void getDataSuccess(DetailResponse model);

  void getDataFail(String msg);
}
