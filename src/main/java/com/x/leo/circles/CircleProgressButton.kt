package com.x.leo.circles

import android.animation.Animator
import android.animation.ValueAnimator
import android.content.Context
import android.content.res.ColorStateList
import android.graphics.*
import android.text.TextUtils
import android.util.AttributeSet
import android.view.View
import android.widget.Button

/**
 * @作者:XLEO
 * @创建日期: 2017/8/18 10:04
 * @描述:${TODO}
 *
 * @更新者:${Author}$
 * @更新时间:${Date}$
 * @更新描述:${TODO}
 * @下一步：
 */
class CircleProgressButton(ctx: Context, attrs: AttributeSet?, defStyleAttr: Int) : Button(ctx, attrs, defStyleAttr) {
    private lateinit var circleCenter: PointF
    private var innerRadius: Float = 0f
    private var outterRadius: Float = 0f
    private var outterStrokeWidth: Float = 10f
    private var outterInnerSpace: Float = 10f
    private var duration: Int = 0
    private var mPaint: Paint
    private var colorlist: ColorStateList
    private var progressBackgroundColor: Int = Color.GRAY
    private var progressColor: Int = Color.RED
    private var animator: ValueAnimator? = null
    private var animatedValue: Int = 0
    private var animatorListener: Animator.AnimatorListener? = null
    private lateinit var outRect: RectF
    private val startAngle: Float = 0f
    private var circleAlpha:Float = 1f

    init {
        val attributes = ctx.obtainStyledAttributes(attrs, R.styleable.CircleProgressButton)
        outterInnerSpace = attributes.getDimension(R.styleable.CircleProgressButton_innerOuterSpace, 10f)
        outterStrokeWidth = attributes.getDimension(R.styleable.CircleProgressButton_outerStrokeWidth, 10f)
        progressBackgroundColor = attributes.getColor(R.styleable.CircleProgressButton_progressBackground, Color.GRAY)
        progressColor = attributes.getColor(R.styleable.CircleProgressButton_progressColor, Color.RED)
        colorlist = attributes.getColorStateList(R.styleable.CircleProgressButton_circleColorList)
        duration = attributes.getInt(R.styleable.CircleProgressButton_duration, 0)
        attributes.recycle()
        mPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    }

    constructor(ctx: Context, attrs: AttributeSet?) : this(ctx, attrs, 0)
    constructor(ctx: Context) : this(ctx, null)

    fun setDuration(duration: Int) {
        this.duration = duration
    }

    override fun setAlpha(alpha: Float) {
        circleAlpha = alpha
    }

    override fun getAlpha(): Float {
        return circleAlpha
    }
    fun setOnAnimatedEnd() {}
    fun stopAnimation(){
        if(animator != null){
            animator!!.end()
            animator = null
        }
    }
    override fun setOnClickListener(l: OnClickListener?) {
        super.setOnClickListener(object : OnCircleButtonClickListener(l) {
            override fun onButtonClick(v: View?) {
            }
        })
    }

    fun setAnimationListener(listener: Animator.AnimatorListener) {
        animatorListener = listener
    }
    fun startProgressAnimation() {
        if (animator != null) {
            animator!!.cancel()
        }
        animator = ValueAnimator.ofInt(0, duration)
        animator!!.duration = duration.toLong()
        animator!!.addUpdateListener {
            animation ->
            animatedValue = animation.animatedValue as Int
            invalidate()
        }
        if (animatorListener != null) {
            animator!!.addListener(animatorListener)
        }
        animator!!.start()
    }

    fun cancelAnimation(){
        if (animator != null && animator!!.isRunning) {
            animator!!.cancel()
            animator = null
        }
    }
    fun resetProgress(){
        animatedValue = 0
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        cancelAnimation()
    }
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        setMeasuredDimension(widthMeasureSpec, heightMeasureSpec)
        circleCenter = PointF(measuredWidth / 2.toFloat(), measuredHeight / 2.toFloat())
        outterRadius = if (measuredHeight > measuredWidth) {
            measuredWidth / 2 - outterStrokeWidth / 2
        } else {
            measuredHeight / 2 - outterStrokeWidth / 2
        }
        innerRadius = outterRadius - outterStrokeWidth / 2 - outterInnerSpace
        outRect = RectF(circleCenter.x - outterRadius,
                circleCenter.y - outterRadius,
                circleCenter.x + outterRadius,
                circleCenter.y + outterRadius)
    }


    override fun onDraw(canvas: Canvas?) {
        if (canvas == null) {
            return
        }
        mPaint.style = Paint.Style.STROKE
        mPaint.color = progressBackgroundColor
        mPaint.strokeWidth = outterStrokeWidth
        canvas.drawCircle(circleCenter.x, circleCenter.y, outterRadius, mPaint)
        drawProgress(canvas)
        mPaint.color = colorlist.getColorForState(drawableState, colorlist.defaultColor)
        mPaint.style = Paint.Style.FILL
        mPaint.strokeWidth = 0f
        mPaint.alpha = (circleAlpha * 255).toInt()
        canvas.drawCircle(circleCenter.x, circleCenter.y, innerRadius, mPaint)
        if (!TextUtils.isEmpty(text)) {
            val textLength = paint.measureText(text.toString())
            paint.color = textColors.getColorForState(drawableState,textColors.defaultColor)
            paint.textSize = textSize
            val rect = Rect()
            paint.getTextBounds(text.toString(),0,text.length,rect)

            val y = circleCenter.y - rect.top/2
            canvas.drawText(text,0,text.length,circleCenter.x - textLength / 2, y,paint)
        }
    }


    private fun drawProgress(canvas: Canvas) {
        if (duration == 0) {
            return
        }
        mPaint.color = progressColor
        val sweepAngle = animatedValue / duration.toFloat() * 360
        canvas.drawArc(outRect, startAngle, sweepAngle, false, mPaint)
    }
}

abstract class OnCircleButtonClickListener : View.OnClickListener {
    private var listener: View.OnClickListener? = null

    constructor(l: View.OnClickListener?) {
        listener = l
    }

    override fun onClick(v: View?) {
        onButtonClick(v)
        listener?.onClick(v)
    }

    abstract fun onButtonClick(v: View?)
}

