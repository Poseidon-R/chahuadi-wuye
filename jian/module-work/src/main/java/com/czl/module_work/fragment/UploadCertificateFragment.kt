package com.czl.module_work.fragment

import android.text.TextUtils
import android.view.View
import com.alibaba.android.arouter.facade.annotation.Route
import com.blankj.utilcode.util.GsonUtils
import com.czl.base.base.BaseFragment
import com.czl.base.config.AppConstants
import com.czl.base.data.bean.TakeCareDispatchBean
import com.czl.base.util.GlideEngine
import com.czl.module_work.BR
import com.czl.module_work.R
import com.czl.module_work.adapter.MyDispatchDeviceAdapter
import com.czl.module_work.adapter.OrderDetailPhotoAdapter
import com.czl.module_work.adapter.UploadCertificateAdapter
import com.czl.module_work.databinding.WorkFragmentDeviceTakeCareBinding
import com.czl.module_work.databinding.WorkFragmentUploadCertificateBinding
import com.czl.module_work.viewModel.DeviceTakeCareViewModel
import com.czl.module_work.viewModel.MyOrderDetailViewModel
import com.czl.module_work.viewModel.UploadCertificateViewModel
import com.google.gson.reflect.TypeToken
import com.luck.picture.lib.PictureSelector
import com.luck.picture.lib.config.PictureConfig
import com.luck.picture.lib.config.PictureMimeType
import com.luck.picture.lib.entity.LocalMedia
import com.luck.picture.lib.listener.OnResultCallbackListener


/**
 * 创建日期：2021/12/28  17:39
 * 类说明:
 * @author：86152
 */
@Route(path = AppConstants.Router.Work.F_WORK_UPLOAD_CERTIFICATE)
class UploadCertificateFragment :
    BaseFragment<WorkFragmentUploadCertificateBinding, UploadCertificateViewModel>() {

    private lateinit var mAdapter: UploadCertificateAdapter

    val ids = arrayListOf<String>()

    var alarmId = ""
    override fun useBaseLayout(): Boolean = false;

    override fun initContentView(): Int = R.layout.work_fragment_upload_certificate

    override fun initVariableId(): Int = BR.viewModel

    override fun initData() {
        viewModel.tvTitle.set("上传凭证")
        viewModel.setIds(ids)
        viewModel.setOrderId(alarmId)
        if (TextUtils.isEmpty(alarmId)) {
            //保养工单
            binding.llStatusContent.visibility = View.VISIBLE
            binding.llStatusTag.visibility = View.VISIBLE
            binding.llRemark.visibility = View.GONE
        } else {
            //报警工单
            binding.llStatusContent.visibility = View.GONE
            binding.llStatusTag.visibility = View.GONE
            binding.llRemark.visibility = View.VISIBLE
        }
        initAdapter()
    }

    override fun initViewObservable() {
        viewModel.uc.addImageEvent.observe(this, {
            mAdapter.setNewInstance(mutableListOf(it))
        })
        viewModel.uC.finishEvent.observe(this, {
            back()
        })
        viewModel.uc.choiceImgEvent.observe(this, {
            PictureSelector.create(this)
                .openGallery(PictureMimeType.ofImage())
                .imageEngine(GlideEngine.createGlideEngine())
                .isCompress(true)
                .selectionMode(PictureConfig.SINGLE)
                .minimumCompressSize(100)
                .forResult(object : OnResultCallbackListener<LocalMedia?> {
                    override fun onResult(result: List<LocalMedia?>) {
                        result[0]?.let { it -> viewModel.uploadImg(it.compressPath) }
                    }

                    override fun onCancel() {
                        // onCancel Callback
                    }
                })
        })
    }

    override fun initParam() {
        val taskIds = arguments?.getString(AppConstants.BundleKey.WORK_ORDER_TASK_IDS) ?: ""
        alarmId = arguments?.getString(AppConstants.BundleKey.WORK_ORDER_ALARM_ID) ?: ""
        if (TextUtils.isEmpty(taskIds)) return
        val type = object : TypeToken<List<String>>() {}.type;
        val idsJson: List<String> = GsonUtils.fromJson(taskIds, type)
        this.ids.addAll(idsJson)
    }

    private fun initAdapter() {
        mAdapter = UploadCertificateAdapter()
        mAdapter.setOnItemClickListener { adapter, view, position ->
            viewModel.uc.choiceImgEvent.call()
        }
        binding.ryCommon.apply {
            adapter = mAdapter
        }
        mAdapter.addData("")
    }
}