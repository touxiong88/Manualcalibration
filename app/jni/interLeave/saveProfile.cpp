#include <stdio.h>
#include <stdlib.h>
#include <time.h>
#include <string.h>
#include <stdint.h>
#include <fcntl.h>
#include <errno.h>
#include <unistd.h>

//
#include "profile.h"
#include "fflog.h"

#define PROFILE "/sdcard/HolographyProfile"



void saveProfileFileT2(double angle,int baseY,int baseView, uint32_t *data,int num){

	LOGFI("Save Profile ***************************************");
	LOGFI("angle:			%f",angle);
	LOGFI("baseY:			%u",baseY);
	LOGFI("baseView:			%u",baseView);
	LOGFI("number of Calibration:		%d",num);

        char file[256] = "/sdcard/HolographyProfile";
    
	int fd = open(file,O_RDWR | O_CREAT | O_TRUNC, 0666);

	int opened = 1;
	if(fd < 0){
		LOGFI("open1 %s err %d\n",file,errno);
	}

	int le = 192;
	int header = 0x4f4c4f48;
	
	
	int ret = write(fd,&header,sizeof(uint32_t));
	LOGFI("written  %d, %s.",ret,strerror(errno));
	ret += write(fd,&angle,sizeof(double));
	LOGFI("written  %d, %s.",ret,strerror(errno));
	ret += write(fd,&baseY,sizeof(uint32_t));
	LOGFI("written  %d, %s.",ret,strerror(errno));
	ret += write(fd,&baseView,sizeof(uint32_t));
	LOGFI("written  %d, %s.",ret,strerror(errno));
	ret += write(fd,&num,sizeof(uint32_t));
	LOGFI("written  %d, %s.",ret,strerror(errno));
	for(int i = 0; i < num ; i++){
	//	LOGFI("[%d]:%x",i,data[i]);
	}

      int datasize = num*sizeof(uint16_t); 
      uint16_t *data2 = (uint16_t*)malloc(datasize);
      
      for(int i = 0; i < num; i++){

            data2[i] = data[i];

       }
    
	ret += write(fd,data2,datasize);
	LOGFI("%p",data);
	LOGFI("written  %d, %s.",ret,strerror(errno));
	

	if(ret < 192){
		le = 192-ret;
		int tmp[192] = {0};
		ret += write(fd,tmp,le);
		LOGFI("written  %d, %d of them filled by '0'.",ret,le);
	}
	close(fd);

	LOGFI("Save Profile ***************************************");
}



int readProfileFileInternal(char *path,HolographyProfile *profile){
	
	if(profile == NULL){
		LOGFI("profile is NULL,malloc space before  read profile");
		return -1;
	}

	int fd = open(path,O_RDONLY);
	if(fd < 0){
		LOGFI("opened error");
		return -1;
	}

	int ret = read(fd,&profile->header,sizeof(uint32_t));

	if(profile->header != 0x4f4c4f48){
		LOGFI("It is not an Holography  Profile!");
		LOGFI("%x",profile->header);
		close(fd);
		return -2;
	}

	ret += read(fd,&profile->angle,sizeof(double));
	ret += read(fd,&profile->baseY,sizeof(uint32_t));
	ret += read(fd,&profile->baseView,sizeof(uint32_t));
	ret += read(fd,&profile->num,sizeof(uint32_t));

	int size = profile->num*sizeof(uint16_t);
	uint16_t *data = (uint16_t*)malloc(size);
    
	profile->data = (uint32_t*)malloc(profile->num*sizeof(uint32_t));

	int ret2 = read(fd,data,size);

      for(int i = 0; i < profile->num; i++){

            profile->data[i] = data[i];

       }

    
	ret += ret2;

	LOGFI("data size:		%d ",ret2);
	LOGFI("gblhpfh read:	%d\n",ret);

	close(fd);
	LOGFI("angle:			%f",profile->angle);
	LOGFI("baseY:			%u",profile->baseY);
	LOGFI("baseView:			%u",profile->baseView);
	LOGFI("number of Calibration:		%d",profile->num);


    profile->cwidth = 800;
    profile->cheight = 600;
    profile->gwidth = 3.0;

	return 0;
}

int  readProfileFileT2(HolographyProfile *profile){
	
	LOGXWL();

	int ret  = readProfileFileInternal(  (char*) PROFILE,profile);

	if(ret  == 0){
		LOGFI("use data");
		return  0;
	}
/*
	ret = readProfileFileInternal( (char*)PROFILE2,profile);

	if(ret  == 0){
		LOGFI("use sdcard");
		return  0;
	}


	ret = readProfileFileInternal( (char*)PROFILE3,profile);

	if(ret  == 0){
		LOGFI("use android");
		return  0;
	}
	*/
	return -1;
}





int saveProfileFileEmptyT2(double aangle){

	LOGFI();

	double  angle = 17.780000;
	int baseY = 300;
	int baseView = 16;
	int num = 90;
	uint32_t data[] = {
	720,714,710,701,695,
	690,683,678,672,666,
	661,654,648,643,638,
	631,625,620,615,610,
	605,598,595,588,583,
	577,573,567,562,557,
	552,546,542,537,533,
	532,525,521,515,511,
	506,505,500,496,491,
	485,484,480,476,470,
	466,459,454,450,445,
	440,435,430,425,420,
	415,411,405,399,395,
	389,382,378,374,368,
	363,357,352,346,342,
	336,331,324,320,314,
	309,304,298,293,287,
	283,276,271,267,214};

    saveProfileFileT2(angle,baseY,baseView,data,num);

    return 0;

}









