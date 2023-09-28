package com.example.strokematching

class StrokeMatchingWrapper {

    companion object {
        init {
            System.loadLibrary("strokematching")
        }
    }

    external fun passCheckDistance(pointsA: ArrayList<FloatPoint>, pointsB: ArrayList<FloatPoint>): Boolean
}