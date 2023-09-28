//
// Created by Ganang Arief Pratama on 28/09/23.
//

#pragma once

#include "vector"

using namespace std;

class Point2f {
public:
    float x;
    float y;
    Point2f(float x, float y) : x(x), y(y) {}
};

namespace strokeMatching {
    class StrokeMatching {
    public:
        bool passFrechetDistance(vector<Point2f> pointsA, vector<Point2f> pointsB);
        float recursiveDFD(int indexA, int indexB, float maxThreshold);
    };
}
