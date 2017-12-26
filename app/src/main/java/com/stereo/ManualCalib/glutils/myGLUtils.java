package com.stereo.ManualCalib.glutils;

public class myGLUtils {
	static{
		System.loadLibrary("myGLUtils");
	}
	public static native void texImage2D(int corlor,int mDelt,float angle,float delts,float deltp);
	public static native void mPiex(int x,int y);
	
	
	public static native void autoSwitch(); 
	public static native void init(double angle,double pw,int w,int h);
	
	public static native float getAngle();
	public static native float getGWidth();
	public static native int getBaseView();
	public static native void setBaseView(int baseView);
	public static native void save();
	
	public static native void stop();
	public static native void readFrame();
	
	public static native int  getx();
	public static native int  gety();
	public static native int  getdis();
	public static native int  getd();
	public static native void  takep();	
	
	
}
