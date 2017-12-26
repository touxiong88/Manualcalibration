#ifndef  __PROFILE__H
#define __PROFILE__H

typedef struct  HolographyProfileHeader{
	uint32_t header;     	//header  must be 0x4f4c4f48
	double angle;      	//Fit  angle
	uint32_t bias;  
	double gwidth; 
	uint32_t cwidth;
	uint32_t cheight;
	uint32_t baseY;      	// base Y
	uint32_t baseView;   	// base View
	uint32_t num;        	//number of Calibration data
	uint32_t *data;
}HolographyProfile;

int saveProfileFileEmpty(double aangle,double gwidth);
void saveProfileFile(double angle,double gwidth,int cwidth,int cheight,uint32_t baseY,uint32_t baseView, uint32_t *data,int num);

int readProfileFileInternal(char *path,HolographyProfile *profile);
int  readProfileFile(HolographyProfile *profile);
float getAngle(double *gwidth);


void saveProfileFileT2(double angle,int baseY,int baseView, uint32_t *data,int num);
int  readProfileFileT2(HolographyProfile *profile);
int saveProfileFileEmptyT2(double aangle);

#define BASE_Y 300
#define PREVIEW_WIDTH 800
#define PREVIEW_HEIGHT 600

#define InchToStandard 25400
#define gratingGrid 180.308
#define Mi6PPI 428
#define Mi6cm (InchToStandard/Mi6PPI)
//double gratingWidth = (gratingGrid/Mi6cm);

#endif
