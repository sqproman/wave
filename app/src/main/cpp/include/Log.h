//
// Created by Roman on 06.10.2024.
//

#pragma once

#include "android/log.h"

#ifndef NDEBUG
#define LOGD(args...) \
__android_log_print(android_LogPriority::ANDROID_LOG_DEBUG, "WaveSynthesizer", args)
#else
#define LOGD(args...)
#endif