package com.android.work.widgetmodel.widget

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.marginBottom
import androidx.core.view.marginLeft
import androidx.core.view.marginRight
import androidx.core.view.marginTop
import com.android.work.widgetmodel.R
import com.android.work.widgetmodel.widget.GongGeWidget.Companion.DirectionType.Companion.between
import com.android.work.widgetmodel.widget.GongGeWidget.Companion.DirectionType.Companion.start
import com.android.work.widgetmodel.widget.GongGeWidget.Companion.OrientationType.Companion.horizontal
import kotlin.math.max

class GongGeWidget : ViewGroup {
    private val TAG = "GongGeWidget"

    companion object {
        /**
         * 子控件摆放方式枚举
         */
        class OrientationType {
            companion object {
                /**
                 * 横向排版
                 */
                const val horizontal = 0

                /**
                 * 垂直排版
                 */
                const val vertical = 1
            }
        }

        /**
         * 方向枚举
         */
        class DirectionType {
            companion object {
                /**
                 * 起始位对齐
                 */
                const val start = 0

                /**
                 * 结束位对齐
                 */
                const val end = 1

                /**
                 * 均分
                 */
                const val between = 2
            }
        }

    }

    /**
     * 设置子view集合
     */
    var widgets: MutableList<View>? = null

    /**
     * 子view
     */
    var childView:View? = null

    fun putChildView(value:View){
        childView = value
        this@GongGeWidget.addView(value)
    }

    /**
     * 排版方式
     */
    var orientation: Int = horizontal

    fun putOrientation(value:Int) {
        orientation = value
        requestLayout()
    }

    /**
     * 列数
     */
    var columnCount: Int = 3

    fun putColumnCount(value:Int) {
        Log.d(TAG,"columnCount:$value")
        columnCount = value
        requestLayout()
    }

    /**
     * 行数
     */
    var rowCount: Int = 3

    fun putRowCount(value:Int) {
        rowCount = value
        requestLayout()
    }

    /**
     * 横向间隔
     */
    var horizontalSpace: Int = 0

    fun putHorizontalSpace(value:Int){
        if (value < 0) {
            Toast.makeText(context, "请设置间距大于0", Toast.LENGTH_SHORT).show()
            return
        }else if(value > width){
            Toast.makeText(context, "设置的间距超过控件宽度", Toast.LENGTH_SHORT).show()
            return
        }
        horizontalSpace = value
        requestLayout()
    }

    /**
     * 纵向间隔
     */
    var verticalSpace: Int = 0

    fun putVerticalSpace(value:Int){
        if (value < 0) {
            Toast.makeText(context, "请设置间距大于0", Toast.LENGTH_SHORT).show()
            return
        }else if(value > height){
            Toast.makeText(context, "设置的间距超过控件高度", Toast.LENGTH_SHORT).show()
            return
        }
        verticalSpace = value
        requestLayout()
    }

    /**
     * 方向
     */
    var direction: Int = start

