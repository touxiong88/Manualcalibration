package com.stereo.ManualCalib;

public class GlobalVariable {
//public  static float angle = 15.008f;
//public  static float delts   = -0.517f;
//public static float deltp   = -0.00039857f;
	public static  boolean isTakeePhone = false;
 	public static  float angle = 17.78f;
//public static  float dw = 2.66667f;
//public static  float dw = 2.878f;  //P1 8.4 PAD
	public static  float dw = 3.275f;
	public static  float unit = 0.001f;
	public static  int  baseview = 0;
	public  static float delts   = -1.417f;
	public static float deltp   = 0;
	public static float arg1 = 0.2f;
	public static float arg2 = 0.37f;
	public static float arg3 = 0.57f;

	public static final int FLOAT_SIZE_BYTES = 4;
	public static final int VERTICES_DATA_STRIDE_BYTES = 5 * FLOAT_SIZE_BYTES;
	public static final int VERTICES_DATA_POS_OFFSET = 0;
	public static final int VERTICES_DATA_UV_OFFSET = 3;
}
