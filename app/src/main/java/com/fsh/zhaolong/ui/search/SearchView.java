package com.fsh.zhaolong.ui.search;

import com.fsh.zhaolong.bean.AddResponse;
import com.fsh.zhaolong.mvp.main.BaseView;

/**
 * Created by HIPAA on 2016/12/16.
 */

public interface SearchView extends BaseView{

  void getDataSuccess(AddResponse model);

  void getSearchSuccess(AddResponse model);
  void getDataFail(String msg);
}
