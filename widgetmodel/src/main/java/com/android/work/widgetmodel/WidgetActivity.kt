package com.android.work.widgetmodel

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.android.work.widgetmodel.widget.GongGeWidget
import kotlin.random.Random

class WidgetActivity : AppCompatActivity() {
    lateinit var gongGe: GongGeWidget

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_widget_layout)

        gongGe = findViewById<GongGeWidget>(R.id.gongGe)
    }

    // 切换排版
    fun cutOrientation() {
        if (gongGe.orientation == GongGeWidget.Companion.OrientationType.horizontal)
            gongGe.putOrientation(GongGeWidget.Companion.OrientationType.vertical)
        else
            gongGe.putOrientation(GongGeWidget.Companion.OrientationType.horizontal)
    }

    // 添加行
    fun addRow() {
        gongGe.putRowCount( ++ gongGe.rowCount)
    }

    // 添加列
    fun addColumn() {
        gongGe.putColumnCount( ++ gongGe.columnCount)
    }

    // 切换方向
    fun cutDirection() {
        when (gongGe.direction) {
            GongGeWidget.Companion.DirectionType.start -> {
                gongGe.putDirection(GongGeWidget.Companion.DirectionType.between)
            }
            GongGeWidget.Companion.DirectionType.between -> {
                gongGe.putDirection(GongGeWidget.Companion.DirectionType.end)
            }
            else -> {
                gongGe.putDirection(GongGeWidget.Companion.DirectionType.start)
            }
        }
    }

    // 增大横向间距
    fun addHorizonSpace() {
        gongGe.putHorizontalSpace(gongGe.horizontalSpace + 10)
    }

    // 减小横向间距
    fun reduceHorizonSpace() {
        gongGe.putHorizontalSpace(gongGe.horizontalSpace - 10)
    }

    // 增加纵向间距
    fun addVerticalSpace() {
        gongGe.putVerticalSpace(gongGe.verticalSpace + 10)
    }

    // 减小纵向间距
    fun reduceVerticalSpace() {
        gongGe.putVerticalSpace((gongGe.verticalSpace - 10))
    }

    // 添加view
    fun addView() {
        val textView = TextView(this)
        val width = Random.nextInt(50,400)
        val height = Random.nextInt(30,200)
        val params = ViewGroup.LayoutParams(width,height)
        textView.layoutParams = params
        textView.text = "width:$width,height:$height"
        textView.textSize = 10f
        val red = Random.nextInt(255)
        val green = Random.nextInt(255)
        val blue = Random.nextInt(255)
        Log.d("WidgetActivity","width:$width,height:$height,red:$red,green:$green,blue:$blue")
        textView.setBackgroundColor(Color.argb(255,red,green,blue))
        gongGe.putChildView(textView)
    }

    fun clean() {
        gongGe.clean()
    }

    fun cutControl(view: View) {
        CutControlDialog.Companion.Build().setAddColumnListener(object :DialogListener{
            override fun listener() {
                addColumn()
            }
        }).setAddRowListener(object :DialogListener{
            override fun listener() {
                addRow()
            }
        }).setAddViewListener(object :DialogListener{
            override fun listener() {
                addView()
            }
        }).setCutDirectionListener(object :DialogListener{
            override fun listener() {
                cutDirection()
            }
        }).setCutOrientationListener(object :DialogListener{
            override fun listener() {
                cutOrientation()
            }
        }).setAddHorizonSpaceListener(object :DialogListener{
            override fun listener() {
                addHorizonSpace()
            }
        }).setReduceHorizonSpaceListener(object :DialogListener{
            override fun listener() {
                reduceHorizonSpace()
            }
        }).setAddVerticalSpaceListener(object :DialogListener{
            override fun listener() {
                addVerticalSpace()
            }
        }).setReduceVerticalSpaceListener(object :DialogListener{
            override fun listener() {
                reduceVerticalSpace()
            }
        }).setCleanListener(object :DialogListener{
            override fun listener() {
                clean()
            }
        }).builder(this).show()
    }
}

