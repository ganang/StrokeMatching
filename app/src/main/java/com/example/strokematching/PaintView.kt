package com.example.strokematching

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.Toast

class FloatPoint(var x: Float, var y: Float)
class PaintView: View {

    private var params: ViewGroup.LayoutParams? = null
    private var defaultPaintBrush = Paint()
    private var paintBrush = Paint()
    private var path = Path()
    private var strokes = ArrayList<ArrayList<Path>>()
    private var currentPoints = ArrayList<FloatPoint>()
    private var currentCounter = 0
    private var strokeMatchingWrapper = StrokeMatchingWrapper()

    companion object {
        var finishCounter = 0
        var jsonPoints = ArrayList<ArrayList<FloatPoint>>()
        var defaultStrokes = ArrayList<Path>()
        var defaultPoints = ArrayList<ArrayList<FloatPoint>>()
    }

    constructor(context: Context) : this(context, null) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init()
    }

    private fun init() {
        defaultPaintBrush.isAntiAlias = true
        defaultPaintBrush.color = Color.BLACK
        defaultPaintBrush.style = Paint.Style.STROKE
        defaultPaintBrush.strokeJoin = Paint.Join.ROUND
        defaultPaintBrush.strokeWidth = 34f

        paintBrush.isAntiAlias = true
        paintBrush.color = Color.GREEN
        paintBrush.style = Paint.Style.STROKE
        paintBrush.strokeJoin = Paint.Join.ROUND
        paintBrush.strokeWidth = 34f

        strokes.add(ArrayList())

        params = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        val x = event.x
        val y = event.y

        when(event.action) {
            MotionEvent.ACTION_DOWN -> {
                path.moveTo(x, y)
                strokes.last().add(path)
                currentPoints.add(FloatPoint(x, y))
                return true
            }

            MotionEvent.ACTION_MOVE -> {
                path.lineTo(x, y)
                strokes.last().add(path)
                currentPoints.add(FloatPoint(x, y))
            }

            MotionEvent.ACTION_UP -> {
                val cloneDefaultPoints = defaultPoints[currentCounter].clone() as ArrayList<FloatPoint>
                val cloneCurrentPoints = currentPoints.clone() as ArrayList<FloatPoint>

                if (strokeMatchingWrapper.passCheckDistance(cloneDefaultPoints, cloneCurrentPoints)) {
                    currentCounter += 1
                } else {
                    strokes.removeLast()
                }

                Log.d("=> currentCounter", currentCounter.toString())
                Log.d("=> finishCounter", finishCounter.toString())
                if (currentCounter == finishCounter) {
                    Toast.makeText(context, "JAWA JAWA JAWA", Toast.LENGTH_SHORT).show()
                }

                jsonPoints.add(cloneCurrentPoints)
                strokes.add(ArrayList())
                currentPoints.clear()
                path = Path()
            }

            else -> return false
        }

        postInvalidate()
        return false
    }

    override fun onDraw(canvas: Canvas) {
        for(stroke in defaultStrokes) {
            canvas.drawPath(stroke, defaultPaintBrush)
            invalidate()
        }

        for(stroke in strokes) {
            for(path in stroke) {
                canvas.drawPath(path, paintBrush)
                invalidate()
            }
        }
    }
}