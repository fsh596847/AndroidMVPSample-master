package com.fsh.zhaolong.ui.main;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;
import com.fsh.zhaolong.R;
import com.fsh.zhaolong.bean.FailSuccesssful;
import com.fsh.zhaolong.bean.MainResponse;
import com.fsh.zhaolong.mvp.other.MvpActivity;
import com.fsh.zhaolong.swiperecyclerview.SwipeRecyclerView;
import com.fsh.zhaolong.ui.detail.DetailActivity;
import com.fsh.zhaolong.ui.increse.AddActivity;
import com.fsh.zhaolong.ui.main.edit.EditActivity;
import com.fsh.zhaolong.ui.search.SearchActivity;
import com.fsh.zhaolong.ui.view.MainAlertDialog;
import com.fsh.zhaolong.util.Const;
import com.fsh.zhaolong.util.PreferenceUtils;
import com.google.gson.Gson;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.json.JSONObject;

/**
 * Created by HIPAA on 2016/12/19.
 */

public class MainActivity extends MvpActivity<MainPresenter>
    implements MainView, MainAlertDialog.CallPayType, MainAdapter.CallBack {
  //交货单位
  public static final String INTENT_KEY_UNTID = "UNTID";
  //请选择品种
  public static final String INTENT_KEY_BREED = "BREED";
  public static final String INTENT_KEY_BREEDNAME = "BREEDNAME";
  //传递编辑
  public static final String INTENT_KEY_EDIT = "EDIT";
  private SwipeRecyclerView recyclerView;
  private List<MainResponse.DataBean> data;
  private MainAdapter adapter;
  private int totalPage;
  private int totalSize;
  private int pagerNum = 1;
  private String userid;
  private String mHid;
  private int mPosition;
  //对话框
  private List<String> listItem = new ArrayList<>();

  public static void CallIntent(Context context, String UNTID, String BREED, String BREEDName) {
    Intent intent = new Intent(context, MainActivity.class);
    intent.putExtra(INTENT_KEY_UNTID, UNTID);
    intent.putExtra(INTENT_KEY_BREED, BREED);
    intent.putExtra(INTENT_KEY_BREEDNAME, BREEDName);
    context.startActivity(intent);
  }

  public static void CallIntent(Context context, String UNTID, String BREEDName) {
    Intent intent = new Intent(context, MainActivity.class);
    intent.putExtra(INTENT_KEY_UNTID, UNTID);
    intent.putExtra(INTENT_KEY_BREEDNAME, BREEDName);
    context.startActivity(intent);
  }

  public static void CallIntent1(Context context, String BREED, String BREEDName) {
    Intent intent = new Intent(context, MainActivity.class);
    intent.putExtra(INTENT_KEY_BREED, BREED);
    intent.putExtra(INTENT_KEY_BREEDNAME, BREEDName);
    context.startActivity(intent);
  }

  public static void CallIntent2(Context context, String BREEDName) {
    Intent intent = new Intent(context, MainActivity.class);
    intent.putExtra(INTENT_KEY_BREEDNAME, BREEDName);
    context.startActivity(intent);
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
  }

  @Override protected MainPresenter createPresenter() {
    return new MainPresenter(this);
  }

  @Override public void init() {
    listItem.add("编辑");
    listItem.add("删除");
    recyclerView = (SwipeRecyclerView) findViewById(R.id.swipeRecyclerView);
    //set color
    recyclerView.getSwipeRefreshLayout()
        .setColorSchemeColors(getResources().getColor(R.color.colorPrimary));
    //set layoutManager
    recyclerView.getRecyclerView().setLayoutManager(new LinearLayoutManager(this));
    data = new ArrayList<>();
    adapter = new MainAdapter(data, mActivity, listItem);
    adapter.setCallBack(this);
    recyclerView.setAdapter(adapter);
    userid = PreferenceUtils.getPrefString(mActivity, "userid", null);

    final String unitid = getIntent().getStringExtra(INTENT_KEY_UNTID);
    final String varietyid = getIntent().getStringExtra(INTENT_KEY_BREED);
    final String varietyidName = getIntent().getStringExtra(INTENT_KEY_BREEDNAME);

    final Map<String, String> map = getMap(unitid, varietyid, varietyidName);

    recyclerView.setOnLoadListener(new SwipeRecyclerView.OnLoadListener() {
      @Override
      public void onRefresh() {
        mvpPresenter.projectList(map);
        //mvpPresenter.projectList(userid, unitid, varietyid, varietyidName, pagerNum + "", "15");
      }

      @Override
      public void onLoadMore() {
        pagerNum++;
        if (pagerNum <= totalPage) {
          final Map<String, String> map = getMap(unitid, varietyid, varietyidName);
          mvpPresenter.projectList(map);
          //mvpPresenter.projectList(userid, unitid, varietyid, varietyidName, pagerNum + "", "15");
        } else {
          recyclerView.onNoMore("没有更多了");
          recyclerView.complete();
        }
      }
    });

    //设置自动下拉刷新，切记要在recyclerView.setOnLoadListener()之后调用
    //因为在没有设置监听接口的情况下，setRefreshing(true),调用不到OnLoadListener
    recyclerView.setRefreshing(true);
  }

  @NonNull
  private Map<String, String> getMap(String unitid, String varietyid, String varietyidName) {
    final Map<String, String> map = new HashMap<>();
    map.put("userid", userid);
    if (!TextUtils.isEmpty(unitid)) {
      map.put("unitid", unitid);
    }
    if (!TextUtils.isEmpty(varietyid)) {
      map.put("varietyid", varietyid);
    }
    if (!TextUtils.isEmpty(getIntent().getStringExtra(INTENT_KEY_BREEDNAME))) {
      map.put("varietyname", getIntent().getStringExtra(INTENT_KEY_BREEDNAME));
    }
    map.put("p", pagerNum + "");
    map.put("pagesize", "15");

    return map;
  }

  @Override public void getDataSuccess(String model) {
    try {
      if (!TextUtils.isEmpty(model)) {
        JSONObject jsonObject = new JSONObject(model);
        int status = jsonObject.getInt("status");
        if (status == Const.SUNNESS_STATUE) {
          MainResponse mainResponse = new Gson().fromJson(model, MainResponse.class);
          totalPage = mainResponse.getTotalPage();
          totalSize = mainResponse.getTotalRows();
          if (data.size() < totalSize) {
            data.addAll(mainResponse.getData());
            adapter.notifyDataSetChanged();
          } else {
            recyclerView.onNoMore("没有更多了");
          }
        } else {
          FailSuccesssful failSuccesssful = new Gson().fromJson(model, FailSuccesssful.class);
          Toast.makeText(mActivity, failSuccesssful.getData(), Toast.LENGTH_SHORT).show();
        }
        recyclerView.complete();
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @Override public void getDataFail(String msg) {
    recyclerView.complete();
  }

  @Override public void getDeleteMsg(String msg) {
    FailSuccesssful failSuccesssful = new Gson().fromJson(msg, FailSuccesssful.class);
    if (failSuccesssful.getStatus() == Const.SUNNESS_STATUE) {
      data.remove(mPosition);
      adapter.notifyDataSetChanged();
    }
    Toast.makeText(mActivity, failSuccesssful.getData(), Toast.LENGTH_SHORT).show();
  }

  public void addClick(View view) {
    Intent intent = new Intent(mActivity, AddActivity.class);
    startActivity(intent);
  }

  public void searchClick(View view) {
    Intent intent = new Intent(mActivity, SearchActivity.class);
    startActivity(intent);
  }

  @Override public void callback(String dataBean, int i) {
    Map<String, String> map = new HashMap<>();
    map.put("userid", userid);
    map.put("hid", mHid);
    if (i == 0) {
      Intent intent = new Intent(mActivity, EditActivity.class);
      intent.putExtra(INTENT_KEY_EDIT, data.get(mPosition));
      intent.putExtra(DetailActivity.INTEN_KEY_HID, data.get(mPosition).getHid());
      startActivity(intent);
    } else {
      mvpPresenter.deleteList(map);
    }
  }

  @Override public void call(String hid, int position) {
    mHid = hid;
    mPosition = position;
  }

  //static class ItemViewHolder extends RecyclerView.ViewHolder {
  //
  //  LinearLayout lyt;
  //  TextView tv1;
  //  TextView tv2;
  //  TextView tv3;
  //  TextView tv4;
  //  TextView tv5;
  //  TextView tv6;
  //  TextView tv7;
  //  TextView tv8;
  //  TextView tv9;
  //  TextView tv10;
  //  Button btn;
  //
  //  public ItemViewHolder(View view) {
  //    super(view);
  //    lyt = (LinearLayout) view.findViewById(R.id.lyt);
  //    tv1 = (TextView) view.findViewById(R.id.tv1);
  //    tv2 = (TextView) view.findViewById(R.id.tv2);
  //    tv3 = (TextView) view.findViewById(R.id.tv3);
  //    tv4 = (TextView) view.findViewById(R.id.tv4);
  //    tv5 = (TextView) view.findViewById(R.id.tv5);
  //    tv6 = (TextView) view.findViewById(R.id.tv6);
  //    tv7 = (TextView) view.findViewById(R.id.tv7);
  //    tv8 = (TextView) view.findViewById(R.id.tv8);
  //    tv9 = (TextView) view.findViewById(R.id.tv9);
  //    tv10 = (TextView) view.findViewById(R.id.tv10);
  //    btn = (Button) view.findViewById(R.id.tv11);
  //  }
  //}
  //
  //private class RecyclerViewAdapter extends RecyclerView.Adapter<ItemViewHolder> {
  //
  //  @Override
  //  public int getItemCount() {
  //    return data == null ? 0 : data.size();
  //  }
  //
  //  @Override
  //  public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
  //    View view =
  //        LayoutInflater.from(MainActivity.this)
  //            .inflate(R.layout.adapter_layout_item, parent, false);
  //    return new ItemViewHolder(view);
  //  }
  //
  //  @Override
  //  public void onBindViewHolder(final ItemViewHolder holder, final int position) {
  //
  //    holder.tv1.setText(data.get(position).getBillcode());
  //    holder.tv2.setText(data.get(position).getUnitname());
  //    holder.tv3.setText(data.get(position).getVarietyname());
  //
  //    if (TextUtils.isEmpty(data.get(position).getTareweight())) {
  //      holder.tv4.setText("--");
  //    } else {
  //      holder.tv4.setText(data.get(position).getSpec());
  //    }
  //
  //    holder.tv5.setText(data.get(position).getPeel());
  //
  //    if (TextUtils.isEmpty(data.get(position).getTotalnum())) {
  //      holder.tv6.setText("--");
  //    } else {
  //      holder.tv6.setText(data.get(position).getTotalnum());
  //    }
  //    holder.tv7.setText(data.get(position).getTotalgrossweight());
  //    holder.tv8.setText(data.get(position).getTotaltareweight());
  //    holder.tv9.setText(data.get(position).getTotalnetweight());
  //
  //    if (TextUtils.isEmpty(data.get(position).getTotalwaste())) {
  //      holder.tv10.setText("--");
  //    } else {
  //      holder.tv10.setText(data.get(position).getTotalwaste());
  //    }
  //    holder.lyt.setOnClickListener(new View.OnClickListener() {
  //      @Override public void onClick(View view) {
  //        Intent intent = new Intent(mActivity, DetailActivity.class);
  //        intent.putExtra(DetailActivity.INTEN_KEY_HID, data.get(position).getHid());
  //        startActivity(intent);
  //      }
  //    });
  //    holder.btn.setOnClickListener(new View.OnClickListener() {
  //      @Override public void onClick(View view) {
  //        mHid = data.get(position).getHid();
  //        mPosition=position;
  //        MainAlertDialog alertDialog = new MainAlertDialog(mActivity).builder();
  //        alertDialog.setTitle("温馨提示");
  //        alertDialog.setList(listItem);
  //        alertDialog.setCallback(MainActivity.this);
  //        alertDialog.show();
  //      }
  //    });
  //  }
  //}
}
