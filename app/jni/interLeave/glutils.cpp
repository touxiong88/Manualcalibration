#include "myGLUtils.h"
#include "fflog.h"
#include <string.h>
#include <math.h>
#include <stdint.h>
#include <fcntl.h>
#include "ffthread.h"
#include <stdio.h>
#include <stdlib.h>
#include "profile.h"

#define TAG "Holography"
#define MATRIX_PATH "/sdcard/matrix"

double inc_x;
double per_inc_x;
double pxwid; //光栅宽度--光栅单位占像素个数

float tanAf = 0.320872;
double tanAf_3h;
double tanAf_3h2;

double gs1 = 0.2;;
double gs2 = 1.4;
double gs3 = 1.8;
double gs4 = 3.0;

int gWidth = 1920;
int gHeight = 1080;
int gSize = 1920*1080;

uint8_t *bufRed;
uint8_t *bufGreen;
uint8_t *bufBlue;


HolographyProfile *profile;

double gAngle;
int baseView ;


#define etime(beg) \
	long end = getTimeMs(); \
	LOGXWL("beg %ld, end %ld,update took: %ld",beg,end,end - beg);\

void calImage2(double delt_s);


void printData(uint8_t *data){
	char buf[1024];
	int i;
	char tmp[2];
	for(i=0;i < 30;i++){
		sprintf(tmp,"%x ",data[i]);
		strcat(buf,tmp);
	}
	LOGFI("%s",buf);
}


void readProfile(){
    LOGFI();
    if(profile != NULL){
        delete profile;
    }
    profile = new HolographyProfile();

    int ret = readProfileFile(profile);

 //   int ret = readProfileFileT2(profile);
    if(ret == 0){
        gAngle = profile->angle;
        pxwid  = profile->gwidth;
        baseView = profile->baseView;
    }
}

void saveProfile(){
/*    if(!g_getProfile){
        LOGFI("no  profile");
        saveProfileFileEmptyT2(gAngle);
        readProfile();
        return;
    }*/
    LOGFI("baseView  %d",baseView);
    saveProfileFile(gAngle,pxwid,PREVIEW_WIDTH,PREVIEW_HEIGHT,BASE_Y,baseView,profile->data,profile->num);

    saveProfileFileT2(gAngle,profile->baseY,baseView,profile->data,profile->num);

}

void init(double angle,double gw,int w,int h){

	LOGFI();
	gWidth = w;
	gHeight = h;
	gSize = w*h;
	gAngle = angle;
	pxwid = gw;

	LOGFI("angle:%f,pxwid: %f,%dx%d,size:%d",angle,pxwid,w,h,gSize);

    readProfile();
	LOGFI("angle:%f,pxwid: %f,%dx%d,size:%d",angle,pxwid,w,h,gSize);

	inc_x = 2 / cos(angle *M_PI / 180);
	per_inc_x = inc_x/32.0;

	tanAf =  tan(angle * M_PI / 180); //tan(angle)
	tanAf_3h = tanAf/3;//tan(angle)/3
	tanAf_3h2 = 2*tanAf/3;

	bufBlue = (uint8_t*)malloc(gSize);//分辨率大小
	bufGreen = (uint8_t*)malloc(gSize);
	bufRed = (uint8_t*)malloc(gSize);

	calImage2(0);
}

void save_angle(){
	FILE *fp = fopen("/sdcard/Android/angle.txt","w+");
	if(!fp){
		LOGFI("cannot open file ,%d",errno);
		return;
	}
	fprintf(fp,"%f",gAngle);
	fclose(fp);
}

//getColorRatio(dis_test,0);
double getColorRatio(double dis_test,double int_x_v){//int_x_v = 0 根据角度获取颜色取值比例

		double tmp;
		if(dis_test <= gs1+int_x_v){
		 tmp = (1-(dis_test+inc_x-gs4)/(inc_x+gs1-gs4));
		}else if(dis_test <= gs2+int_x_v){
			tmp = 0.0;
		}else if(dis_test <= gs3+int_x_v){
			tmp = (dis_test - gs2)/(gs3-gs2);
		}else if(dis_test <= gs4+int_x_v){
			tmp = 1.0;
		}else{
			tmp = (1-(dis_test-gs4)/(inc_x+gs1-gs4));
		}
    return tmp;
}

int pre_s[4] = {20,140,180,300};

double delt_p = 0.00025;
double delt_s1=0.0;

