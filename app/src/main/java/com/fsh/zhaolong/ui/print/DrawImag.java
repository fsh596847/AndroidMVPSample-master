package com.fsh.zhaolong.ui.print;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import java.io.FileOutputStream;
import java.io.IOException;

public class DrawImag {
  Bitmap bmp;
  Canvas canvas;
  Paint paint;
  int width;
  int height;
  //A4纸的尺寸是210mm×297mm
  //1英寸＝25.4毫米

  public void create(int widthmm, int heightmm, int ppi) {
    width = (int) (widthmm / 25.4 * ppi);
    height = (int) (heightmm / 25.4 * ppi);
    //		ALPHA_8 代表8位Alpha位图
    //		ARGB_4444 代表16位ARGB位图
    //		ARGB_8888 代表32位ARGB位图
    //		RGB_565 代表8位RGB位图
    Bitmap.Config cfg = Bitmap.Config.RGB_565;
    bmp = Bitmap.createBitmap(width, height, cfg);
    canvas = new Canvas(bmp);
    canvas.drawColor(Color.WHITE);
    paint = new Paint();
    paint.setAntiAlias(true);// 设置画笔的锯齿效果。 true是去除，大家一看效果就明白了
    //		paint.setFakeBoldText(false);
  }

  public int getWidth() {
    return width;
  }

  public int getHeight() {
    return height;
  }

  public void drawHLine(int x, int y, int width, int height) {
    paint.setStrokeWidth(height);
    canvas.drawLine(x, y, x + width, y, paint);
  }

  public void drawVLine(int x, int y, int width, int height) {
    paint.setStrokeWidth(width);
    canvas.drawLine(x, y, x, y + height, paint);
  }

  /**
   * @param textAlign 文字对其方式，-1做，1-右，0-中间
   */
  public int drawText(int x, int y, int width, int lineHeight, String text, int fontsize,
      int textAlign) {
    if (text == null) {
      return 0;
    }
    paint.setTextSize(fontsize);
    paint.setStrokeWidth(0);
    float strWidth = paint.measureText(text);
    if ((strWidth + 1) > width) {
      int len = text.length() * width / (int) (strWidth + 1);
      String ptext = text.substring(0, len);
      String ntext = text.substring(len);
      strWidth = paint.measureText(ptext);
      while ((strWidth + 1) > width) {
        len--;
        ptext = text.substring(0, len);
        ntext = text.substring(len);
        strWidth = paint.measureText(ptext);
      }
      int ret = drawText(x, y, width, lineHeight, ptext, fontsize, textAlign);
      int next = drawText(x, y + lineHeight, width, lineHeight, ntext, fontsize, textAlign);
      return ret + next;
    } else {
      if (textAlign == 0) {
        x += (width - strWidth) / 2;
      } else if (textAlign == 1) {
        x += width - strWidth;
      }
      canvas.drawText(text, x, y, paint);
      return 1;
    }
  }

  public Bitmap getBitmap() {
    return bmp;
  }

  public void saveBmp2File(String fileName, Bitmap.CompressFormat format, int quality) {
    FileOutputStream out = null;
    try {
      out = new FileOutputStream(fileName);
      bmp.compress(format, quality, out);
    } catch (Exception e) {

    } finally {
      if (out != null) {
        try {
          out.close();
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
    }
  }
}
