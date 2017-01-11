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
import com.fsh.zhaolong.ui.view.TalertDialog;
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
  }

  @Override protected void onResume() {
    super.onResume();
    data.clear();
    pagerNum = 1;
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
    Toast.makeText(mActivity, "暂未开通", Toast.LENGTH_SHORT).show();
    //Map<String, String> map = new HashMap<>();
    //map.put("userid", userid);
    //map.put("hid", mHid);
    //if (i == 0) {
    //  Intent intent = new Intent(mActivity, EditActivity.class);
    //  intent.putExtra(INTENT_KEY_EDIT, data.get(mPosition));
    //  intent.putExtra(DetailActivity.INTEN_KEY_HID, data.get(mPosition).getHid());
    //  startActivity(intent);
    //} else {
    //  mvpPresenter.deleteList(map);
    //}
  }

  @Override public void call(String hid, int position) {
    mHid = hid;
    mPosition = position;
  }

  @Override public void del(String hid, int position) {
    mHid = hid;
    mPosition = position;
    new TalertDialog(mActivity).builder().setTitle("温馨提示").setMsg("确认删除吗").setNegativeButton("",
        new View.OnClickListener() {
          @Override public void onClick(View view) {

          }
        }).setPositiveButton("", new View.OnClickListener() {
      @Override public void onClick(View view) {
        Map<String, String> map = new HashMap<>();
        map.put("userid", userid);
        map.put("hid", mHid);
        mvpPresenter.deleteList(map);
      }
    }).show();
  }

  @Override public void edit(String hid, int position) {
    mHid = hid;
    mPosition = position;
    Intent intent = new Intent(mActivity, EditActivity.class);
    intent.putExtra(INTENT_KEY_EDIT, data.get(mPosition));
    intent.putExtra(DetailActivity.INTEN_KEY_HID, data.get(mPosition).getHid());
    startActivity(intent);
  }
}
