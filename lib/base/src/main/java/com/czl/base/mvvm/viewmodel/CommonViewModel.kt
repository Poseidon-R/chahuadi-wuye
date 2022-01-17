package com.czl.base.mvvm.viewmodel

import com.czl.base.base.BaseViewModel
import com.czl.base.data.DataRepository
import com.czl.base.base.MyApplication

/**
 * @author Alwyn
 * @Date 2020/10/19
 * @Description
 */
class CommonViewModel(application: MyApplication, model: DataRepository) :
    BaseViewModel<DataRepository>(application,model) {
}