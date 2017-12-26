package com.stereo.ManualCalib;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.estar.lcm3djni;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.stereo.ManualCalib.glutils.myGLUtils;
import com.stereo.ManualCalib.myGLSurfaceView.FFPlayerListener;
import com.stereo.ManualCalib.myGLSurfaceView.MyGLSurfaceView;

public class AngleActivity extends Activity implements FFPlayerListener{
private static final String TAG = "AngleActivity";
private MyGLSurfaceView mGLView;
	Button btnAngleInc, btnAngleDec;
	Button btnWidthInc,btnWidthDec;
	Button btnViewInc, btnViewDec;
	Button btnnext, btnsave ;
	Button unitIncrease,unitDecrease;
	TextView textParameter,textUnit;

	static{
		String phoneModel =  android.os.Build.MODEL;
		String TakeePhoneModel = "takee 1";
		if (TakeePhoneModel.equals(phoneModel)) {
			GlobalVariable.isTakeePhone = true;
		}

		System.loadLibrary("myGLUtils");

	}

@Override
protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);//
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		requestWindowFeature(Window.FEATURE_NO_TITLE);//
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);//
		setContentView(R.layout.activity_angle);

		mGLView = (MyGLSurfaceView)findViewById(R.id.mSurfaceView1);
		mGLView.setLister(this);
		lcm3djni.EstarLcm3DOn();
		init();
}

public void init(){

	btnAngleInc = (Button)findViewById(R.id.btnAngleInc);
	btnAngleInc.setOnClickListener(mClickListener);

	btnAngleDec = (Button)findViewById(R.id.btnAngleDec);
	btnAngleDec.setOnClickListener(mClickListener);

	btnWidthInc = (Button)findViewById(R.id.btnWidthInc);
	btnWidthInc.setOnClickListener(mClickListener);

	btnWidthDec = (Button)findViewById(R.id.btnWidthDec);
	btnWidthDec.setOnClickListener(mClickListener);

	btnViewInc = (Button)findViewById(R.id.btnViewInc);
	btnViewInc.setOnClickListener(mClickListener);

	btnViewDec = (Button)findViewById(R.id.btnViewDec);
	btnViewDec.setOnClickListener(mClickListener);

	btnsave = (Button)findViewById(R.id.btnsave);
	btnsave.setOnClickListener(mClickListener);

	btnnext = (Button)findViewById(R.id.btnnext);
	btnnext.setOnClickListener(mClickListener);

	unitIncrease = (Button)findViewById(R.id.unit_inc);
	unitIncrease.setOnClickListener(mClickListener);

	unitDecrease = (Button)findViewById(R.id.unit_dec);
	unitDecrease.setOnClickListener(mClickListener);

	textParameter = (TextView)findViewById(R.id.text_parameter);
	textUnit = (TextView)findViewById(R.id.text_unit);

	apHander.sendEmptyMessage(0);
}

		Handler  apHander = new Handler(){
@Override
public void handleMessage(Message msg) {
		// TODO Auto-generated method stub

	textParameter.setText("baseview:" +GlobalVariable.baseview +  "\nangle:" + GlobalVariable.angle+"\npw:"+GlobalVariable.dw);
	textUnit.setText("单位"+GlobalVariable.unit);

	myGLUtils.setBaseView(GlobalVariable.baseview);
	super.handleMessage(msg);//父级处理消息 防止错过一些消息
}
		};

	View.OnClickListener mClickListener = new View.OnClickListener() {
@Override
public void onClick(View arg0) {
	// TODO Auto-generated method stub
	int id = arg0.getId();
	switch (id) {
		case R.id.btnsave:
			myGLUtils.save();
			break;
		case R.id.btnAngleInc:
			GlobalVariable.angle += GlobalVariable.unit;
			apHander.sendEmptyMessage(0);
			break;
		case R.id.btnAngleDec:
			GlobalVariable.angle -= GlobalVariable.unit;
			apHander.sendEmptyMessage(0);
			break;
		case R.id.btnWidthInc:
			GlobalVariable.dw += GlobalVariable.unit;
			apHander.sendEmptyMessage(0);
			break;
		case R.id.btnWidthDec:
			GlobalVariable.dw -= GlobalVariable.unit;
			apHander.sendEmptyMessage(0);
			break;
		case R.id.btnViewInc:
			GlobalVariable.baseview += 1;
			if(GlobalVariable.baseview >32) GlobalVariable.baseview = 32;
			apHander.sendEmptyMessage(0);
			break;
		case R.id.btnViewDec:
			GlobalVariable.baseview -= 1;
			if(GlobalVariable.baseview  < 1) GlobalVariable.baseview = 1;
			apHander.sendEmptyMessage(0);
			break;
		case R.id.btnnext:
			LogUtils.d(TAG + " next!");
			mGLView.switchPic();
			break;
		case R.id.unit_inc:
			GlobalVariable.unit *= 10;
			apHander.sendEmptyMessage(0);
			break;
		case R.id.unit_dec:
			GlobalVariable.unit /= 10;
			apHander.sendEmptyMessage(0);
			break;
	}
	mGLView.needUpdate();//更新显示
}
		};

@Override
public void onSurfaceCreated() {
		// TODO Auto-generated method stub
		}
@Override
public void onSurfaceChanged() {
		// TODO Auto-generated method stub
		LogUtils.d(TAG+" main :onSurfaceChanged");
		apHander.sendEmptyMessage(0);
		}


@Override
protected void onDestroy() {
		//		LogUtils.d(TAG+" onDestroy");
		lcm3djni.EstarLcm3DOff();
		myGLUtils.stop();
		// TODO Auto-generated method stub
		//		System.exit(0);
		super.onPause();
}

}
