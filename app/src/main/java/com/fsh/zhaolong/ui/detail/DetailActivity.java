package com.fsh.zhaolong.ui.detail;

import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
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

  @Bind(R.id.RecyclerView) RecyclerView recyclerView;
  public static final String INTEN_KEY_HID = "HID";
  private List<DetailResponse.DataBean> mDatas;
  private Myadapter myadapter;
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
    myadapter = new Myadapter();
    recyclerView.setAdapter(myadapter);
    Map<String, String> map = new HashMap<>();
    map.put("userid", PreferenceUtils.getPrefString(mActivity, "userid", null));
    map.put("hid", getIntent().getStringExtra(INTEN_KEY_HID));
    mvpPresenter.phoneueryMakCodeDatail(map);
  }

  @Override public void getDataSuccess(DetailResponse model) {
    mDatas.addAll(model.getData());
    myadapter.notifyDataSetChanged();

  }

  @Override public void getDataFail(String msg) {

  }

  class Myadapter extends RecyclerView.Adapter<Myadapter.MyViewHolder> {

    @Override
    public Myadapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
      Myadapter.MyViewHolder
          holder = new Myadapter.MyViewHolder(LayoutInflater.from(
          DetailActivity.this).inflate(R.layout.adapter_detail, parent,
          false));
      return holder;
    }

    @Override
    public void onBindViewHolder(Myadapter.MyViewHolder holder, final int position) {
      holder.tv1.setText(mDatas.get(position).getVarietyname());
      holder.tv2.setText(mDatas.get(position).getSpec()+"");
      holder.tv3.setText(mDatas.get(position).getGrossweight()+"");

    }

    @Override
    public int getItemCount() {
      return mDatas.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

      TextView tv1;
      TextView tv2;
      TextView tv3;

      public MyViewHolder(View view) {
        super(view);
        tv1 = (TextView) view.findViewById(R.id.tv1);
        tv2 = (TextView) view.findViewById(R.id.tv2);
        tv3 = (TextView) view.findViewById(R.id.tv3);
      }
    }
  }
}
