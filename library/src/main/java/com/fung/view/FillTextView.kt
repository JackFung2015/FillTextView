package com.fung.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.util.AttributeSet
import android.view.View

/***
 * 宽度填充TextView（支持全中文，全英文，中英混合等全部情况）
 */
class FillTextView(context: Context, attrs: AttributeSet?) : View(context,attrs) {
    /***
     * 需要绘制的文字
     */
    var mText: String = "FillTextView"

    /***
     * 文本颜色
     */
    private var mTextColor: Int = Color.BLACK

    /***
     * 文本大小
     */
    private var mTextSize: Float = 12f

    /***
     * 文字矩形区域水平方向间距
     */
    private var mHorizontal = 20

    /***
     * 绘制时控制文本绘制的范围
     */
    private var mBound: Rect? = null
    var mPaint: Paint? = null

    /***
     * 每行的文本
     */
    private var mTextList: MutableList<String>? = null


    init {
        mTextList = mutableListOf()
        //获取自定义属性的值
        val a = context.theme.obtainStyledAttributes(attrs, R.styleable.FillTextView, 0, 0)
        mText = a.getString(R.styleable.FillTextView_text).toString()
        mTextColor = a.getColor(R.styleable.FillTextView_textColor, Color.BLACK)
        mTextSize = a.getDimension(R.styleable.FillTextView_textSize, 12f)
        a.recycle()

        mPaint = Paint()
        mPaint?.run {
            textSize = mTextSize
            color = mTextColor
            isAntiAlias = true //设置抗锯齿
        }
        //获得绘制文本的宽和高
        mBound = Rect()
        mPaint?.getTextBounds(mText, 0, mText.length, mBound)
    }

    /***
     * 是否只有一行
     */
    var isOneLine: Boolean = true

    /***
     * 行数
     */
    var spLineNum: Int = 0

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        var widthMode = MeasureSpec.getMode(widthMeasureSpec)
        var heightMode = MeasureSpec.getMode(heightMeasureSpec)
        var widthSize = MeasureSpec.getSize(widthMeasureSpec)
        var heightSize = MeasureSpec.getSize(heightMeasureSpec)
        val textWidth = mBound?.width() //文本宽度
        if (mTextList?.size == 0) {
            //文本分段
            val padding = paddingLeft + paddingRight + mHorizontal
            var specWidth = widthSize - padding //一行能够显示文本的最大宽度
            textWidth?.run {
                if (this < specWidth) {
                    //一行足以显示
                    spLineNum = 1
                    mTextList?.add(mText)
                } else {
                    //行数大于1
                    isOneLine = false
                    spLineNum = kotlin.math.ceil((this * 1.0 / specWidth)).toInt() //获取行数
                    var length = 0
                    mPaint?.run {
                        for(i in 0 until spLineNum){//遍历行数，获取每次截取的位置index
                            val index = this.breakText(mText.toCharArray(),length,mText.length-length, specWidth.toFloat(),null)
                            mTextList?.add(mText.substring(length,index+length))
                            length+=index
                        }
                    }
                }
            }
        }
        var width = if (widthMode == MeasureSpec.EXACTLY) {
            widthSize
        } else {
            if (isOneLine) {
                paddingLeft + textWidth!! + paddingRight + mHorizontal //单行宽度
            } else {
                widthSize //多行宽度
            }
        }
        var height = if (heightMode == MeasureSpec.EXACTLY) {
            heightSize
        } else {
            val textHeight = getLineHeight()
            if (isOneLine) {
                (paddingTop + textHeight + paddingBottom).toInt() //单行高度
            } else {
                (paddingTop + textHeight * spLineNum + paddingBottom).toInt() //多行高度
            }
        }
        setMeasuredDimension(width, height)
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        mTextList?.let {
            mPaint?.run {
                for (i in it.indices) {
                    //https://blog.csdn.net/mq2856992713/article/details/52327938
                    canvas?.drawText(it[i], (mHorizontal / 2+paddingLeft).toFloat(), (-(getBaseLineLocation()) + getLineHeight() * i + paddingTop), this)
                }
            }
        }
    }

    /***
     * 获取baseLine距离控件坐标系y值
     */
    private fun getBaseLineLocation(): Float {
        val fontMetrics = mPaint?.fontMetrics
        fontMetrics?.run {
            //getLineHeight()/2+getLineHeight()/2 - fontMetrics.bottom
            return this.top
        }
        return 0f
    }

    /***
     * 获取单行高度
     */
    private fun getLineHeight(): Float {
        val fontMetrics = mPaint?.fontMetrics
        fontMetrics?.run {
            return (this.bottom - this.top)
        }
        return 0f
    }
}
