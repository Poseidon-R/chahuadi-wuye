package com.czl.base.adapter

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseDataBindingHolder
import com.czl.base.R
import com.czl.base.databinding.ItemEasyBinding

class EasyAdapter: BaseQuickAdapter<String, BaseDataBindingHolder<ItemEasyBinding>>(R.layout.item_easy) {
    override fun convert(holder: BaseDataBindingHolder<ItemEasyBinding>, item: String) {
        holder.dataBinding?.apply {
            data = item
            adapter = this@EasyAdapter
            executePendingBindings()
        }
    }
}