#ifndef __FFPLAYER_LOG_H_
#define __FFPLAYER_LOG_H_
#include <android/log.h>

#define XWL 1
#define FQ 1
#define FA 1
#define FV 1
#define FI 1

#ifndef TAG
#define TAG "Holography"
#endif

#define LOGV(...) __android_log_print(ANDROID_LOG_VERBOSE, TAG, __VA_ARGS__)
#define LOGD(...) __android_log_print(ANDROID_LOG_DEBUG ,  TAG, __VA_ARGS__)
#define LOGI(...) __android_log_print(ANDROID_LOG_INFO ,   TAG, __VA_ARGS__)
#define LOGW(...) __android_log_print(ANDROID_LOG_WARN ,   TAG, __VA_ARGS__)
#define LOGE(...) __android_log_print(ANDROID_LOG_ERROR ,  TAG, __VA_ARGS__)

#if XWL
#define LOGXWL(x, ...) LOGV("%s(%d), "x,__FUNCTION__,__LINE__, ##__VA_ARGS__)
#else
#define LOGXWL(x,...) (void)0
#endif

//#define LOGT(x, ...) LOGV("func:%s,line:%d, "x, __FUNCTION__,__LINE__, ##__VA_ARGS__)
#define LOGT(x,...) (void)0

#if FI
#define LOGFI(x, ...) LOGV("func:%s,line:%d, "x,__FUNCTION__,__LINE__, ##__VA_ARGS__)
#else
#define LOGFI(x,...) (void)0
#endif

#if FQ
#define LOGFQ(x, ...) LOGV("file:%s,func:%s,line:%d, "x, __FILE__, __FUNCTION__,__LINE__, ##__VA_ARGS__)
#else
#define LOGFQ(x,...) (void)0
#endif

#if FA
#define LOGFA(x, ...) LOGV("file:%s, func:%s,line:%d, "x,__FILE__, __FUNCTION__,__LINE__, ##__VA_ARGS__)
#else
#define LOGFA(x,...) (void)0
#endif

#if FV
#define LOGFV(x, ...) LOGV("file:%s,func:%s,line:%d, "x, __FILE__, __FUNCTION__,__LINE__, ##__VA_ARGS__)
#else
#define LOGFV(x,...) (void)0
#endif

//#define LOGGL(x, ...) LOGV("file:%s,func:%s,line:%d, "x, __FILE__, __FUNCTION__,__LINE__, ##__VA_ARGS__)
#define LOGGL(x,...) (void)0


#endif
