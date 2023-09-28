//
// Created by Ganang Arief Pratama on 28/09/23.
//

#include "android/log.h"

#ifndef NDEBUG
#define LOGD(args...) \
__android_log_print(android_LogPriority::ANDROID_LOG_DEBUG, "StrokeMatching", args)
#else
#define LOGD(args)
#endif
