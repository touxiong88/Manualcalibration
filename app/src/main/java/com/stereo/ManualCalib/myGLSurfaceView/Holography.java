package com.stereo.ManualCalib.myGLSurfaceView;

import com.stereo.ManualCalib.LogUtils;

public class Holography {

	private static final String TAG = "Holography";
	static{
		System.loadLibrary("holography");
	}
	
	
	public static void  startFaceDetector(){
		
		
	}
	
	public static void stopFaceDetector(){
		
	}

	public static void initHolography(){
		LogUtils.d(TAG+"initHolography");
		HolographyInit();
	}
	
	
	public static native void  deinitHolography();
	public static native void  HolographyInit( );
//	public static native void  HolographySetSize(int x,int y);
	public static native void  startAutoSwitch();
	public static native void  stopAutoSwitch();
//	public static native void  setTexture(int tid);
	public static native void  update(int a,int b);
	public static native void  sendDelt(int a);
	
	
}
