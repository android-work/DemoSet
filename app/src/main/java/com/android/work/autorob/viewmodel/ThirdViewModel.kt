package com.android.work.autorob.viewmodel

import android.app.Application
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.graphics.drawable.Drawable
import androidx.databinding.BindingAdapter
import androidx.databinding.ObservableField
import androidx.recyclerview.widget.RecyclerView
import com.android.work.autorob.AppListAdapter
import com.android.work.mvvm.viewmodel.base.BaseViewModel

class ThirdViewModel(application: Application) : BaseViewModel(application) {

    val apps by lazy { ObservableField<MutableList<AppInfo>?>() }

    fun requestApps() {
        launch({
            getApplication<Application>().packageManager?.let {pm ->
                val list = pm.getInstalledApplications(
                    PackageManager.GET_META_DATA
                )

                val appList = mutableListOf<AppInfo>()
                list.forEach {
                    try {
                        if (it.packageName != null && it.packageName.isNotEmpty() && (it.flags and ApplicationInfo.FLAG_SYSTEM) == 0) {
                            appList.add(
                                AppInfo(
                                    logo = it.loadIcon(pm),
                                    name = it.loadLabel(pm).toString(),
                                    packName = it.packageName
                                )
                            )
                        }
                    }catch (e:Exception){
                        e.printStackTrace()
                    }
                }
                apps.set(appList)
            }
        })
    }

}

data class AppInfo(val logo: Drawable?, val name: String?, val packName: String?)

@BindingAdapter("setData")
fun setAppsData(recyclerView: RecyclerView,list:MutableList<AppInfo>?){
    (recyclerView.adapter as AppListAdapter).updateData(list)
}