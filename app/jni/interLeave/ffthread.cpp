#define TAG "Holography"
#include "ffthread.h"
#include "fflog.h"
#include <sys/prctl.h>
#ifdef __cplusplus
extern "C" {
#endif

pthread_t FF_CreateThread(void* (fn) (void *),const char *name, void *data)
{
    pthread_attr_t type;
    pthread_t  tid = -1;

    if (pthread_attr_init(&type) != 0) {
        LOGFI("Couldn't initialize pthread attributes");
        return (-1);
    }
    pthread_attr_setdetachstate(&type, PTHREAD_CREATE_JOINABLE);


    if (pthread_create(&tid, &type, fn, data) != 0) {
        LOGFI("Not enough resources to create thread");
        return (-1);
    }
	
    LOGFI("tid: %u,name: %s", (unsigned int)tid,name);
    prctl(PR_SET_NAME, (unsigned long)(name), 0, 0, 0);

    return tid;
}

void FF_WaitThread(pthread_t  tid)
{
	LOGFI("tid %d",tid);
    pthread_join(tid, 0);
}

int androidSetThreadPriority(pid_t tid, int pri)
{
    int rc = 0;


    if (setpriority(PRIO_PROCESS, tid, pri) < 0) {
        rc = -ENOSYS;
    }
    return rc;
}

static long basesec = 0;
long getTimeMs() {
    struct timeval tv;
    long nowtime;
    gettimeofday(&tv, 0);
    if (basesec == 0) {
        basesec = tv.tv_sec;
    }
    nowtime = (int64_t)(tv.tv_sec - basesec) * (int64_t)1000 +
              (int64_t)tv.tv_usec/1E3;
    return nowtime;
}



#ifdef __cplusplus
}
#endif
