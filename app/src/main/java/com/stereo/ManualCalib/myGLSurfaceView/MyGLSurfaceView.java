/*
 * Copyright (C) 2011 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.stereo.ManualCalib.myGLSurfaceView;

import android.content.Context;
import android.graphics.SurfaceTexture;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;

import com.stereo.ManualCalib.GlobalVariable;
import com.stereo.ManualCalib.LogUtils;
import com.stereo.ManualCalib.R;
import com.stereo.ManualCalib.glutils.myGLUtils;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class MyGLSurfaceView extends GLSurfaceView {
	private static final String TAG = "MyGLSurfaceView";
	MyRenderer mRenderer;
	public FFPlayerListener mLister;


	public Render3D mRender3D;

	public Render2D2 mRender2D2;
	public FrameBufferOBJ mFBO = null;

	float angle = GlobalVariable.angle;

	int[] textureList = new int[2];
	public int texid = 1;
	public int mShowCam = 2;

    public MyGLSurfaceView(Context context) {
        this(context, null);
        init();
    }

    public MyGLSurfaceView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        init();
    }
    private void init() {
        setEGLContextClientVersion(2);
        mRenderer = new MyRenderer();
        setRenderer(mRenderer);
        setRenderMode(RENDERMODE_WHEN_DIRTY);
    }

	public void setLister(FFPlayerListener ll) {
		mLister = ll;
	}


class MyRenderer implements GLSurfaceView.Renderer,SurfaceTexture.OnFrameAvailableListener{
    private float[] mSTMatrix = new float[16];

	private int mMidTexture;
    private boolean  updateSurfaceVid = false;
    private boolean  updateSurfaceCam = false;
	int[] picA = {R.drawable.p02_car,R.drawable.p02_car};



	int currPic = 0;

	boolean drawP = false;
	boolean drawN = false;

	public int mBufferWidth;
	public int mBufferHeight;
    public MyRenderer() {
    }

    public void drawPrev(){
    	currPic--;
    	if(currPic < 0) currPic = picA.length-1;
    	ShaderUtil.LoadTextureFromRes(texid,picA[currPic],MyGLSurfaceView.this.getResources());
    	drawP = false;
    }

    public void drawNext(){
    	currPic++;
    	if(currPic > picA.length-1) currPic = 0;

    	ShaderUtil.LoadTextureFromRes(texid,picA[currPic],MyGLSurfaceView.this.getResources());
    	drawN = false;
    }

    public void onDrawFrame(GL10 glUnused) {

		hander.sendEmptyMessage(0);

		mFBO.used();
    	mRender2D2.drawSelf(textureList[texid]);
		mFBO.unused();
		mRender3D.drawSelf(mMidTexture);

    }

    public void onSurfaceCreated(GL10 glUnused, EGLConfig config) {
    	LogUtils.d(TAG+" onSurfaceCreated");
        GLES20.glEnable(GLES20.GL_BLEND);
        GLES20.glBlendFunc(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);
        GLES20.glClearColor(0, 0, 0, 1.0f);


        synchronized(this) {
            updateSurfaceVid = false;
        }

        textureList[0] = ShaderUtil.initTexture(R.drawable.p04_black_white,MyGLSurfaceView.this.getResources());


        textureList[1] = ShaderUtil.initTexture(R.drawable.p02_car,MyGLSurfaceView.this.getResources());

        mLister.onSurfaceCreated();
    }

    public void onSurfaceChanged(GL10 glUnused, int w, int h) {
		LogUtils.d(TAG+" onSurfaceChanged:"+w+":"+h);
    	mBufferWidth = w;
    	mBufferHeight = h;
        GLES20.glViewport(0, 0, w, h);

        myGLUtils.init(angle, GlobalVariable.dw, mBufferWidth, mBufferHeight);

        GlobalVariable.angle = myGLUtils.getAngle();
        GlobalVariable.dw = myGLUtils.getGWidth();
        GlobalVariable.baseview = myGLUtils.getBaseView();


        mRender3D = new Render3D(MyGLSurfaceView.this,w,h);

        mRender2D2= new Render2D2(MyGLSurfaceView.this,w,h);
        mFBO = new FrameBufferOBJ(mBufferWidth, mBufferHeight);
        mMidTexture = mFBO.getTexture();
        mLister.onSurfaceChanged();

        LogUtils.d(TAG+" onSurfaceChanged out");

  //      autohander.sendEmptyMessage(0);
  //      autoSwitch();
    }


    synchronized public void onFrameAvailable(SurfaceTexture surface) {
/*    	if(surface == mRenderCam.getSurfaceTexture()){
    		updateSurfaceCam= true;
    	}else{
    		updateSurfaceVid = true;
    	}*/
    }

}

public void  needUpdate(){
	requestRender();
}




long  lasttime = 0;
int frames = 0;
Handler hander = new Handler(){
	@Override
	public void handleMessage(Message msg) {
		// TODO Auto-generated method stub
		frames++;
		long ct = System.currentTimeMillis();
		if(ct - 1000 > lasttime){
			LogUtils.d(TAG+" fps1:"+frames);
			frames = 0;
			lasttime = ct;
		}
		super.handleMessage(msg);
	}
};

Handler autohander = new Handler(){
	@Override
	public void handleMessage(Message msg) {
		// TODO Auto-generated method stub
		mRender3D.addDelts(1);

		sendEmptyMessageDelayed(0, 3000);
		super.handleMessage(msg);
	}
};


public void addDelt(int i) {
	// TODO Auto-generated method stub
	mRender3D.addDelts(i);
}

public void moveBlack(int x,int y){
	mRender3D.moveBlack(x, y);
}

public void switchPic(){

	texid++;
	texid = texid%2;
	LogUtils.d(TAG+" texid:"+texid+"textureList:"+textureList[texid]);
}

//public void moveBlack(int x,int y){
//	mRender3DVideo.moveBlack(x, y);
//}
}
