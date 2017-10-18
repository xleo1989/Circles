package com.x.leo.circles

import android.animation.ValueAnimator
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.*
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.util.Log
import android.view.View

/**
 * @作者:XLEO
 * @创建日期: 2017/9/30 10:18
 * @描述:${TODO}
 *
 * @更新者:${Author}$
 * @更新时间:${Date}$
 * @更新描述:${TODO}
 * @下一步：
 */
class LoadIngView(ctx: Context, attrs: AttributeSet?) : View(ctx, attrs) {
    private val NORES: Int = -1

    var baseRes: Int = NORES

    var duration: Int = 300

    var outCircleWidth: Int = 200

    var animateStyle: AnimateStyle = AnimateStyle.CIRCLE_STYLE

    private var baseDrawable: Drawable? = null

    private val animte: ValueAnimator by lazy {
        ValueAnimator.ofInt(0, 360)
    }

    private val centerPoint: Point by lazy {
        Point(measuredWidth / 2, measuredHeight / 2)
    }
    var outerInnerSpanFaction: Float = 0.2f
    private val TAG = "LoadingView"

    private val outerInnerSpan: Int by lazy {
        logd("==outerInnnerSpan==outCircleWidth:" + outCircleWidth)
        when (animateStyle) {
            AnimateStyle.CIRCLE_STYLE -> {
                (outCircleWidth * outerInnerSpanFaction).toInt()
            }
            AnimateStyle.CIRCLE_STYLE_2 -> {
                (outCircleWidth * outerInnerSpanFaction).toInt()
            }
            AnimateStyle.CIRCLE_STYLE_3 -> {
                (outCircleWidth * outerInnerSpanFaction).toInt() / 4
            }
            else -> {
                throw IllegalArgumentException("wrong animate style")
            }
        }
    }
    private val ovalRadius: Int by lazy {
        logd("==ovalRadius=="+"outCircleWidth:" + outCircleWidth + "\n outInnerSpan:" + outerInnerSpan)
        when (animateStyle) {
            AnimateStyle.CIRCLE_STYLE -> {
                (outCircleWidth - outerInnerSpan) / 2
            }
            AnimateStyle.CIRCLE_STYLE_2 -> {
                (outCircleWidth - outerInnerSpan) / 2
            }
            AnimateStyle.CIRCLE_STYLE_3 -> {
                (outCircleWidth - outerInnerSpan * 4) / 2
            }
            else -> {
                (outCircleWidth - outerInnerSpan) / 2
            }
        }
    }

    private val outCircleRadius: Int by lazy {
        logd("==outCircleRadius==" + "ovalRadius:" + ovalRadius + "\nouterINnerSpan:" + outerInnerSpan)
        if (baseHeight > baseWidth) baseHeight / 2 else baseWidth / 2 + outerInnerSpan + ovalRadius
    }

    private var mAnimatedValue: Int = 0
    private var baseWidth: Int = 0
    private var baseHeight: Int = 0
    var openAngle: Int = 90

    private var ovalSpanAngle: Int = 10
    private val angleDiff: Int by lazy {
        Math.toDegrees(Math.asin(ovalRadius.toDouble() / outCircleRadius) * 2).toInt() + ovalSpanAngle
    }

    private val mPaint: Paint by lazy {
        Paint(Paint.ANTI_ALIAS_FLAG)
    }

    var ovalColor: Int = Color.BLACK

    init {
        if (attrs != null) {
            val oAttr = ctx.obtainStyledAttributes(attrs, R.styleable.LoadIngView)
            baseRes = oAttr.getResourceId(R.styleable.LoadIngView_baseRes, NORES)
            baseDrawable = if (baseRes == NORES) {
                null
            } else {
                resources.getDrawable(baseRes)
            }
            baseWidth = if (baseDrawable == null) {
                0
            } else {
                baseDrawable!!.intrinsicWidth
            }
            baseHeight = if (baseDrawable == null) {
                0
            } else {
                baseDrawable!!.intrinsicHeight
            }
            animateStyle = AnimateStyle.getStyleByOrder(oAttr.getInt(R.styleable.LoadIngView_animateStyle, 0))
            duration = oAttr.getInt(R.styleable.LoadIngView_animateDuration, 300)
            outCircleWidth = oAttr.getDimensionPixelSize(R.styleable.LoadIngView_outCircleWidth, 100)
            ovalSpanAngle = oAttr.getInt(R.styleable.LoadIngView_ovalSpanAngle, 10)
            openAngle = oAttr.getInt(R.styleable.LoadIngView_openAngle, 90)
            outerInnerSpanFaction = oAttr.getFraction(R.styleable.LoadIngView_outInnerMargin, 1, 1, 0.2f)
            ovalColor = oAttr.getColor(R.styleable.LoadIngView_ovalColor, Color.parseColor("#fffd9236"))
            oAttr.recycle()
            logd("==init==")
        }
    }

    private val doLog: Boolean = true

    private fun logd(s:String){
        if(doLog){
            Log.d(TAG,s)
        }
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        startAnimate()
    }

    private fun startAnimate() {
        if (animte.isRunning) {
            endAnimate()
        }
        animte.apply {
            duration = this@LoadIngView.duration.toLong()
            repeatCount = ValueAnimator.INFINITE
            repeatMode = ValueAnimator.RESTART
            addUpdateListener { animation ->
                val animatedValue = animation.getAnimatedValue() as Int
                when (animateStyle) {
                    AnimateStyle.CIRCLE_STYLE -> {
                        mAnimatedValue = animatedValue
                    }
                    else -> {
                        mAnimatedValue = animatedValue
                    }
                }
                invalidate()
            }
            start()
        }
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        endAnimate()
    }

