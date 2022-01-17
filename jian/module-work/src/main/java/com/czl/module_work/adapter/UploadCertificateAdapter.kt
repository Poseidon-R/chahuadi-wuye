package com.czl.module_work.adapter

import android.text.TextUtils
import com.blankj.utilcode.util.ImageUtils
import com.bumptech.glide.Glide
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseDataBindingHolder
import com.czl.base.data.net.RetrofitClient
import com.czl.module_work.R
import com.czl.module_work.databinding.WorkItemUploadCertificateBinding


/**
 * 创建日期：2022/1/8  12:26
 * 类说明:
 * @author：86152
 */
class UploadCertificateAdapter :
    BaseQuickAdapter<String, BaseDataBindingHolder<WorkItemUploadCertificateBinding>>(R.layout.work_item_upload_certificate) {
    override fun convert(
        holder: BaseDataBindingHolder<WorkItemUploadCertificateBinding>,
        item: String
    ) {
        if (!TextUtils.isEmpty(item)) {
            Glide.with(context).load(item).into(holder.getView(R.id.iv_pic))
        }
    }

}