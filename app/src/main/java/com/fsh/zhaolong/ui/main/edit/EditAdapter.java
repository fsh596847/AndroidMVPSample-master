package com.fsh.zhaolong.ui.main.edit;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import com.fsh.zhaolong.R;
import com.fsh.zhaolong.bean.AddItemBean;
import java.util.List;

import static java.lang.Double.parseDouble;

/**
 * Created by HIPAA on 2016/12/23.
 */

public class EditAdapter extends RecyclerView.Adapter<EditAdapter.MyViewHolder> {

  CallBack callBack;
  ZengHang zengHang;
  private Context context;
  private List<AddItemBean> mDatas;
  private int layout;

  public EditAdapter(Context context, List<AddItemBean> mDatas, int layout) {
    this.context = context;
    this.mDatas = mDatas;
    this.layout = layout;
  }

  @Override
  public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    MyViewHolder holder = new MyViewHolder(LayoutInflater.from(
        context).inflate(layout, parent,
        false));
    return holder;
  }

  @Override
  public void onBindViewHolder(final MyViewHolder holder, final int position) {
    holder.tv1.setText(mDatas.get(position).getBreed());

    holder.btn.setTag(position);
    if (position == mDatas.size() - 1) {
      holder.btn.setText("增行");
    } else {
      holder.btn.setText("删除");
    }
    holder.btn.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View view) {
        if (Integer.parseInt(holder.btn.getTag().toString()) == position) {
          if (position == mDatas.size() - 1) {
            if (zengHang != null) {
              zengHang.add(position + 1);
            }
          } else {
            zengHang.remove(position);
          }
        }
      }
    });

    holder.tv1.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View view) {
        if (callBack != null) {
          callBack.BreedCallbck(position);
        }
      }
    });

    if (holder.tv2.getTag() instanceof TextWatcher) {
      holder.tv2.removeTextChangedListener(
          (TextWatcher) (holder.tv2.getTag()));
    }
    holder.tv2.setText(mDatas.get(position).getNorms());
    //holder.tv2.setTag(position);
    TextWatcher textWatcher2 = new TextWatcher() {
      @Override public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
      }

      @Override public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
      }

      @Override public void afterTextChanged(Editable editable) {
        //if (Integer.parseInt(holder.tv2.getTag().toString()) == position) {
        String stv2 = editable.toString();
        if (stv2.length() > 0 && parseDouble(stv2) > 0 && !stv2.startsWith(".")) {
          formNun(stv2, holder.tv2);
          mDatas.get(position).setNorms(stv2);
        }
        //}
      }
    };
    holder.tv2.addTextChangedListener(textWatcher2);
    holder.tv2.setTag(textWatcher2);
    /**
     * 毛重
     */
    if (holder.tv3.getTag() instanceof TextWatcher) {
      holder.tv3.removeTextChangedListener(
          (TextWatcher) (holder.tv3.getTag()));
    }
    holder.tv3.setText(mDatas.get(position).getRough());
    TextWatcher textWatcher = new TextWatcher() {
      @Override public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
      }

      @Override public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
      }

      @Override public void afterTextChanged(Editable editable) {
        //if (Integer.parseInt(holder.tv3.getTag().toString()) == position) {
        String stv3 = editable.toString();
        if (stv3.length() > 0 && !stv3.startsWith(".") && parseDouble(stv3) > 0) {
          formNun(stv3, holder.tv3);
          Log.d(EditActivity.class.getSimpleName(), "afterTextChanged:" + position);
          if (callBack != null) {
            callBack.MaoZhong(position, stv3);
          }
        }
      }
      //}
    };
    holder.tv3.addTextChangedListener(textWatcher);
    holder.tv3.setTag(textWatcher);
  }

  public void setCallBack(CallBack callBack) {
    this.callBack = callBack;
  }

  public void setZengHang(ZengHang zengHang) {
    this.zengHang = zengHang;
  }

  @Override
  public int getItemCount() {
    return mDatas.size();
  }

  private void formNun(CharSequence s, EditText editText) {
    if (s.toString().contains(".")) {
      if (s.length() - 1 - s.toString().indexOf(".")
          > 2) {
        s = s.toString().subSequence(0, s.toString().indexOf(".") + 3);
        editText.setText(s);
        editText.setSelection(s.length());
      }
    }

    if (s.toString().trim().substring(0).equals(".")) {
      s = "0" + s;
      editText.setText(s);
      editText.setSelection(2);
    }
    if (s.toString().startsWith("0")
        && s.toString().trim().length() > 1) {
      if (!s.toString().substring(1, 2).equals(".")) {
        editText.setText(s.subSequence(0,
            1));
        editText.setSelection(1);
        return;
      }
    }
  }

  public interface CallBack {
    //品种
    void BreedCallbck(int position);

    //毛重
    void MaoZhong(int position, String s);
  }

  public interface ZengHang {

    void add(int pos);

    void remove(int pos);
  }

  class MyViewHolder extends RecyclerView.ViewHolder {
    TextView tv1;
    EditText tv2;
    EditText tv3;
    Button btn;

    public MyViewHolder(View view) {
      super(view);
      tv1 = (TextView) view.findViewById(R.id.tv1);
      tv2 = (EditText) view.findViewById(R.id.tv2);
      tv3 = (EditText) view.findViewById(R.id.tv3);
      btn = (Button) view.findViewById(R.id.btn4);
    }
  }
}