//function 根据角度 光栅宽度更新 一帧所有像素RGB值
void calImage2(double delt_s){//形参 delt_s = 0

	LOGFI("pxwid %f, angle %f",pxwid,gAngle);
	inc_x = pxwid / cos(gAngle *M_PI / 180);
	per_inc_x = inc_x/320.0;//32等分?

	tanAf =  tan(gAngle * M_PI / 180);//角度正切值
//	tanAf_3h = tanAf/3;
//	tanAf_3h2 = tanAf_3h*2;


	gs1 = (double)pre_s[0]*per_inc_x;//不等分取值
	gs2 = (double)pre_s[1]*per_inc_x;
	gs3 = (double)pre_s[2]*per_inc_x;
	gs4 = (double)pre_s[3]*per_inc_x;

	LOGXWL("gs:%f,%f,%f,%f,%.16lf",gs1,gs2,gs3,gs4,delt_p);
	LOGXWL("delt_p:%.16lf",delt_p);

	LOGFI("tanAf:%f,tanAf_3h %f,tanAf_3h2 %f",tanAf,tanAf_3h,tanAf_3h2);
	int i,j;
	double y_pos = 0;
	double delt = 0;
	double dis_test = 0;

	uint8_t blue;
	uint8_t green;
	uint8_t red;

	int pos = 0;
	int pos1 = 0;

	double tmp = 0.0;

	for (i=0;i<gHeight;i++){

		y_pos = i*tanAf+delt_s;
	    delt = fmod(y_pos,inc_x);//y/x 取模求余
	    pos1=i*gWidth;

		for(j=0;j<gWidth;j++){

	        dis_test = fmod((inc_x + j - delt+j*delt_p),inc_x);
	        pos = pos1 + (gWidth - j - 1);

	        tmp = getColorRatio(dis_test,0);//根据数据获取坐标颜色比例
	        blue = tmp *255;
	        bufBlue[pos] = blue;

	        tmp = getColorRatio(fmod(dis_test+tanAf_3h,inc_x),0);
	        green = tmp *255;
	        bufGreen[pos] = green;

	        tmp = getColorRatio(fmod(dis_test+tanAf_3h2,inc_x),0);
	        red = tmp *255;
	        bufRed[pos] = red;
		}
	}
	LOGXWL("%f",gAngle);
	save_angle();
	LOGFI("delt_s:%.16lf",delt_s1);
	LOGFI("finish\n\n");

	char *str = (char*)malloc(1024);

	uint8_t *tmpbuffer;

		tmpbuffer = bufBlue;
		memset(str,0,1024);
		char buf[4] = {'\0'};
		for(j=20000;j<20008;j++){
			sprintf(buf,"%x ",tmpbuffer[j]);
			strcat(str,buf);
		}
		LOGFI("buffer[%d]:%s",i,str);

	free(str);

}
#define RED 1
#define GREEN 2
#define BLUE 3
void updateImage(int corlor,int mDelt,float angle,float ds,float dp){

	if(fabs(gAngle - angle) > 0.00001 || fabs(pxwid - ds) > 0.00001){
		LOGFI("gAngle: %6.6f   angle %6.6f pre pxwid  %6.6f, new pxwid %f",gAngle,angle,pxwid,ds);
		gAngle = angle;
		pxwid = ds;
		calImage2(0);// 根据角度 光栅宽度更新 一帧所有像素RGB值
	}

	glPixelStorei ( GL_UNPACK_ALIGNMENT, 1 );//通过GL_PACK_ALIGMENT告诉OpenGL从颜色缓冲区读取的数据如何打包然后放到用户指定的内存空间中去

	if(corlor == RED){

		glTexImage2D ( GL_TEXTURE_2D, 0, GL_LUMINANCE, gWidth, gHeight, 0, GL_LUMINANCE, GL_UNSIGNED_BYTE, bufBlue);//根据指定的参数，生成一个2D纹理（Texture） 重新生成 Blue纹理
	}else if(corlor == GREEN){

		glTexImage2D ( GL_TEXTURE_2D, 0, GL_LUMINANCE, gWidth, gHeight, 0, GL_LUMINANCE, GL_UNSIGNED_BYTE, bufGreen);//根据指定的参数，生成一个2D纹理（Texture）
	}else if(corlor == BLUE){

		glTexImage2D ( GL_TEXTURE_2D, 0, GL_LUMINANCE, gWidth, gHeight, 0, GL_LUMINANCE, GL_UNSIGNED_BYTE, bufRed);//根据指定的参数，生成一个2D纹理（Texture）
	}

}

void readFrame(){
}

