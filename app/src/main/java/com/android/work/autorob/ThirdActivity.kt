package com.android.work.autorob

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.work.apt_annotation.BindView
import com.android.work.autorob.databinding.ActivityThirdBinding
import com.android.work.autorob.viewmodel.AppInfo
import com.android.work.autorob.viewmodel.ThirdViewModel

class ThirdActivity: AppCompatActivity() {


    private val viewModel by lazy { ViewModelProvider(this).get(ThirdViewModel::class.java) }

    @BindView(R.id.rv)
    lateinit var rv:RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = DataBindingUtil.setContentView<ActivityThirdBinding>(this,R.layout.activity_third)
        binding.viewModel = viewModel
        `ThirdActivity$$Util`().findViewById(this)
        val adapter = AppListAdapter(arrayListOf())
        rv.adapter = adapter
        rv.layoutManager = LinearLayoutManager(this,RecyclerView.VERTICAL,false)
        adapter.setOnClick {
            Toast.makeText(this,"已选择了${it.name}",0).show()
            intent.putExtra("packName",it.packName)
            setResult(RESULT_OK,intent)
            finish()
        }
        viewModel.requestApps()
    }
}