package com.czl.base.callback

import com.czl.base.R
import com.kingja.loadsir.callback.Callback;

/**
 * @author Alwyn
 * @Date 2020/11/25
 * @Description
 */
class LoadingCallback : Callback() {
    override fun onCreateView(): Int {
        return R.layout.common_loading_layout
    }
}