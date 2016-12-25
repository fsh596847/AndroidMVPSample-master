package com.fsh.zhaolong.ui.increse;

import com.fsh.zhaolong.bean.AddProjrctResponse;
import com.fsh.zhaolong.bean.AddResponse;
import com.fsh.zhaolong.bean.AddResponseSuccessful;
import com.fsh.zhaolong.mvp.main.BaseView;

/**
 * Created by HIPAA on 2016/12/16.
 */

public interface AddView extends BaseView {

  void getDataSuccess(AddResponse model);
  void getRecponse(AddResponseSuccessful model);
  void getProjectRecponse(AddProjrctResponse model);
  void getDataFail(String msg);
}
