package com.android.work.demoset.dynamic_change_skin

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import com.android.work.apt_annotation.BindView
import com.android.work.demoset.R
import com.android.work.network.ViewModelUtil.launch

class ChangeSkinActivity: AppCompatActivity() {

    @BindView(R.id.skin_ll)
    lateinit var skinLl:LinearLayout

    @BindView(R.id.skin_image)
    lateinit var skinImage:ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_change_skin_layout)
        `ChangeSkinActivity$$Util`().findViewById(this)

//        val drawable = ChangeSkinUtil.getDrawableSource("img")
        skinLl.background = ChangeSkinUtil.getDrawableSource(ChangeSkinUtil.getResourceId("img","drawable"))

    }

    fun changeSkin(view: View) {

//        val drawable = ChangeSkinUtil.getDrawableSource("launch")
//        skinImage.setImageDrawable(drawable)
        skinImage.background = ChangeSkinUtil.getDrawableSource(ChangeSkinUtil.getResourceId("launch","drawable"))
    }
}