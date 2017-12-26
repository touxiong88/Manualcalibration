LOCAL_PATH := $(call my-dir)

#include $(CLEAR_VARS)
#LOCAL_MODULE := opencv_java
#LOCAL_SRC_FILES := libopencv_java.so
#include $(PREBUILT_SHARED_LIBRARY)

include $(CLEAR_VARS)


include $(CLEAR_VARS)


LOCAL_SRC_FILES :=  \
		glutils.cpp \
		myGLUtilsJni.cpp \
		ffthread.cpp \
		profile.cpp	\
		saveProfile.cpp
		

LOCAL_C_INCLUDES += 

LOCAL_LDLIBS := -llog -lm -lGLESv2  
LOCAL_MODULE    := myGLUtils

include $(BUILD_SHARED_LIBRARY)
