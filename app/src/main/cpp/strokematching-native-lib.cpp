#include <jni.h>
#include "header/StrokeMatching.h"
#include "Log.h"

using namespace std;
using namespace strokeMatching;

extern "C"
JNIEXPORT jboolean JNICALL
Java_com_example_strokematching_StrokeMatchingWrapper_passCheckDistance(
        JNIEnv *env, jobject thiz,
        jobject pointsA,
        jobject pointsB) {

    auto strokeMatching = make_unique<StrokeMatching>();
    jclass arrayList = env->FindClass("java/util/ArrayList");
    jclass floatPoint = env->FindClass("com/example/strokematching/FloatPoint");

    if (arrayList == nullptr || floatPoint == nullptr) {
        return false;
    }

    jmethodID alGetId  = env->GetMethodID(arrayList, "get", "(I)Ljava/lang/Object;");
    jmethodID alSizeId = env->GetMethodID(arrayList, "size", "()I");
    jmethodID ptGetXId = env->GetMethodID(floatPoint, "getX", "()F");
    jmethodID ptGetYId = env->GetMethodID(floatPoint, "getY", "()F");

    if (alGetId == nullptr || alSizeId == nullptr || ptGetXId == nullptr || ptGetYId == nullptr) {
        env->DeleteLocalRef(arrayList);
        env->DeleteLocalRef(floatPoint);
        return false;
    }

    int pointACount = static_cast<int>(env->CallIntMethod(pointsA, alSizeId));
    int pointBCount = static_cast<int>(env->CallIntMethod(pointsB, alSizeId));

    if (pointACount < 1 || pointBCount < 1) {
        env->DeleteLocalRef(arrayList);
        env->DeleteLocalRef(floatPoint);
        return false;
    }

    vector<Point2f> currentPointsA;
    currentPointsA.reserve(pointACount);
    float xA, yA;

    for (int i = 0; i < pointACount; ++i) {
        jobject point = env->CallObjectMethod(pointsA, alGetId, i);
        xA = static_cast<float >(env->CallFloatMethod(point, ptGetXId));
        yA = static_cast<float >(env->CallFloatMethod(point, ptGetYId));
        env->DeleteLocalRef(point);

        currentPointsA.emplace_back(xA, yA);
    }

    vector<Point2f> currentPointsB;
    currentPointsB.reserve(pointBCount);
    float xB, yB;
    for (int i = 0; i < pointBCount; ++i) {
        jobject point = env->CallObjectMethod(pointsB, alGetId, i);
        xB = static_cast<float >(env->CallFloatMethod(point, ptGetXId));
        yB = static_cast<float >(env->CallFloatMethod(point, ptGetYId));
        env->DeleteLocalRef(point);

        currentPointsB.emplace_back(xB, yB);
    }

    env->DeleteLocalRef(arrayList);
    env->DeleteLocalRef(floatPoint);

    return strokeMatching->passFrechetDistance(currentPointsA, currentPointsB);
}