package com.fsh.zhaolong.ui.main;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.fsh.zhaolong.R;
import com.fsh.zhaolong.bean.MainResponse;
import com.fsh.zhaolong.ui.detail.DetailActivity;
import java.util.List;

/**
 * Created by HIPAA on 2017/1/6.
 */

public class MainAdapter extends RecyclerView.Adapter<MainAdapter.ItemViewHolder> {

  CallBack callBack;
  private List<MainResponse.DataBean> data;
  private Context context;
  private List<String> listItem;

  public MainAdapter(List<MainResponse.DataBean> data,
      Context context, List<String> listItem) {
    this.data = data;
    this.context = context;
    this.listItem = listItem;
  }

  @Override
  public int getItemCount() {
    return data == null ? 0 : data.size();
  }

  @Override
  public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    View view =
        LayoutInflater.from(context)
            .inflate(R.layout.adapter_layout_item, parent, false);
    return new ItemViewHolder(view);
  }

  public void setCallBack(CallBack callBack) {
    this.callBack = callBack;
  }

  @Override
  public void onBindViewHolder(final ItemViewHolder holder, final int position) {

    holder.tv1.setText(data.get(position).getBillcode());
    holder.tv2.setText(data.get(position).getUnitname());
    holder.tv3.setText(data.get(position).getVarietyname());

    if (TextUtils.isEmpty(data.get(position).getTareweight())) {
      holder.tv4.setText("--");
    } else {
      holder.tv4.setText(data.get(position).getSpec());
    }

    holder.tv5.setText(data.get(position).getPeel());

    if (TextUtils.isEmpty(data.get(position).getTotalnum())) {
      holder.tv6.setText("--");
    } else {
      holder.tv6.setText(data.get(position).getTotalnum());
    }
    holder.tv7.setText(data.get(position).getTotalgrossweight());
    holder.tv8.setText(data.get(position).getTotaltareweight());
    holder.tv9.setText(data.get(position).getTotalnetweight());

    if (TextUtils.isEmpty(data.get(position).getTotalwaste())) {
      holder.tv10.setText("--");
    } else {
      holder.tv10.setText(data.get(position).getTotalwaste());
    }
    holder.lyt.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View view) {
        Intent intent = new Intent(context, DetailActivity.class);
        intent.putExtra(DetailActivity.INTEN_KEY_HID, data.get(position).getHid());
        context.startActivity(intent);
      }
    });
    holder.btn.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View view) {
        //if (callBack != null) {
        //  callBack.call(data.get(position).getHid(), position);
        //}
        //MainAlertDialog alertDialog = new MainAlertDialog(context).builder();
        //alertDialog.setTitle("温馨提示");
        //alertDialog.setList(listItem);
        //alertDialog.setCallback((MainActivity) context);
        //alertDialog.show();
      }
    });
    holder.btnDel.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View view) {
        if (callBack != null) {
          callBack.del(data.get(position).getHid(), position);
        }
      }
    });
    holder.btnEdit.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View view) {
        if (callBack != null) {
          callBack.edit(data.get(position).getHid(), position);
        }
      }
    });
  }

  public interface CallBack {

    void call(String hid, int position);

    void del(String hid, int position);

    void edit(String hid, int position);
  }

  static class ItemViewHolder extends RecyclerView.ViewHolder {

    LinearLayout lyt;
    TextView tv1;
    TextView tv2;
    TextView tv3;
    TextView tv4;
    TextView tv5;
    TextView tv6;
    TextView tv7;
    TextView tv8;
    TextView tv9;
    TextView tv10;
    Button btn;
    Button btnDel;
    Button btnEdit;

    public ItemViewHolder(View view) {
      super(view);
      lyt = (LinearLayout) view.findViewById(R.id.lyt);
      tv1 = (TextView) view.findViewById(R.id.tv1);
      tv2 = (TextView) view.findViewById(R.id.tv2);
      tv3 = (TextView) view.findViewById(R.id.tv3);
      tv4 = (TextView) view.findViewById(R.id.tv4);
      tv5 = (TextView) view.findViewById(R.id.tv5);
      tv6 = (TextView) view.findViewById(R.id.tv6);
      tv7 = (TextView) view.findViewById(R.id.tv7);
      tv8 = (TextView) view.findViewById(R.id.tv8);
      tv9 = (TextView) view.findViewById(R.id.tv9);
      tv10 = (TextView) view.findViewById(R.id.tv10);
      btn = (Button) view.findViewById(R.id.tv11);
      btnDel = (Button) view.findViewById(R.id.tv12);
      btnEdit = (Button) view.findViewById(R.id.tv13);
    }
  }
}
