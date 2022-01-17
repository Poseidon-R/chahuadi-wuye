package com.czl.base.binding.viewadapter.viewgroup

import androidx.databinding.ViewDataBinding

interface IBindingItemViewModel<V : ViewDataBinding> {
    fun injectDataBinding(binding: V)
}