class CutControlDialog(context: Context,private val build:Build) : Dialog(context){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dialog_cut_control_layout)
        findViewById<TextView>(R.id.cutOrientation).setOnClickListener {
            // 切换排版
            build.getCutOrientationListener().listener()
            dismiss()
        }
        findViewById<TextView>(R.id.addRow).setOnClickListener {
            // 添加行
            build.getAddRowListener().listener()
            dismiss()
        }
        findViewById<TextView>(R.id.addColumn).setOnClickListener {
            // 添加列
            build.getAddColumnListenerr().listener()
            dismiss()
        }
        findViewById<TextView>(R.id.cutDirection).setOnClickListener {
            // 切换方向
            build.getCutDirectionListener().listener()
            dismiss()
        }
        findViewById<TextView>(R.id.addHorizonSpace).setOnClickListener {
            // 增大横向间距
            build.getAddHorizonSpaceListener().listener()
            dismiss()
        }
        findViewById<TextView>(R.id.reduceHorizonSpace).setOnClickListener {
            // 减小横向间距
            build.getReduceHorizonSpaceListener().listener()
            dismiss()
        }
        findViewById<TextView>(R.id.addVerticalSpace).setOnClickListener {
            // 增加纵向间距
            build.getAddVerticalSpaceListener().listener()
            dismiss()
        }
        findViewById<TextView>(R.id.reduceVerticalSpace).setOnClickListener {
            // 减小纵向间距
            build.getReduceVerticalSpaceListener().listener()
            dismiss()
        }
        findViewById<TextView>(R.id.addView).setOnClickListener {
            // 添加view
            build.getAddViewListener().listener()
            dismiss()
        }
        findViewById<TextView>(R.id.clean).setOnClickListener {
            build.getCleanListener().listener()
            dismiss()
        }
    }

    companion object{
        class Build{
            private lateinit var cleanListener:DialogListener
            private lateinit var addViewListener:DialogListener
            private lateinit var reduceVerticalSpaceListener:DialogListener
            private lateinit var addVerticalSpaceListener:DialogListener
            private lateinit var reduceHorizonSpaceListener:DialogListener
            private lateinit var addHorizonSpaceListener:DialogListener
            private lateinit var cutDirectionListener:DialogListener
            private lateinit var addColumnListener:DialogListener
            private lateinit var addRowListener:DialogListener
            private lateinit var cutOrientationListener:DialogListener

            fun getCleanListener():DialogListener = cleanListener

            fun setCleanListener(listener: DialogListener):Build{
                this.cleanListener = listener
                return this
            }

            fun getAddViewListener():DialogListener = addViewListener

            fun setAddViewListener(listener: DialogListener):Build{
                this.addViewListener = listener
                return this
            }

            fun getReduceVerticalSpaceListener():DialogListener = reduceVerticalSpaceListener

            fun setReduceVerticalSpaceListener(listener: DialogListener):Build{
                this.reduceVerticalSpaceListener = listener
                return this
            }

            fun getAddVerticalSpaceListener():DialogListener = addVerticalSpaceListener

            fun setAddVerticalSpaceListener(listener: DialogListener):Build{
                this.addVerticalSpaceListener = listener
                return this
            }

            fun getReduceHorizonSpaceListener():DialogListener = reduceHorizonSpaceListener

            fun setReduceHorizonSpaceListener(listener: DialogListener):Build{
                this.reduceHorizonSpaceListener = listener
                return this
            }

            fun getAddHorizonSpaceListener():DialogListener = addHorizonSpaceListener

            fun setAddHorizonSpaceListener(listener: DialogListener):Build{
                this.addHorizonSpaceListener = listener
                return this
            }

            fun getCutDirectionListener():DialogListener = cutDirectionListener

            fun setCutDirectionListener(listener: DialogListener):Build{
                this.cutDirectionListener = listener
                return this
            }

            fun getAddColumnListenerr():DialogListener = addColumnListener

            fun setAddColumnListener(listener: DialogListener):Build{
                this.addColumnListener = listener
                return this
            }

            fun getAddRowListener():DialogListener = addRowListener

            fun setAddRowListener(listener: DialogListener):Build{
                this.addRowListener = listener
                return this
            }

            fun getCutOrientationListener():DialogListener = cutOrientationListener

            fun setCutOrientationListener(listener: DialogListener):Build{
                this.cutOrientationListener = listener
                return this
            }

            fun builder(context: Context):CutControlDialog{
                return CutControlDialog(context,this)
            }
        }
    }
}

interface DialogListener {
    fun listener()
}