    fun putDirection(value:Int){
        direction = value
        requestLayout()
    }

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyle: Int) : super(
        context,
        attrs,
        defStyle
    ) {
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.GongGeWidget)
        columnCount = typedArray.getInt(R.styleable.GongGeWidget_column, 3)
        rowCount = typedArray.getInt(R.styleable.GongGeWidget_row, 3)
        verticalSpace = typedArray.getDimensionPixelSize(R.styleable.GongGeWidget_verticalSpace, 0)
        horizontalSpace =
            typedArray.getDimensionPixelSize(R.styleable.GongGeWidget_horizontalSpace, 0)
        orientation = typedArray.getInt(R.styleable.GongGeWidget_orientation, horizontal)
        direction = typedArray.getInt(R.styleable.GongGeWidget_direction, start)
        typedArray.recycle()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        var totalWidth = 0
        var totalHeight = 0
        var tempWidth = 0
        var tempHeight = 0
        // 获取控件设置的尺寸
        var width = getDefaultSize(0, widthMeasureSpec)
        var height = getDefaultSize(0, heightMeasureSpec)
        Log.d(TAG, "width:$width,height:$height")
        // 如果是横向，判断宽度是否有值，无值使用屏幕高度
        if (orientation == horizontal && width == 0) {
            width = context.resources.displayMetrics.widthPixels - marginLeft - marginRight
        }
        // 如果是纵向，判断高度是否有值，无值使用屏幕高度
        if (orientation == OrientationType.vertical && height == 0) {
            height = context.resources.displayMetrics.heightPixels - marginTop - marginBottom
        }
        // 计算所有子view宽高
        measureChildren(widthMeasureSpec, heightMeasureSpec)
        // 获取子view个数
        val count = widgets?.size ?: childCount
        Log.d(TAG, "childCount:$count")
        when (direction) {
            between -> {
                for (i in 0 until count) {
                    val view = widgets?.get(i) ?: getChildAt(i)
                    if (orientation == horizontal) {
                        val childHeight = view?.measuredHeight ?: 0
                        tempHeight = (childHeight + verticalSpace)
                        if (i % columnCount == 0) {
                            // 换行，累计高度
                            totalHeight += tempHeight
                        } else {
                            totalHeight = max(tempHeight, totalHeight)
                        }
                    } else {
                        val childWidth = view?.measuredWidth ?: 0
                        tempWidth = (childWidth + horizontalSpace)
                        if (i % rowCount == 0) {
                            // 换列，累计宽度
                            totalWidth += tempWidth
                        } else {
                            totalWidth = max(totalWidth, tempWidth)
                        }
                    }
                }
                if (orientation == horizontal) {
                    totalWidth = width
                    totalHeight += verticalSpace
                } else {
                    totalHeight = height
                    totalWidth += horizontalSpace
                }
            }
            else -> {
                for (i in 0 until count) {
                    // 获取子view，并计算宽高
                    val view = widgets?.get(i) ?: getChildAt(i)
                    val childWidth = view?.measuredWidth ?: 0
                    val childHeight = view?.measuredHeight ?: 0
                    Log.d(TAG, "childWidth:$childWidth  childHeight:$childHeight")
                    if (orientation == horizontal) {
                        tempHeight = (childHeight + verticalSpace)
                        // 计算宽度
                        val releaseWidth = width - totalWidth - childWidth - horizontalSpace
                        // 表示剩余位置包括右边距
                        if (releaseWidth - horizontalSpace >= 0) {
                            totalWidth += (childWidth + horizontalSpace)
                        } else {
                            // 当超宽度时，将当前当总宽度赋值给tempWidth，用于比较取最大宽度
                            tempWidth = max(totalWidth, tempWidth)
                            // totalWidth 赋值控件宽度 + 左间距，表示换行
                            totalWidth = childWidth + horizontalSpace
                            // 计算高度
                            totalHeight += (childHeight + verticalSpace)
                        }
                        totalHeight = max(totalHeight, tempHeight)
                    } else {
                        tempWidth = (childWidth + horizontalSpace)
                        // 计算宽度
                        val releaseHeight = height - totalHeight - childHeight - verticalSpace
                        // 剩余高度还有一个垂直间距
                        if (releaseHeight - verticalSpace >= 0) {
                            totalHeight += (childHeight + verticalSpace)
                        } else {
                            // 当超宽度时，将当前当总宽度赋值给tempWidth，用于比较取最大宽度
                            tempHeight = max(totalHeight, tempHeight)
                            // totalWidth 赋值0，表示换行
                            totalHeight = 0
                            // 计算宽度
                            totalWidth += (childWidth + 2 * horizontalSpace)
                        }
                        totalWidth = max(totalWidth, tempWidth)
                    }
                }
                totalWidth = (max(totalWidth, tempWidth) + horizontalSpace)
                totalHeight = (max(totalHeight, tempHeight) + verticalSpace)
            }
        }

        Log.d(TAG, "totalWidth:$totalWidth,totalHeight:$totalHeight")
        // 设置控件宽高
        setMeasuredDimension(totalWidth, totalHeight)
    }


    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        val count = widgets?.size ?: childCount
        when (direction) {
            between -> linearLayout(count)
            else -> warpLayout(count)
        }
    }

    /**
     * 线性布局
     */
    private fun linearLayout(count: Int) {
        var marginBottom = 0
        var marginRight = 0
        var marginLeft = horizontalSpace
        var marginTop = verticalSpace
        var tempMarginTop = verticalSpace
        var tempMarginLeft = horizontalSpace
        var tempMarginBottom = 0
        var tempMarginRight = 0
        for (i in 0 until count) {
            val view = widgets?.get(i) ?: getChildAt(i)
            // 如果是横向布局
            if (orientation == horizontal) {
                // 横向排版，计算子view宽度
                val childWidth = (width - ((columnCount + 1) * horizontalSpace)) / columnCount
                val childHeight = view?.measuredHeight ?: 0
                if (i % columnCount != 0) {
                    marginLeft = marginRight + horizontalSpace
                    marginTop = tempMarginTop
                } else {
                    // 换行
                    marginLeft = horizontalSpace
                    marginTop = tempMarginBottom + verticalSpace
                    tempMarginTop = marginTop
                }
                marginBottom = marginTop + childHeight
                marginRight = childWidth + marginLeft
                tempMarginBottom = max(tempMarginBottom,marginBottom)

            } else {
                // 纵向排版，计算子view高度
                val childHeight = (height - ((columnCount + 1) * verticalSpace)) / rowCount
                val childWidth = view?.measuredWidth ?: 0
                if (i % rowCount != 0) {
                    marginTop = marginBottom + verticalSpace
                    marginLeft = tempMarginLeft
                } else {
                    // 换列
                    marginTop = verticalSpace
                    marginLeft = tempMarginRight + horizontalSpace
                    tempMarginLeft = marginLeft
                }
                marginBottom = marginTop + childHeight
                marginRight = marginLeft + childWidth
                tempMarginRight = max(tempMarginRight,marginRight)
            }
            Log.d(
                TAG,
                "left:$marginLeft,top:$marginTop,right:$marginRight,bottom:$marginBottom"
            )
            view?.layout(marginLeft, marginTop, marginRight, marginBottom)
        }
    }

    /**
     * 流式布局
     */
    private fun warpLayout(count: Int) {
        var marginRight = 0
        var marginBottom = 0
        var marginLeft = 0
        var marginTop = 0
        var tempMarginTop = verticalSpace
        var tempMarginLeft = horizontalSpace
        var tempMarginBottom = 0
        var tempMarginRight = 0
        for (i in 0 until count) {
            // 获取子view
            val view = widgets?.get(i) ?: getChildAt(i)
            val childWidth = view?.measuredWidth ?: 0
            val childHeight = view?.measuredHeight ?: 0
            // 横向排版，左对齐
            if (direction == start) {
                if (orientation == horizontal) {
                    if (width - marginRight - horizontalSpace - childWidth >= 0) {
                        // 子view左边距 = 横向间隔 + 上一个view的右边距
                        marginLeft = horizontalSpace + marginRight
                        // 子view右边距 = 当前子view左边距 + 子view宽度
                        marginRight = childWidth + marginLeft
                        // 子view上边距 = 纵向间隔 + 上一个view的下边距
                        marginTop = tempMarginTop
                        // 子view下边距 = 当前子view上边距 + 子view高度
                        marginBottom = marginTop + childHeight
                        tempMarginBottom = max(marginBottom,tempMarginBottom)
                    } else {
                        // 换行处理
                        marginLeft = horizontalSpace
                        marginRight = childWidth + marginLeft
                        marginTop = tempMarginBottom + verticalSpace
                        marginBottom = marginTop + childHeight
                        tempMarginTop = marginTop
                    }
                } else {
                    // 竖向排版，上对齐
                    if (height - marginBottom - verticalSpace - childHeight >= 0) {
                        // 子view上边距 = 竖向间隔 + 上一个view的下边距
                        marginTop = marginBottom + verticalSpace
                        // 子view左边距 = 上一个view右边距 + 横向间距
                        marginLeft = tempMarginLeft
                        // 子view右边距 = 当前子view左边距 + 子view宽度
                        marginRight = childWidth + marginLeft
                        tempMarginRight = max(tempMarginRight,marginRight)
                        // 子view下边距 = 当前子view上边距 + 子view高度
                        marginBottom = marginTop + childHeight
                    } else {
                        // 换列处理
                        marginLeft = tempMarginRight + horizontalSpace
                        marginRight = childWidth + marginLeft
                        marginTop = verticalSpace
                        tempMarginLeft = marginLeft
                        marginBottom = marginTop + childHeight
                    }
                }
            } else {
                // 横向排版，右对齐
                if (orientation == horizontal) {
                    if (marginLeft - horizontalSpace - childWidth >= 0) {
                        if (i == 0) {
                            // 第一个子view右边距 = 控件宽度 - 横向间距
                            marginRight = width - horizontalSpace
                            // 第一个子view左边距 = view的右边距 - 子view宽度
                            marginLeft = marginRight - childWidth
                        } else {
                            // 之后子view右边距 = 上一个view左边距 - 横向间距
                            marginRight = marginLeft - horizontalSpace
                            // 之后子view左边距 = 当前view的右边距 - 子view宽度
                            marginLeft = marginRight - childWidth
                        }
                        // 子view上边距 = 纵向间隔 + 上一个view的下边距
                        marginTop = tempMarginTop
                        // 子view下边距 = 当前子view上边距 + 子view高度
                        marginBottom = marginTop + childHeight
                        tempMarginBottom = max(tempMarginBottom,marginBottom)
                    } else {
                        // 换行处理
                        marginRight = width - horizontalSpace
                        marginLeft = marginRight - childWidth
                        marginTop = tempMarginBottom + verticalSpace
                        marginBottom = marginTop + childHeight
                        tempMarginTop = marginTop
                    }
                } else {
                    // 竖向排版，下对齐
                    if (marginTop - verticalSpace - childHeight >= 0) {
                        marginBottom = if (i == 0) {
                            height - verticalSpace
                        } else {
                            marginTop - verticalSpace
                        }
                        marginTop = marginBottom - childHeight
                        marginLeft = tempMarginLeft
                        marginRight = marginLeft + childWidth
                        tempMarginRight = max(tempMarginRight,marginRight)
                    } else {
                        marginBottom = height - verticalSpace
                        marginTop = marginBottom - childHeight
                        marginLeft = tempMarginRight + horizontalSpace
                        tempMarginLeft = marginLeft
                        marginRight = marginLeft + childWidth
                    }

                }
            }
            view?.layout(marginLeft, marginTop, marginRight, marginBottom)
        }
    }

    /**
     * 清空布局
     */
    fun clean(){
        removeAllViews()
        widgets?.clear()
        direction = start
        orientation = horizontal
        verticalSpace = 0
        horizontalSpace = 0
        rowCount = 3
        columnCount = 3
    }
}