    private fun endAnimate() {
        if (animte.isRunning) {
            animte.removeAllUpdateListeners()
            animte.cancel()
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val widthMode = MeasureSpec.getMode(widthMeasureSpec)
        val widthMeasured = MeasureSpec.getSize(widthMeasureSpec)
        val heightMode = MeasureSpec.getMode(heightMeasureSpec)
        val heightMeasured = MeasureSpec.getSize(heightMeasureSpec)

        var resultWidth = baseWidth + outCircleWidth * 2
        var resultHeight = baseHeight + outCircleWidth * 2
        if (widthMode == MeasureSpec.EXACTLY) {
            if (widthMeasured == 0) {
                super.onMeasure(widthMeasureSpec, heightMeasureSpec)
            } else if (widthMeasured < resultWidth) {
                throw IllegalArgumentException("width is not big enough")
            } else {
                resultWidth = widthMeasured
            }
        }
        if (heightMode == MeasureSpec.EXACTLY) {
            if (heightMeasured == 0) {
                super.onMeasure(widthMeasureSpec, heightMeasureSpec)
            } else if (heightMeasured < resultHeight) {
                throw  IllegalArgumentException("height is not big enough")
            } else {
                resultHeight = heightMeasured
            }
        }
        setMeasuredDimension(resultWidth, resultHeight)
    }

    override fun onDraw(canvas: Canvas?) {
        if (canvas == null) {
            return
        }
//        logd("measuredWidth:" + measuredWidth + "\nmeasuredHeight:" + measuredHeight
//         + "\noutCicleWidth" + outCircleWidth + "\n baseWidth" + baseWidth + "\n baseHeight:" + baseHeight
//         + "\noutCicleRadius" + outCircleRadius + "\novalRadius" + ovalRadius + "\noutInerSpan" + outerInnerSpan)
        background?.draw(canvas)
        mPaint.alpha = 100
        drawBasDrawable(canvas!!)
        mPaint.color = ovalColor
        mPaint.style = Paint.Style.FILL
        when (animateStyle) {
            AnimateStyle.CIRCLE_STYLE -> {
                drawOutCircles(canvas)
            }
            AnimateStyle.CIRCLE_STYLE_2 -> {
                drawCircleShandow(canvas)
            }
            AnimateStyle.CIRCLE_STYLE_3 -> {
                drawShakeCircles(canvas)
            }
            else -> {
                throw IllegalArgumentException("error animate style")
            }
        }
    }

    private fun drawShakeCircles(canvas: Canvas) {
        var temp = mAnimatedValue
        val centerDiff = outerInnerSpan * 3 * if (mAnimatedValue.toFloat() / 180 >= 1) 2 - mAnimatedValue.toFloat() / 180 else mAnimatedValue.toFloat() / 180
        while (temp - mAnimatedValue < 360 - openAngle) {
            val currAlpha = (temp - mAnimatedValue).toFloat() / (360 - openAngle) * 70 + 30
            if (temp + angleDiff - mAnimatedValue <= 360 - openAngle) {
                drawCircle(temp, currAlpha.toInt(), canvas, centerDiff.toInt())
            }
            temp += angleDiff
        }
    }

    private fun drawCircleShandow(canvas: Canvas) {
        var temp = 0
        while (temp < mAnimatedValue) {
            val currAlpha = temp.toFloat() / mAnimatedValue * 100
            drawCircle(temp, currAlpha.toInt(), canvas, 0)
            temp += angleDiff
        }
    }


    private fun drawOutCircles(canvas: Canvas) {
        var temp = mAnimatedValue
        while (temp - mAnimatedValue < 360 - openAngle) {
            val currAlpha = (temp - mAnimatedValue).toFloat() / (360 - openAngle) * 70 + 30
            if (temp + angleDiff - mAnimatedValue <= 360 - openAngle)
                drawCircle(temp, currAlpha.toInt(), canvas, 0)
            temp += angleDiff
        }
    }

    private fun drawCircle(angle: Int, currAlpha: Int, canvas: Canvas, centerDiff: Int) {
        val xCenter = centerPoint.x + (outCircleRadius + centerDiff) * Math.cos(Math.toRadians(angle.toDouble()))
        val yCenter = centerPoint.y - (outCircleRadius + centerDiff) * Math.sin(Math.toRadians(angle.toDouble()))
        mPaint.alpha = currAlpha
        canvas.drawCircle(xCenter.toFloat(), yCenter.toFloat(), ovalRadius.toFloat(), mPaint)
    }


    private fun drawBasDrawable(canvas: Canvas) {
        if (baseDrawable != null) {
            baseDrawable!!.bounds = Rect(centerPoint.x - baseDrawable!!.intrinsicWidth / 2, centerPoint.y - baseDrawable!!.intrinsicHeight / 2, centerPoint.x + baseDrawable!!.intrinsicWidth / 2, centerPoint.y + baseDrawable!!.intrinsicHeight / 2)
            baseDrawable!!.draw(canvas)
        }
    }
}

enum class AnimateStyle {
    CIRCLE_STYLE, CIRCLE_STYLE_2, CIRCLE_STYLE_3;

    companion object {
        fun getStyleByOrder(order: Int): AnimateStyle {
            return when (order) {
                0 -> {
                    CIRCLE_STYLE
                }
                1 -> {
                    CIRCLE_STYLE_2
                }
                2 -> {
                    CIRCLE_STYLE_3
                }
                else -> {
                    CIRCLE_STYLE
                }
            }
        }
    }
}