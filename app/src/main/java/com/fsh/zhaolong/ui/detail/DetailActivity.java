package com.fsh.zhaolong.ui.detail;

import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import butterknife.Bind;
import com.fsh.zhaolong.R;
import com.fsh.zhaolong.bean.DetailResponse;
import com.fsh.zhaolong.mvp.other.MvpActivity;
import com.fsh.zhaolong.util.PreferenceUtils;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by HIPAA on 2016/12/19.
 */

public class DetailActivity extends MvpActivity<DetailPresenter> implements DetailView {

  public static final String INTEN_KEY_HID = "HID";
  public final String JIN = "0";
  public final String GONG_JIN = "1";
  @Bind(R.id.RecyclerView) RecyclerView recyclerView;
  private List<DetailResponse.DataBean> mDatas;
  private DetailAdapter detailAdapter;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_detail);
  }

  @Override protected DetailPresenter createPresenter() {
    return new DetailPresenter(this);
  }

  public void backClick(View view) {
    finish();
  }

  @Override public void init() {
    mDatas = new ArrayList<>();
    recyclerView.setLayoutManager(new LinearLayoutManager(this));
    recyclerView.setItemAnimator(new DefaultItemAnimator());

    Map<String, String> map = new HashMap<>();
    map.put("userid", PreferenceUtils.getPrefString(mActivity, "userid", null));
    map.put("hid", getIntent().getStringExtra(INTEN_KEY_HID));
    mvpPresenter.phoneueryMakCodeDatail(map);
  }

  @Override public void getDataSuccess(DetailResponse model) {
    mDatas = model.getData();
    if (mDatas.size() >= 0) {
      String mea = model.getData().get(0).getMea();
      if (mea.equals(JIN)) {
        detailAdapter = new DetailAdapter(mActivity, mDatas, R.layout.adapter_add_brother);
      } else if (mea.equals(GONG_JIN)) {
        detailAdapter = new DetailAdapter(mActivity, mDatas, R.layout.adapter_add);
      }
      recyclerView.setAdapter(detailAdapter);
    }
  }

  @Override public void getDataFail(String msg) {

  }

}
