package com.fsh.zhaolong.ui.main;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
  private Display display;
  private int width;

  public MainAdapter(List<MainResponse.DataBean> data,
      Context context) {
    this.data = data;
    this.context = context;
    display = ((MainActivity) context).getWindowManager().getDefaultDisplay();
    width = display.getWidth() - 20;
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
    //(width * 0.15));
    //(width * 0.2));
    //(width * 0.05));
    //(width * 0.05));
    //(width * 0.1));
    //(width * 0.05));
    //(width * 0.1));
    //(width * 0.05));
    //3.5
    holder.tv1.setWidth((int) (width * 0.15));
    holder.tv2.setWidth((int) (width * 0.15));
    //1
    holder.tv5.setWidth((int) (width * 0.13));
    holder.tv6.setWidth((int) (width * 0.06));
    //3
    holder.tv7.setWidth((int) (width * 0.07));
    holder.tv8.setWidth((int) (width * 0.07));
    holder.tv9.setWidth((int) (width * 0.07));
    holder.tv10.setWidth((int) (width * 0.07));
    //1.8
    holder.btn.setWidth((int) (width * 0.06));
    holder.btnDel.setWidth((int) (width * 0.06));
    holder.btnEdit.setWidth((int) (width * 0.06));

    holder.tv1.setText(data.get(position).getBillcode());
    holder.tv2.setText(data.get(position).getUnitname());

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
        intent.putExtra(DetailActivity.INTEN_KEY_HID, data.get(position));
        context.startActivity(intent);
      }
    });
    holder.btn.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View view) {
        if (callBack != null) {
          callBack.call(data.get(position).getHid(), position);
        }
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

    TextView tv5;
    TextView tv6;
    TextView tv7;
    TextView tv8;
    TextView tv9;
    TextView tv10;
    TextView btn;
    TextView btnDel;
    TextView btnEdit;

    public ItemViewHolder(View view) {
      super(view);
      lyt = (LinearLayout) view.findViewById(R.id.lyt);
      tv1 = (TextView) view.findViewById(R.id.tv1);
      tv2 = (TextView) view.findViewById(R.id.tv2);
      tv5 = (TextView) view.findViewById(R.id.tv5);
      tv6 = (TextView) view.findViewById(R.id.tv6);
      tv7 = (TextView) view.findViewById(R.id.tv7);
      tv8 = (TextView) view.findViewById(R.id.tv8);
      tv9 = (TextView) view.findViewById(R.id.tv9);
      tv10 = (TextView) view.findViewById(R.id.tv10);
      btn = (TextView) view.findViewById(R.id.tv11);
      btnDel = (TextView) view.findViewById(R.id.tv12);
      btnEdit = (TextView) view.findViewById(R.id.tv13);
    }
  }
}
