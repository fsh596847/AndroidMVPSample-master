package com.fsh.zhaolong.ui.print;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

/**
 * byte数组工具类
 * 
 * @author zhrjian
 *
 */
public class ByteArrayUtils {
	/**
	 * 两个数组连接
	 *
	 * @param father 在前
	 * @param son 在后
	 */
	public static byte[] twoToOne(byte[] father, byte[] son) {
		if (father != null && son != null) { // 两个数组都不为空
			byte[] all = new byte[father.length + son.length];
			for (int i = 0; i < all.length; i++) {
				if (i < father.length) {
					all[i] = father[i];
				} else {
					all[i] = son[i - father.length];
				}
			}
			father = null;// 释放掉
			return all;
		} else if (father == null && son != null) { // father为空son不为空
			return son;
		} else if (father != null && son == null) {// father不为空son为空
			return father;
		} else { // father和son全为空
			return null;
		}
	}

	/**
	 * 将byte类型变量加入数组
	 * 
	 * @param father
	 * @param data
	 * @return
	 */
	public static byte[] twoToOne(byte[] father, byte data) {
		if (father != null) { // 两个数组都不为空
			byte[] all = new byte[father.length + 1];
			for (int i = 0; i < all.length - 1; i++) {
				all[i] = father[i];
			}
			all[all.length - 1] = data;
			father = null;// 释放掉
			return all;
		} else { // father为空son不为空
			return new byte[] {data};
		}
	}

	/**
	 * byte数组分割工具
	 *
	 * @param data
	 *            需要分割的byte数组
	 * @param length
	 *            分割的长度
	 * @return
	 */
	public static List<byte[]> fen(byte[] data, int length) {
		List<byte[]> data2 = new ArrayList<byte[]>();
		int num = data.length / length;// 分包个数取整
		int num2 = data.length % length;// 剩余数据的长度，不足1024
		// 分包发送
		for (int i = 0; i < num; i++) {
			byte[] buffer = new byte[length];// 每个包大小1K
			for (int j = 0; j < 1024; j++) {
				buffer[j] = data[j + i * 1024];
			}
			data2.add(buffer);
		}
		if (num2 != 0) {// 还有数据
			byte[] buffer2 = new byte[num2];
			for (int k = 0; k < num2; k++) {
				buffer2[k] = data[k + num * 1024];
			}
			data2.add(buffer2);
		}
		return data2;
	}

	/**
	 * 字符串转换成GB2312编码的byte数组
	 *
	 * @param str
	 * @return
	 */
	public static byte[] stringToByte(String str) {
		byte[] s2byte = null;
		try {
			s2byte = str.getBytes("GB2312");
		} catch (UnsupportedEncodingException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		return s2byte;
	}
}
