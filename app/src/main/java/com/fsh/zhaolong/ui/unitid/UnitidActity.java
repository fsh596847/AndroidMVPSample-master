package com.fsh.zhaolong.ui.unitid;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.fsh.zhaolong.R;
import com.fsh.zhaolong.bean.UntidResponse;
import com.fsh.zhaolong.mvp.other.MvpActivity;
import com.fsh.zhaolong.swiperecyclerview.SwipeRecyclerView;
import com.fsh.zhaolong.ui.increse.AddActivity;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by HIPAA on 2016/12/20.  查询交货单位接口
 */

public class UnitidActity extends MvpActivity<UntidPresenter> implements UnitidView {
  private SwipeRecyclerView recyclerView;
  private List<UntidResponse.DataBean> data;
  private RecyclerViewAdapter adapter;
  private int pagerSize = 10;
  private int pagerNum = 1;
  private String userid;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_unitid);
  }

  public void backClick(View view){
    finish();
  }

  @Override protected UntidPresenter createPresenter() {
    return new UntidPresenter(this);
  }

  @Override public void init() {

    recyclerView = (SwipeRecyclerView) findViewById(R.id.swipeRecyclerView);
    //set color
    recyclerView.getSwipeRefreshLayout()
        .setColorSchemeColors(getResources().getColor(R.color.colorPrimary));
    //set layoutManager
    recyclerView.getRecyclerView().setLayoutManager(new LinearLayoutManager(this));
    data = new ArrayList<>();
    adapter = new RecyclerViewAdapter();
    recyclerView.setAdapter(adapter);

    recyclerView.setOnLoadListener(new SwipeRecyclerView.OnLoadListener() {
      @Override
      public void onRefresh() {

        mvpPresenter.queryDeliveryunit(pagerNum);
      }

      @Override
      public void onLoadMore() {
        pagerNum++;
        mvpPresenter.queryDeliveryunit(pagerNum);

      }
    });

    //设置自动下拉刷新，切记要在recyclerView.setOnLoadListener()之后调用
    //因为在没有设置监听接口的情况下，setRefreshing(true),调用不到OnLoadListener
    recyclerView.setRefreshing(true);
  }

  @Override public void getDataSuccess(UntidResponse model) {

    if(model.getTotalRows()>data.size()){
      data.addAll(model.getData());
      recyclerView.complete();
      adapter.notifyDataSetChanged();
    }else {
      recyclerView.onNoMore("-- 没有更过了 --");
      //recyclerView.stopLoadingMore()
      recyclerView.complete();
    }
  }

  @Override public void getDataFail(String msg) {
    recyclerView.complete();
  }

  public void addClick(View view) {

  }

  public void searchClick(View view) {

  }

  private class RecyclerViewAdapter extends RecyclerView.Adapter<ItemViewHolder> {

    @Override
    public int getItemCount() {
      return data == null ? 0 : data.size();
    }

    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
      View view =
          LayoutInflater.from(UnitidActity.this).inflate(R.layout.adapter_unitid, parent, false);
      return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ItemViewHolder holder, final int position) {
      holder.tv.setText(data.get(position).getUnitname()+"");
      //for test item click listener
      holder.tv.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
          Intent intent=new Intent();
          intent.putExtra(AddActivity.INTENT_KEY_UNITNAME,data.get(position));
          setResult(RESULT_OK,intent);
          finish();
        }
      });
    }
  }

  static class ItemViewHolder extends RecyclerView.ViewHolder {

    TextView tv;

    public ItemViewHolder(View view) {
      super(view);
      tv = (TextView) view.findViewById(R.id.tv);
    }
  }
}
