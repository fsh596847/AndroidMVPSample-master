package com.fsh.zhaolong.ui.detail;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.fsh.zhaolong.R;
import com.fsh.zhaolong.bean.DetailResponse;
import java.util.List;

/**
 * Created by HIPAA on 2017/1/13.
 */

public class DetailAdapter extends RecyclerView.Adapter<DetailAdapter.MyViewHolder> {

  private Context context;
  private List<DetailResponse.DataBean> mDatas;
  private int layout;

  public DetailAdapter(Context context,
      List<DetailResponse.DataBean> mDatas, int layout) {
    this.context = context;
    this.mDatas = mDatas;
    this.layout = layout;
  }

  @Override
  public DetailAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    DetailAdapter.MyViewHolder
        holder = new DetailAdapter.MyViewHolder(LayoutInflater.from(
        context).inflate(layout, parent,
        false));
    return holder;
  }

  @Override
  public void onBindViewHolder(DetailAdapter.MyViewHolder holder, final int position) {
    holder.tv1.setText(mDatas.get(position).getVarietyname());
    holder.tv2.setText(mDatas.get(position).getSpec() + "");
    holder.tv3.setText(mDatas.get(position).getGrossweight() + "");
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
