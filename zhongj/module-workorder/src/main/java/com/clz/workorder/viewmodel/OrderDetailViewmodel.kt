package com.clz.workorder.viewmodel

import androidx.databinding.ObservableField
import com.clz.workorder.BR
import com.czl.base.base.BaseViewModel
import com.czl.base.base.MyApplication
import com.czl.base.binding.command.BindingAction
import com.czl.base.binding.command.BindingCommand
import com.czl.base.binding.command.BindingConsumer
import com.czl.base.data.DataRepository
import com.czl.base.event.SingleLiveEvent

class OrderDetailViewmodel(application: MyApplication, model: DataRepository) :
BaseViewModel<DataRepository>(application, model) {
    var reportType=ObservableField("")
    var reportLocation=ObservableField("")
    var reportContent=ObservableField("")
    var reportContentNum=ObservableField("")
    var reportTypeData:SingleLiveEvent<ArrayList<String>> = SingleLiveEvent()
    var onClickType:BindingCommand<Void> = BindingCommand(BindingAction {
        getreportTypeData()
    })
    var reportLocationData:SingleLiveEvent<ArrayList<String>> = SingleLiveEvent()
    var onClickLocation:BindingCommand<Void> = BindingCommand(BindingAction {
        getreportLocationData()
    })
    var onClickSubmit:BindingCommand<Void> = BindingCommand(BindingAction {
        submitReport()
    })

    private fun submitReport() {
        //提交至后台
    }

    private fun getreportLocationData() {
        //请求接口获取报事位置列表数据
        var locations=ArrayList<String>()
        locations.add("a栋")
        reportLocationData.postValue(locations)
    }

    private fun getreportTypeData() {
        //请求接口获取报事类型列表数据
        var types=ArrayList<String>()
        types.add("空调维修")
        reportTypeData.postValue(types)

    }

    public fun reportContentLength(content:String): String {
        return content.length.toString()+"/500"
    }
}