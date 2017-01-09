package com.fsh.zhaolong.ui.main.edit;

import com.fsh.zhaolong.bean.AddProjrctResponse;
import com.fsh.zhaolong.bean.AddResponse;
import com.fsh.zhaolong.bean.AddResponseSuccessful;
import com.fsh.zhaolong.bean.DetailResponse;
import com.fsh.zhaolong.mvp.main.BaseView;

/**
 * Created by HIPAA on 2016/12/16.
 */

public interface EditView extends BaseView {

  void getDataSuccess(AddResponse model);

  void getRecponse(AddResponseSuccessful model);

  void getProjectRecponse(AddProjrctResponse model);

  void getDataFail(String msg);

  void getDataSuccess(DetailResponse model);
}
