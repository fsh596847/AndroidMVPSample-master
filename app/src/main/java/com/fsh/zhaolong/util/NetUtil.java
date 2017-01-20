/**
 * @Description:
 * @author fsh
 * @date 2015-4-6 上午10:42:26
 * @version V1.0
 */
package com.fsh.zhaolong.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class NetUtil {

  public final static int WIFI = 1;
  public final static int MOBILE = 2;
  public final static int NONE = -1;

  /**
   * 获取当前的网络状态
   */
  public static int getConnectState(Context context) {
    ConnectivityManager manager = (ConnectivityManager) context.getApplicationContext()
        .getSystemService(Context.CONNECTIVITY_SERVICE);

    if (manager == null) return NONE;

    NetworkInfo netWorkInfo = manager.getActiveNetworkInfo();

    if (netWorkInfo == null || !netWorkInfo.isAvailable() || !netWorkInfo.isConnected()) {
      return NONE;
    } else if (netWorkInfo.getType() == ConnectivityManager.TYPE_WIFI) {
      return WIFI;
    } else {
      return MOBILE;
    }
  }

  /**
   * true if network has connected
   */
  public static boolean isConnected(Context context) {
    return NetUtil.getConnectState(context) != NONE;
  }
}