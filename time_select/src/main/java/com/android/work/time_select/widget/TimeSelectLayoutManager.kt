package com.android.work.time_select.widget

import android.util.Log
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class TimeSelectLayoutManager: RecyclerView.LayoutManager() {

    private val tag = "TimeSelectLayoutManager"

    /**
     * 一个item从可见到完全不可见所滑动的距离
     */
    private var onCompleteScrollDistance = 0

    /**
     * 全局滑动过的距离
     */
    private var verticalDistance = 0

    /**
     * 第一个item从可见到完全不可见的滑动距离
     */
    private var firstChildOnCompleteScrollDistance = 0

    /**
     * RecyclerView布局高度
     */
    private var recyclerViewHeight = 0

    /**
     * item高度
     */
    private var itemHeight = 0

    /**
     * 当前可见的item的位置
     */
    private var curVisibleItemPosition = 0

    override fun generateDefaultLayoutParams(): RecyclerView.LayoutParams {
        return RecyclerView.LayoutParams(RecyclerView.LayoutParams.WRAP_CONTENT,RecyclerView.LayoutParams.WRAP_CONTENT)
    }

    override fun canScrollHorizontally(): Boolean {
        return false
    }

    override fun canScrollVertically(): Boolean {
        return true
    }

    override fun scrollVerticallyBy(
        dy: Int,
        recycler: RecyclerView.Recycler?,
        state: RecyclerView.State?
    ): Int {
        Log.d(tag,"scrollVerticallyBy dy:$dy  verticalDistance:$verticalDistance")
        verticalDistance += dy
        return dy
    }

    /**
     * 在第一次初始化/adapter重置的时候会调用
     */
    override fun onLayoutChildren(recycler: RecyclerView.Recycler?, state: RecyclerView.State?) {
        if(recycler != null) {
            if (state?.itemCount == 0) {
                removeAndRecycleAllViews(recycler)
                return
            }
            val firstView = recycler.getViewForPosition(0)
            val parent = firstView.parent
            if(parent is RecyclerView) {
                recyclerViewHeight = parent.measuredHeight
                itemHeight = firstView.measuredHeight
                firstChildOnCompleteScrollDistance = recyclerViewHeight/2 + itemHeight/2
                Log.d(tag,"onLayoutChildren recyclerViewHeight:$recyclerViewHeight   itemHeight:$itemHeight  " +
                        "firstChildOnCompleteScrollDistance:$firstChildOnCompleteScrollDistance")
            }
            onCompleteScrollDistance = -1
            detachAndScrapAttachedViews(recycler)

            onLayout(recycler,0f,0f)
        }
    }

    private fun onLayout(recycler: RecyclerView.Recycler, dx: Float, dy: Float) {
        // 当滑动的距离超过item从完全可见到完全不可见的距离，表示已经滑倒第n个条目(非第一个)
        if(verticalDistance >= onCompleteScrollDistance){
            curVisibleItemPosition = (verticalDistance / onCompleteScrollDistance)
        }else{
            curVisibleItemPosition = 0
        }
    }

    override fun layoutDecorated(child: View, left: Int, top: Int, right: Int, bottom: Int) {
        super.layoutDecorated(child, left, top, right, bottom)
    }


}