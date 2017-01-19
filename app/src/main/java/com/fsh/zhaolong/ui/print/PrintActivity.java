package com.fsh.zhaolong.ui.print;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import com.fsh.zhaolong.R;
import com.fsh.zhaolong.bean.DetailResponse;
import com.fsh.zhaolong.bean.FailSuccesssful;
import com.fsh.zhaolong.bean.MainResponse;
import com.fsh.zhaolong.ui.main.MainActivity;
import com.fsh.zhaolong.util.Const;
import com.fsh.zhaolong.util.PreferenceUtils;
import com.google.gson.Gson;
import com.jolimark.printerlib.DeviceInfo;
import com.jolimark.printerlib.RemotePrinter;
import com.jolimark.printerlib.VAR.PrinterType;
import com.jolimark.printerlib.VAR.TransType;
import com.jolimark.printerlib.util.WifiPrinterIPUtil.RefleshHandler;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.json.JSONObject;

public class PrintActivity extends AppCompatActivity implements View.OnClickListener {

  static final String API_SERVER_URL = "http://121.40.126.229:8888/yz/";
  ImageView preview_imageView;
  EditText ip_editText, port_editText;
  Button print_button, search_button;
  DrawImag drawImag;
  DisplayMetrics dm;
  private ProgressDialog m_pDialog; // 进度条
  MainResponse.DataBean dataBean;
  DetailResponse detailResponse;
  Toolbar toolbar;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_print);
    toolbar = (Toolbar) findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);
    initView();
    toolbar.setOnClickListener(this);
    dataBean =
        (MainResponse.DataBean) getIntent().getSerializableExtra(MainActivity.INTENT_KEY_EDIT);
    dm = new DisplayMetrics();
    getWindowManager().getDefaultDisplay().getMetrics(dm);
    drawImag = new DrawImag();
    queryDetail();
  }

  private void initView() {
    preview_imageView = (ImageView) findViewById(R.id.preview_imageView);
    ip_editText = (EditText) findViewById(R.id.ip_editText);
    port_editText = (EditText) findViewById(R.id.port_editText);
    print_button = (Button) findViewById(R.id.print_button);
    print_button.setOnClickListener(this);
    search_button = (Button) findViewById(R.id.search_button);
    search_button.setOnClickListener(this);

    String ip = PreferenceUtils.getPrefString(PrintActivity.this, "printerIp", "");
    ip_editText.setText(ip);
    String p = PreferenceUtils.getPrefString(PrintActivity.this, "printerPort", "");
    if (!p.equals("")) {
      port_editText.setText(p);
    }
  }

  private void queryDetail() {
    new Thread(new Runnable() {
      @Override
      public void run() {
        //创建okHttpClient对象
        OkHttpClient mOkHttpClient = new OkHttpClient();
        //创建一个Request
        final Request request = new Request.Builder()
            .url(API_SERVER_URL
                + "/phone_queryMakCodeDatail.action?userid="
                + dataBean.getUserid()
                + "&hid="
                + dataBean.getHid())
            .build();
        //new call
        Call call = mOkHttpClient.newCall(request);
        //请求加入调度
        call.enqueue(new Callback() {
          @Override
          public void onFailure(Call call, IOException e) {
            mHandler.obtainMessage(msgQueryDetailResponseFail, e.getMessage()).sendToTarget();
          }

          @Override
          public void onResponse(Call call, Response response) throws IOException {
            try {
              String htmlStr = response.body().string();
              if (!TextUtils.isEmpty(htmlStr)) {
                JSONObject jsonObject = new JSONObject(htmlStr);
                int status = jsonObject.getInt("status");
                if (status == Const.SUNNESS_STATUE) {
                  Gson gson = new Gson();
                  DetailResponse dr = gson.fromJson(htmlStr, DetailResponse.class);
                  mHandler.obtainMessage(msgQueryDetailResponseOk, dr).sendToTarget();
                } else {
                  FailSuccesssful failSuccesssful =
                      new Gson().fromJson(htmlStr, FailSuccesssful.class);
                  mHandler.obtainMessage(msgQueryDetailResponseFail, failSuccesssful.getData())
                      .sendToTarget();
                }
              } else {
                mHandler.obtainMessage(msgQueryDetailResponseFail, "返回数据异常")
                    .sendToTarget();
              }
            } catch (Exception e) {
              e.printStackTrace();
            }
          }
        });
      }
    }).start();
  }

  @Override
  public void onClick(View v) {
    switch (v.getId()) {
      case R.id.print_button:
        doPrint();
        //doPrintTestText();
        break;
      case R.id.search_button:
        doSearch();
        break;
      case R.id.toolbar:
        finish();
        break;
    }
  }

  final static int msgOpenPrinterFail = 1001;
  final static int msgOpenPrinterOk = 1002;
  final static int msgOpenPrinterConnected = 1003;
  final static int msgQueryDetailResponseOk = 1004;
  final static int msgQueryDetailResponseFail = 1005;
  private Handler mHandler = new Handler() {
    @Override
    public void handleMessage(Message msg) {
      switch (msg.what) {
        case ErrorOrMsg.DEVICE_FOUND:
          m_pDialog.hide();
          DeviceInfo deviceInfo = wifiList.get(wifiList.size() - 1);
          ip_editText.setText(deviceInfo.ip);
          //                    wifiDevicesArrayAdapters.add("Name: " + deviceInfo.type + "\nMac: " + deviceInfo.mac);
          //                    wifiDevicesArrayAdapters.notifyDataSetChanged();
          break;
        case msgQueryDetailResponseOk:
          detailResponse = (DetailResponse) msg.obj;
          Bitmap bmp = genPrint(false);
          preview_imageView.setBackgroundColor(Color.GRAY);
          preview_imageView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
          preview_imageView.setImageBitmap(bmp);
          break;
        case msgOpenPrinterFail:
        case msgOpenPrinterOk:
        case msgOpenPrinterConnected:
        case msgQueryDetailResponseFail:
          Toast.makeText(PrintActivity.this, (String) msg.obj, Toast.LENGTH_LONG).show();
          break;
      }
    }
  };

  // 退纸
  public byte[] getBackByteData() {
    byte[] bData3 = {27, 106, 127};// 打印并退纸100个单位，其它指令请参考《映美打印机编程手册》。
    return bData3;
  }

  int h = 297 * 80 * 360 / 210;

  public byte[] getBackPageData() {
    int len = h / 127 + 1;
    byte[] buf = new byte[len * 3];
    for (int i = 0; i < len; i++) {
      buf[i * 3] = 27;
      buf[i * 3 + 1] = 106;
      buf[i * 3 + 2] = 127;
    }
    return buf;
  }

  // 进纸
  public byte[] getFeedByteData() {
    byte[] bData2 = {27, 74, 100};// 打印并进纸100个单位，其它指令请参考《映美打印机编程手册》。
    return bData2;
  }

  private List<DeviceInfo> wifiList = null;// wifi列表

  private void doSearch() {

    // 显示连接进度对话框
    if (m_pDialog == null) {
      m_pDialog = new ProgressDialog(PrintActivity.this);
      m_pDialog.setMessage("正在搜索");
      m_pDialog.setIndeterminate(true);
      m_pDialog.setCanceledOnTouchOutside(true);
    }
    m_pDialog.show();
    RemotePrinter.getWifiPrinterIP(24, new RefleshHandler() {
      @Override
      public void deviceFound(DeviceInfo deviceInfo) {
        if (wifiList == null) {
          wifiList = new ArrayList<DeviceInfo>();
        }
        if (!(wifiList.contains(deviceInfo))) {
          wifiList.add(deviceInfo);
          mHandler.sendEmptyMessage(ErrorOrMsg.DEVICE_FOUND);
        }
      }
    });
  }

  private Bitmap genPrint(boolean toPrint) {
    int dpi = 360;
    //if (!toPrint){
    dpi = dm.densityDpi;
    //}
    dpi = 160;
    int titleFontSize = 30;
    int contentFontSize = 24;
    float mmpi = 25.4f;//一英寸25.4
    float lineHeightmm = 10;
    float drawLineHeightmm = 0.5f;//线的宽度和高度
    float linepaddingmm = 4;//行内padding
    int leftMargin = (int) (dpi * 22 / mmpi);
    int topMargin = (int) (dpi * 16 / mmpi);
    int rightMargin = (int) (dpi * 16 / mmpi);
    int bottomMargin = (int) (dpi * 16 / mmpi);
    int pageWidth = (int) (dpi * 297 / mmpi) - leftMargin - rightMargin;
    int lineHeight = (int) (dpi * lineHeightmm / mmpi);
    int drawLineHeight = (int) (dpi * drawLineHeightmm / mmpi);
    int linepadding = (int) (dpi * linepaddingmm / mmpi);
    drawImag.create(297, 210, dpi);//初始化
    int cy = lineHeight;//topMargin+lineHeight;//当前y位置
    int columnCount = 9;
    int columnWidth = pageWidth / columnCount;
    drawImag.drawText(leftMargin + 0, cy, pageWidth, lineHeight, "江苏省太湖渔政监督支队划码单", titleFontSize,
        0);
    cy += lineHeight;
    drawImag.drawText(leftMargin + 0, cy, pageWidth, lineHeight,
        "项  　目：" + dataBean.getProjectname(), contentFontSize, -1);
    drawImag.drawText(leftMargin + 0 + pageWidth * 2 / 3, cy, pageWidth / 3, lineHeight,
        "单据日期：" + dataBean.getCreatetime(), contentFontSize, -1);
    cy += lineHeight;
    drawImag.drawText(leftMargin + 0, cy, pageWidth, lineHeight, "交货单位：" + dataBean.getUnitname(),
        contentFontSize, -1);
    drawImag.drawText(leftMargin + 0 + pageWidth / 3, cy, pageWidth / 3, lineHeight,
        "交货地点：" + dataBean.getDeliveryaddress(), contentFontSize, -1);
    drawImag.drawText(leftMargin + 0 + pageWidth * 2 / 3, cy, pageWidth / 3, lineHeight,
        "单 据 号：" + dataBean.getBillcode(), contentFontSize, -1);
    cy += lineHeight;
    StringBuffer buf = new StringBuffer();
    buf.append(",");
    StringBuffer bufGuige = new StringBuffer();
    bufGuige.append(",");
    for (int i = 0; i < detailResponse.getData().size(); i++) {
      DetailResponse.DataBean db = detailResponse.getData().get(i);
      if (buf.indexOf("," + db.getVarietyname() + ",") >= 0) {

      } else {
        if (db.getVarietyname() == null || db.getVarietyname().equals("")) {
          continue;
        }
        buf.append(db.getVarietyname() + ",");
      }
      ///
      if (bufGuige.indexOf("," + db.getSpec() + ",") >= 0) {

      } else {
        bufGuige.append(db.getSpec() + ",");
      }
    }
    String pinzhongName = "";
    String guigeName = "";
    if (buf.length() > 1) {
      pinzhongName = buf.substring(1, buf.length() - 1);
    }
    if (bufGuige.length() > 1) {
      guigeName = bufGuige.substring(1, bufGuige.length() - 1);
    }
    drawImag.drawText(leftMargin + 0, cy, pageWidth, lineHeight, "品　　种：" + pinzhongName,
        contentFontSize, -1);
    drawImag.drawText(leftMargin + 0 + pageWidth / 3, cy, pageWidth / 3, lineHeight,
        "规格（条/斤）：" + guigeName, contentFontSize, -1);
    drawImag.drawText(leftMargin + 0 + pageWidth * 2 / 3, cy, pageWidth / 3, lineHeight,
        "每条除皮(斤)：" + dataBean.getPeel(), contentFontSize, -1);
    cy += lineHeight;
    int lineCount = drawImag.drawText(leftMargin + 0, cy, pageWidth, lineHeight,
        "备注：" + (dataBean.getRemark() == null ? "" : dataBean.getRemark()), contentFontSize, -1);
    cy += lineHeight / 2;
    if (lineCount > 1) {
      cy += (lineCount - 1) * lineHeight;
    }
    int tableY = cy;
    for (int i = 0; i < 13; i++) {
      drawImag.drawHLine(leftMargin + 0, tableY, pageWidth, drawLineHeight);
      tableY += lineHeight;
    }
    tableY -= lineHeight;
    int tableX = leftMargin;
    for (int i = 0; i < (columnCount + 1); i++) {
      if (columnCount == i) {
        tableX = leftMargin + pageWidth;
      }
      if (i == 8) {
        drawImag.drawVLine(tableX, cy + lineHeight, drawLineHeight, tableY - cy - lineHeight);
      } else {
        drawImag.drawVLine(tableX, cy, drawLineHeight, tableY - cy);
      }
      tableX += pageWidth / columnCount;
    }
    tableX = leftMargin;
    tableY = cy + lineHeight - linepadding;
    drawImag.drawText(tableX + columnWidth, tableY, columnWidth, lineHeight, "品种", contentFontSize,
        0);
    drawImag.drawText(tableX + columnWidth * 2, tableY, columnWidth, lineHeight, "规格(条/斤)",
        contentFontSize, 0);
    drawImag.drawText(tableX + columnWidth * 3, tableY, columnWidth, lineHeight, "数量(毛重)",
        contentFontSize, 0);
    drawImag.drawText(tableX + columnWidth * 4, tableY, columnWidth, lineHeight, "单价",
        contentFontSize, 0);
    drawImag.drawText(tableX + columnWidth * 5, tableY, columnWidth, lineHeight, "金额",
        contentFontSize, 0);
    drawImag.drawText(tableX + columnWidth * 6, tableY, columnWidth, lineHeight, "尾数",
        contentFontSize, 0);
    drawImag.drawText(tableX + columnWidth * 7, tableY, columnWidth * 2, lineHeight, "总计",
        contentFontSize, 0);

    for (int i = 1; i < 11; i++) {
      drawImag.drawText(tableX, tableY + lineHeight * i, columnWidth, lineHeight, "" + i,
          contentFontSize, 0);
    }
    double endNum = 0;
    for (int i = 0; i < detailResponse.getData().size(); i++) {
      DetailResponse.DataBean db = detailResponse.getData().get(i);
      drawImag.drawText(tableX + columnWidth + linepadding, tableY + lineHeight * (i + 1),
          columnWidth, lineHeight, db.getVarietyname(), contentFontSize, 0);
      drawImag.drawText(tableX + columnWidth * 2 + linepadding, tableY + lineHeight * (i + 1),
          columnWidth, lineHeight, "" + db.getSpec(), contentFontSize, 0);
      drawImag.drawText(tableX + columnWidth * 3 + linepadding, tableY + lineHeight * (i + 1),
          columnWidth, lineHeight, db.getNum(), contentFontSize, 0);
      drawImag.drawText(tableX + columnWidth * 4 + linepadding, tableY + lineHeight * (i + 1),
          columnWidth, lineHeight, "" + db.getPrice(), contentFontSize, 0);
      drawImag.drawText(tableX + columnWidth * 5 + linepadding, tableY + lineHeight * (i + 1),
          columnWidth, lineHeight, db.getMny(), contentFontSize, 0);
      drawImag.drawText(tableX + columnWidth * 6 + linepadding, tableY + lineHeight * (i + 1),
          columnWidth, lineHeight, "" + db.getEndnum(), contentFontSize, 0);
      endNum += db.getEndnum();
    }

    drawImag.drawText(tableX, tableY + lineHeight * 11, columnWidth, lineHeight, "合计",
        contentFontSize, 0);
    drawImag.drawText(tableX + columnWidth * 5 + linepadding, tableY + lineHeight * 11, columnWidth,
        lineHeight, dataBean.getTotalmny(), contentFontSize, 0);
    drawImag.drawText(tableX + columnWidth * 6 + linepadding, tableY + lineHeight * 11, columnWidth,
        lineHeight, "" + endNum, contentFontSize, 0);

    drawImag.drawText(tableX + columnWidth * 7, tableY + lineHeight, columnWidth, lineHeight, "件数",
        contentFontSize, 0);
    drawImag.drawText(tableX + columnWidth * 8, tableY + lineHeight, columnWidth, lineHeight,
        "" + dataBean.getTotalnum(), contentFontSize, 0);// detailResponse.getData().size()
    drawImag.drawText(tableX + columnWidth * 7, tableY + lineHeight * 2, columnWidth, lineHeight,
        "毛重", contentFontSize, 0);
    drawImag.drawText(tableX + columnWidth * 8, tableY + lineHeight * 2, columnWidth, lineHeight,
        "" + dataBean.getTotalgrossweight(), contentFontSize, 0);
    drawImag.drawText(tableX + columnWidth * 7, tableY + lineHeight * 3, columnWidth, lineHeight,
        "皮重", contentFontSize, 0);
    drawImag.drawText(tableX + columnWidth * 8, tableY + lineHeight * 3, columnWidth, lineHeight,
        "" + dataBean.getTotaltareweight(), contentFontSize, 0);
    drawImag.drawText(tableX + columnWidth * 7, tableY + lineHeight * 4, columnWidth, lineHeight,
        "净重", contentFontSize, 0);
    drawImag.drawText(tableX + columnWidth * 8, tableY + lineHeight * 4, columnWidth, lineHeight,
        "" + dataBean.getTotalnetweight(), contentFontSize, 0);
    drawImag.drawText(tableX + columnWidth * 7, tableY + lineHeight * 5, columnWidth, lineHeight,
        "废品", contentFontSize, 0);
    drawImag.drawText(tableX + columnWidth * 8, tableY + lineHeight * 5, columnWidth, lineHeight,
        "" + dataBean.getTotalwaste(), contentFontSize, 0);
    drawImag.drawText(tableX + columnWidth * 7, tableY + lineHeight * 6, columnWidth, lineHeight,
        "实重", contentFontSize, 0);
    drawImag.drawText(tableX + columnWidth * 8, tableY + lineHeight * 6, columnWidth, lineHeight,
        "" + dataBean.getTotalrealweight(), contentFontSize, 0);
    drawImag.drawText(tableX + columnWidth * 7, tableY + lineHeight * 9, columnWidth, lineHeight,
        "调整数", contentFontSize, 0);

    //        if (toPrint){
    //            String fileName = Environment.getExternalStorageDirectory().getAbsolutePath()+"/360/print.png";
    //            drawImag.saveBmp2File(fileName, Bitmap.CompressFormat.PNG,100);
    //        }else{
    //            String fileName = Environment.getExternalStorageDirectory().getAbsolutePath()+"/360/preview.png";
    //            drawImag.saveBmp2File(fileName, Bitmap.CompressFormat.PNG,100);
    //        }
    return drawImag.getBitmap();
  }

  Bitmap rotationBmp(Bitmap bm, final int orientationDegree) {

    Matrix m = new Matrix();
    m.setRotate(orientationDegree, (float) bm.getWidth() / 2, (float) bm.getHeight() / 2);
    float targetX, targetY;
    if (orientationDegree == 90) {
      targetX = bm.getHeight();
      targetY = 0;
    } else {
      targetX = bm.getHeight();
      targetY = bm.getWidth();
    }

    final float[] values = new float[9];
    m.getValues(values);

    float x1 = values[Matrix.MTRANS_X];
    float y1 = values[Matrix.MTRANS_Y];

    m.postTranslate(targetX - x1, targetY - y1);

    Bitmap bm1 = Bitmap.createBitmap(bm.getHeight(), bm.getWidth(), Bitmap.Config.RGB_565);
    Paint paint = new Paint();
    Canvas canvas = new Canvas(bm1);
    canvas.drawBitmap(bm, m, paint);
    return bm1;
  }

  private PrinterType printerType = null;// 打印机类型
  boolean isPrinting = false;

  private void doPrintTestText() {
    final String ip = ip_editText.getText().toString().trim();
    final String port = port_editText.getText().toString().trim();
    if (ip.equals("") || port.equals("")) {
      Toast.makeText(PrintActivity.this, "打印机IP地址或者端口都不能为空", Toast.LENGTH_SHORT).show();
      return;
    }
    PreferenceUtils.setPrefString(PrintActivity.this, "printerIp", ip);
    PreferenceUtils.setPrefString(PrintActivity.this, "printerPort", port);
    if (!isPrinting) {
      isPrinting = true;
      new Thread() {
        public void run() {
          RemotePrinter myJmPrinter = null;
          try {
            //printerType = PrinterType.PT_DOT24;
            //byte[] buf = getPicByteData();
            myJmPrinter = new RemotePrinter(TransType.TRANS_WIFI, ip + ":" + port);
            if (myJmPrinter.open()) { // 打开通讯通道
              mHandler.obtainMessage(msgOpenPrinterConnected, "已连接到打印机").sendToTarget();
              printerType = myJmPrinter.getPrinterType();
              byte[] buf = getPicByteData();
              myJmPrinter.sendData(getTextByteData(printerType));
              myJmPrinter.sendData(getBackByteData()); //退纸
              mHandler.obtainMessage(msgOpenPrinterOk, "已发送数据到打印机").sendToTarget();
            } else {
              // 打开通道失败
              int ErrCode = myJmPrinter.getLastErrorCode();
              mHandler.obtainMessage(msgOpenPrinterFail, "无法连接打印机,错误码是：" + ErrCode).sendToTarget();
            }
          } catch (Exception e) {
            mHandler.obtainMessage(msgOpenPrinterFail, "打印出错：" + e.getMessage()).sendToTarget();
          } finally {
            if (myJmPrinter != null) {
              myJmPrinter.close();          //关闭通讯通道
              myJmPrinter = null;              //释放 RemotePrinter实例
            }
            isPrinting = false;
          }
        }
      }.start();
    } else {
      Toast.makeText(PrintActivity.this, "请稍候，正在打印中...", Toast.LENGTH_LONG).show();
    }
  }

  private void doPrint() {

    final String ip = ip_editText.getText().toString().trim();
    final String port = port_editText.getText().toString().trim();
    if (ip.equals("") || port.equals("")) {
      Toast.makeText(PrintActivity.this, "打印机IP地址或者端口都不能为空", Toast.LENGTH_SHORT).show();
      return;
    }
    PreferenceUtils.setPrefString(PrintActivity.this, "printerIp", ip);
    PreferenceUtils.setPrefString(PrintActivity.this, "printerPort", port);
    if (!isPrinting) {
      isPrinting = true;
      new Thread() {
        public void run() {
          RemotePrinter myJmPrinter = null;
          try {
            //printerType = PrinterType.PT_DOT24;
            //byte[] buf = getPicByteData();
            myJmPrinter = new RemotePrinter(TransType.TRANS_WIFI, ip + ":" + port);
            if (myJmPrinter.open()) { // 打开通讯通道
              mHandler.obtainMessage(msgOpenPrinterConnected, "已连接到打印机").sendToTarget();
              printerType = myJmPrinter.getPrinterType();
              byte[] buf = getPicByteData();
              myJmPrinter.sendData(buf); // 将点阵数据发送到打印机
              myJmPrinter.sendData(getBackPageData()); //退纸
              byte[] initPrinter = null;
              // 初始化
              initPrinter = ByteArrayUtils.twoToOne(initPrinter, Command.a17);
              myJmPrinter.sendData(initPrinter);
              //myJmPrinter.sendData(getTextByteData(printerType));
              mHandler.obtainMessage(msgOpenPrinterOk, "已发送数据到打印机").sendToTarget();
            } else {
              // 打开通道失败
              int ErrCode = myJmPrinter.getLastErrorCode();
              mHandler.obtainMessage(msgOpenPrinterFail, "无法连接打印机,错误码是：" + ErrCode).sendToTarget();
            }
          } catch (Exception e) {
            mHandler.obtainMessage(msgOpenPrinterFail, "打印出错：" + e.getMessage()).sendToTarget();
          } finally {
            if (myJmPrinter != null) {
              myJmPrinter.close();          //关闭通讯通道
              myJmPrinter = null;              //释放 RemotePrinter实例
            }
            isPrinting = false;
          }
        }
      }.start();
    } else {
      Toast.makeText(PrintActivity.this, "请稍候，正在打印中...", Toast.LENGTH_LONG).show();
    }
  }

  // 获取图片数据
  public byte[] getPicByteData() {
    byte[] comStr = null;
    byte[] tmpBUf = null;
    Bitmap bmp = genPrint(true);
    Bitmap rotatedBmp = rotationBmp(bmp, 90);
    //tmpBUf = convertImage( bmp);
    tmpBUf = RemotePrinter.ConvertImage(printerType, rotatedBmp);

    // 打印机初始化
    comStr = ByteArrayUtils.twoToOne(comStr, Command.a17);
    comStr = ByteArrayUtils.twoToOne(comStr, tmpBUf);
    return comStr;
  }

  // 获取文本数据
  public static byte[] getTextByteData(PrinterType printerType) {
    // 数据容器strToByte[]
    byte strToByte[] = null;
    // 打印机初始化
    strToByte = ByteArrayUtils.twoToOne(strToByte, Command.a17);

    String str = "文本打印示例：\r\n\r\n";
    String chineseContent = "中文：欢迎使用映美无线打印机！\r\n";
    String englishContent = "ENGLISH:Welcome to use the jolimark wireless printer!\r\n\r\n";
    System.out.println("-------------类型：" + printerType);

    if (printerType == PrinterType.PT_DOT24) { // PT_DOT24支持的打印机指令
      // 打印中文，需要发送打印中文指令，0x1C 0x26是打印中文的指令
      // twoToOne()函数是byte类型数组连接的工具类
      strToByte = ByteArrayUtils.twoToOne(strToByte, Command.a14);
      strToByte = ByteArrayUtils.twoToOne(strToByte, ByteArrayUtils.stringToByte(str));
      // 打印默认字体
      strToByte = ByteArrayUtils.twoToOne(strToByte, ByteArrayUtils.stringToByte("默认字体：\r\n"));
      strToByte = ByteArrayUtils.twoToOne(strToByte, ByteArrayUtils.stringToByte(chineseContent));
      strToByte = ByteArrayUtils.twoToOne(strToByte, ByteArrayUtils.stringToByte(englishContent));
      // 发送取消打印中文指令 0x1c 0x2e
      strToByte = ByteArrayUtils.twoToOne(strToByte, Command.a15);
    } else if (printerType == PrinterType.PT_THERMAL || printerType == PrinterType.PT_DOT9) {
      // 默认模式
      strToByte = ByteArrayUtils.twoToOne(strToByte, Command.a14);// 打印中文，需要发送打印中文指令，0x1C
      // 0x26是打印中文的指令
      strToByte = ByteArrayUtils.twoToOne(strToByte, ByteArrayUtils.stringToByte("默认字体：\r\n"));
      strToByte = ByteArrayUtils.twoToOne(strToByte, ByteArrayUtils.stringToByte(chineseContent));
      strToByte = ByteArrayUtils.twoToOne(strToByte, ByteArrayUtils.stringToByte(englishContent));
      // strToByte = ArrayUtils.twoToOne(strToByte, a15);// 发送取消打印中文指令
      // 0x1c 0x2e
      // 倍宽
      // 取消倍宽倍高模式
      strToByte = ByteArrayUtils.twoToOne(strToByte, Command.b11);
      strToByte = ByteArrayUtils.twoToOne(strToByte, Command.b12);
    }
    // 打印机初始化
    strToByte = ByteArrayUtils.twoToOne(strToByte, Command.a17);

    return strToByte;
  }
}