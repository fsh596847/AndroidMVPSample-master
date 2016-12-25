package com.fsh.zhaolong.ui.unitid;

import com.fsh.zhaolong.bean.UntidResponse;
import com.fsh.zhaolong.mvp.main.BaseView;

/**
 * Created by HIPAA on 2016/12/16.
 */

public interface UnitidView extends BaseView{

  void getDataSuccess(UntidResponse model);

  void getDataFail(String msg);
}
