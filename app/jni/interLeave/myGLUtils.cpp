#include "myGLUtilsJni.h"
#include "fflog.h"

#ifdef __cplusplus
extern "C" {
#endif


void JNICALL Java_com_stereo_ManualCalib_glutils_myGLUtils_texImage2D
  (JNIEnv *env, jclass cls , jint w, jint h, jfloat angle,jfloat delts,jfloat deltp){
	int64_t beg = getTimeMs();
	updateImage(w,h,angle,delts,deltp);
	int64_t end = getTimeMs();
//	LOGXWL("beg %ld, end %ld,update took: %ld",beg,end,end - beg);
}

void JNICALL Java_com_stereo_ManualCalib_glutils_myGLUtils_mPiex
  (JNIEnv *env, jclass cls , jint x,jint y){

	mPiex(x,y);

}


void JNICALL Java_com_stereo_ManualCalib_glutils_myGLUtils_autoSwitch
  (JNIEnv *env, jclass cls){

}


void JNICALL Java_com_stereo_ManualCalib_glutils_myGLUtils_readFrame
  (JNIEnv *env, jclass cls){
  readFrame();

}

void JNICALL Java_com_stereo_ManualCalib_glutils_myGLUtils_init
  (JNIEnv *env, jclass cls , jdouble angle,jdouble pw,jint w,jint h){
	init(angle,pw,w,h);

}

float JNICALL Java_com_stereo_ManualCalib_glutils_myGLUtils_getAngle
  (JNIEnv *env, jclass cls ){
	return gAngle;

}

float JNICALL Java_com_stereo_ManualCalib_glutils_myGLUtils_getGWidth
  (JNIEnv *env, jclass cls){
	return pxwid;

}

int JNICALL Java_com_stereo_ManualCalib_glutils_myGLUtils_getBaseView
  (JNIEnv *env, jclass cls ){
	return baseView;

}

void JNICALL Java_com_stereo_ManualCalib_glutils_myGLUtils_setBaseView
  (JNIEnv *env, jclass cls ,jint basev){

	baseView = basev;
        LOGFI("baseView %d",baseView);

}

void JNICALL Java_com_stereo_ManualCalib_glutils_myGLUtils_save
  (JNIEnv *env, jclass cls , jdouble angle,jint w,jint h){

    saveProfile();

}

void JNICALL Java_com_stereo_ManualCalib_glutils_myGLUtils_stop
  (JNIEnv *env, jclass cls){
	stop();

}


int JNICALL Java_com_stereo_ManualCalib_glutils_myGLUtils_getx
  (JNIEnv *env, jclass cls){
	return 0;
}

int JNICALL Java_com_stereo_ManualCalib_glutils_myGLUtils_gety
  (JNIEnv *env, jclass cls){
	return 0;
}

int JNICALL Java_com_stereo_ManualCalib_glutils_myGLUtils_getd
  (JNIEnv *env, jclass cls){
	return 0;
}

int JNICALL Java_com_stereo_ManualCalib_glutils_myGLUtils_getdis
  (JNIEnv *env, jclass cls){
	return 0;//getdis();
}

void JNICALL Java_com_stereo_ManualCalib_glutils_myGLUtils_takep
  (JNIEnv *env, jclass cls){
	//setParm();
}


#ifdef __cplusplus
}
#endif

