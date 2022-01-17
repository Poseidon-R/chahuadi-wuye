package com.czl.module_web.viewmodel

import com.czl.base.base.BaseViewModel
import com.czl.base.base.MyApplication
import com.czl.base.data.DataRepository

class WebViewModel(application: MyApplication, model: DataRepository) :
    BaseViewModel<DataRepository>(application, model)  {
}