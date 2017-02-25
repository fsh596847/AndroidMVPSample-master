package com.fsh.zhaolong.ui.detail;

import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import butterknife.Bind;
import com.fsh.zhaolong.R;
import com.fsh.zhaolong.bean.DetailResponse;
import com.fsh.zhaolong.bean.MainResponse;
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
  private List<DetailResponse.DataBean> mDatas;
  private DetailAdapter detailAdapter;
  @Bind(R.id.RecyclerView) RecyclerView recyclerView;
  //交货单位
  @Bind(R.id.tv1) TextView tv1;
  //地址
  @Bind(R.id.tvaddress) TextView tv;
  //去皮
  @Bind(R.id.tv3) TextView tvPeel;
  //单位
  @Bind(R.id.tv2) TextView tv2;
  //净重
  @Bind(R.id.netWeight) TextView tvNetWeight;
  //时间
  @Bind(R.id.tvDate) TextView tvDate;
  @Bind(R.id.tvproject) TextView tvproject;
  //备注
  @Bind(R.id.etRemarks) TextView etRemarks;
  //件数
  @Bind(R.id.numPage) TextView mTvNum;
  //毛重
  @Bind(R.id.roughWeight) TextView mTvRoughWeight;
  //皮重
  @Bind(R.id.tareWeight) TextView mTvTareWeight;
  //废品
  @Bind(R.id.wasteProduct) TextView mTvWasteProductEt;
  //实重
  @Bind(R.id.trueWeight) TextView mTvTrueWeight;
  //单位
  private String code;
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
    MainResponse.DataBean dataBean =
        (MainResponse.DataBean) getIntent().getSerializableExtra(INTEN_KEY_HID);
    Map<String, String> map = new HashMap<>();
    map.put("userid", PreferenceUtils.getPrefString(mActivity, "userid", null));
    map.put("hid", dataBean.getHid());
    mvpPresenter.phoneueryMakCodeDatail(map);
    tvproject.setText(dataBean.getProjectname());
    tv1.setText(dataBean.getUnitname());
    tv.setText(dataBean.getDeliveryaddress());
    tvDate.setText(dataBean.getBilldate());
    tvPeel.setText(dataBean.getPeel());
    etRemarks.setText(dataBean.getRemark());
    code = dataBean.getMea();
    if (code.equals("1")) {
      tv2.setText(getString(R.string.add_text_gongjin));
    } else {
      tv2.setText(getString(R.string.add_text_jin));
    }
    tvNetWeight.setText("净重：" + dataBean.getTotalnetweight());
    mTvRoughWeight.setText("毛重：" + dataBean.getTotalgrossweight());
    mTvTareWeight.setText("皮重：" + dataBean.getPeel());
    mTvWasteProductEt.setText("废品：" + dataBean.getTotalwaste());
    mTvTrueWeight.setText("实重：" + dataBean.getTotalrealweight());
    mTvNum.setText("件数：" + dataBean.getTotalnum());
  }

  @Override public void getDataSuccess(DetailResponse model) {
    mDatas = model.getData();
    if (mDatas.size() >= 0) {
      analyzeCode();
      if (code.equals(JIN)) {
        detailAdapter = new DetailAdapter(mActivity, mDatas, R.layout.adapter_detail_brother);
      } else if (code.equals(GONG_JIN)) {
        detailAdapter = new DetailAdapter(mActivity, mDatas, R.layout.adapter_detail);
      }
      recyclerView.setAdapter(detailAdapter);
    }
  }

  @Override public void getDataFail(String msg) {

  }

  /**
   * 根据 斤和公斤 初始化list
   */
  private List<DetailResponse.DataBean> analyzeCode() {
    if (code.equals(JIN)) {//斤
      for (int i1 = 0; i1 < mDatas.size(); i1++) {
        DetailResponse.DataBean addItem = mDatas.get(i1);
        if (addItem.getSpec() > 0) {
          addItem.setSpec(addItem.getSpec() * 2);
        }
        if (addItem.getGrossweight() > 0) {
          addItem.setGrossweight(addItem.getGrossweight() * 2);
        }
      }
    } else if (code.equals(GONG_JIN)) {//公斤
      for (int i1 = 0; i1 < mDatas.size(); i1++) {
        DetailResponse.DataBean addItem = mDatas.get(i1);
        if (addItem.getSpec() > 0) {
          addItem.setSpec(addItem.getSpec() / 2);
        }
        if (addItem.getGrossweight() > 0) {
          addItem.setGrossweight(addItem.getGrossweight() / 2);
        }
      }
    }
    return mDatas;
  }

}
