//
// Created by Ganang Arief Pratama on 28/09/23.
//

#include "unordered_map"
#include "vector"
#include "StrokeMatching.h"
#include "Log.h"

using namespace std;

namespace strokeMatching {
    int countA = 0;
    int countB = 0;
    float threshold = 80;
    unordered_map<int, float> memoizedDFD;
    vector <Point2f> pathA;
    vector <Point2f> pathB;

    bool StrokeMatching::passFrechetDistance(vector <Point2f> pointsA, vector <Point2f> pointsB) {
        pathA = pointsA;
        pathB = pointsB;

        countA = pointsA.size();
        countB = pointsB.size();

        int indexA = countA - 1;
        int indexB = countB - 1;

        float distance = recursiveDFD(indexA, indexB, threshold);

        LOGD("=> distance %f", distance);
        LOGD("=> threshold %f", threshold);
        memoizedDFD = unordered_map<int, float>();

        return (distance < 30);
    }

    float StrokeMatching::recursiveDFD(int indexA, int indexB, float maxThreshold) {
        int memoizedIndex = indexA + countA * indexB;
        // Check that the value has not already been solved.
        if (memoizedDFD.find(memoizedIndex) != memoizedDFD.end()) {
            return memoizedDFD[memoizedIndex];
        }
        float result;
        float pointPairDistance = sqrt(pow(pathA[indexA].x - pathB[indexB].x, 2) + pow(pathA[indexA].y - pathB[indexB].y, 2));

        if (indexA == 0 && indexB == 0) {
            // If just checking the first two points, the cost is the distance between the points.
            result = pointPairDistance;
        } else if (pointPairDistance > maxThreshold) {
            // Exit early if this value will never be used, this prunes the search tree.
            result = pointPairDistance;
        } else if (indexB == 0) {
            // If at the start of path B, move towards the start of path A.
            result = max(recursiveDFD(indexA - 1, 0, maxThreshold), pointPairDistance);
        } else if (indexA == 0) {
            // If at the start of path A, move towards the start of path B.
            result = max(recursiveDFD(0, indexB - 1, maxThreshold), pointPairDistance);
        } else {
            // Return the minimum of moving towards the start of A, B, or A & B.
            float diagonalDFD = recursiveDFD(indexA - 1, indexB - 1, maxThreshold);
            float towardsADFD = recursiveDFD(indexA - 1, 0, maxThreshold);
            float towardsBDFD = recursiveDFD(0, indexB - 1, maxThreshold);
            result = min(min(diagonalDFD, towardsADFD), min(towardsBDFD, pointPairDistance));
        }
        memoizedDFD[memoizedIndex] = result;
        return result;
    }
}
