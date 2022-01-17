package com.czl.module_work.fragment

import com.alibaba.android.arouter.facade.annotation.Route
import com.czl.base.base.BaseFragment
import com.czl.base.config.AppConstants
import com.czl.module_work.BR
import com.czl.module_work.R
import com.czl.module_work.adapter.MyDispatchDeviceAdapter
import com.czl.module_work.adapter.OrderDetailPhotoAdapter
import com.czl.module_work.adapter.UploadCertificateAdapter
import com.czl.module_work.databinding.WorkFragmentDeviceTakeCareBinding
import com.czl.module_work.databinding.WorkFragmentRecordCertificateBinding
import com.czl.module_work.databinding.WorkFragmentUploadCertificateBinding
import com.czl.module_work.viewModel.DeviceTakeCareViewModel
import com.czl.module_work.viewModel.MyOrderDetailViewModel
import com.czl.module_work.viewModel.UploadCertificateViewModel


/**
 * 创建日期：2021/12/28  17:39
 * 类说明:
 * @author：86152
 */
@Route(path = AppConstants.Router.Work.F_WORK_RECORD_CERTIFICATE)
class RecordCertificateFragment :
    BaseFragment<WorkFragmentRecordCertificateBinding, UploadCertificateViewModel>() {

    private lateinit var mAdapter: UploadCertificateAdapter

    private var img = ""

    override fun useBaseLayout(): Boolean = false;

    override fun initContentView(): Int = R.layout.work_fragment_record_certificate

    override fun initVariableId(): Int = BR.viewModel

    override fun initData() {
        viewModel.tvTitle.set("上传凭证")
        initAdapter()
    }

    override fun initParam() {
        img = arguments?.getString(AppConstants.BundleKey.WORK_RECORD_IMG) ?: ""
    }


    private fun initAdapter() {
        mAdapter = UploadCertificateAdapter()

        binding.ryCommon.apply {
            adapter = mAdapter
        }
        mAdapter.addData(img)

    }
}