package com.fsh.zhaolong.ui.test;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.GridView;
import butterknife.Bind;
import com.fsh.zhaolong.R;
import com.fsh.zhaolong.bean.AddProjrctResponse;
import com.fsh.zhaolong.bean.AddResponse;
import com.fsh.zhaolong.bean.AddResponseSuccessful;
import com.fsh.zhaolong.mvp.other.MvpActivity;
import com.fsh.zhaolong.ui.increse.AddPresenter;
import com.fsh.zhaolong.ui.increse.AddView;
import com.fsh.zhaolong.ui.increse.ProjectAdapter;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by HIPAA on 2016/12/23.
 */

public class TestActivity extends MvpActivity<AddPresenter>
    implements AddView,ProjectAdapter.Freshen {
  @Bind(R.id.RecyclerView) GridView RecyclerView;

  private SubValuesRecycleAdapter projectAdapter;
  private List<AddProjrctResponse.DataBean> mProjects = new ArrayList<>();
  @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_test);
    mvpPresenter.getProjectRecponse();
  }

  @Override protected AddPresenter createPresenter() {
    return new AddPresenter(this);
  }

  @Override public void init() {

  }

  @Override public void getDataSuccess(AddResponse model) {

  }

  @Override public void getRecponse(AddResponseSuccessful model) {

  }

  @Override public void getProjectRecponse(AddProjrctResponse model) {
    mProjects = model.getData();
    projectAdapter = new SubValuesRecycleAdapter(mProjects, mActivity);
    projectAdapter.setFreshen(this);
    RecyclerView.setAdapter(projectAdapter);
  }

  @Override public void getDataFail(String msg) {

  }

  @Override public void freshen(List<AddProjrctResponse.DataBean> list) {
    this.mProjects = list;
    projectAdapter.notifyDataSetChanged();
  }
}
