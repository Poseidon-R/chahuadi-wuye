package com.clz.workorder.fragment

import android.Manifest
import android.annotation.SuppressLint
import android.view.View
import android.widget.PopupWindow
import androidx.recyclerview.widget.GridLayoutManager
import com.alibaba.android.arouter.facade.annotation.Route
import com.clz.workorder.adapter.GridImageAdapter
import com.clz.workorder.view.FullyGridLayoutManager
import com.clz.workorder.viewmodel.OrderDetailViewmodel
import com.czl.base.base.BaseFragment
import com.czl.base.config.AppConstants
import com.czl.base.util.DialogHelper
import com.czl.base.util.GlideEngine
import com.luck.picture.lib.PictureSelector
import com.luck.picture.lib.config.PictureConfig
import com.luck.picture.lib.config.PictureMimeType
import com.luck.picture.lib.config.PictureSelectionConfig
import com.luck.picture.lib.entity.LocalMedia
import com.luck.picture.lib.listener.OnResultCallbackListener
import com.luck.picture.lib.permissions.PermissionChecker
import com.clz.workorder.R
import com.clz.workorder.BR
import com.clz.workorder.databinding.FragmentOrderDetailBinding
import java.util.*

@Route(path = AppConstants.Router.Order.F_ORDER_DETAIL)
class OrderDetailFragment : BaseFragment<FragmentOrderDetailBinding, OrderDetailViewmodel>() {
    private val maxSelectNum = 9
    private val selectList: ArrayList<LocalMedia> = ArrayList()
    private var gAdapter: GridImageAdapter? = null
    private val pop: PopupWindow? = null
    override fun initContentView(): Int {
        return R.layout.fragment_order_detail
    }

    override fun initVariableId(): Int {
        return BR.viewModel
    }

    override fun initData() {
        viewModel.tvTitle.set("报修详情")
        viewModel.reportContentNum.set("1/500")
    }
    override fun initViewObservable() {
        viewModel.reportContentNum.set("werlkjaskldf")
        gAdapter= GridImageAdapter(context) {
            showpop()
        }.apply {
            setList(selectList)
            setSelectMax(9)
            setOnItemClickListener(object :GridImageAdapter.OnItemClickListener{
                override fun onItemClick(position: Int, v: View?) {
                    val isw=PermissionChecker.checkSelfPermission(context,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    if (isw){
                        if (selectList.size > 0) {
                            val media = selectList[position]
                            val pictureType: String = media.mimeType
                            val mediaType: Int = PictureMimeType.getMimeType(pictureType)
                            when (mediaType) {
                                1 ->                             // 预览图片 可自定长按保存路径
                                    //PictureSelector.create(MainActivity.this).externalPicturePreview(position, "/custom_file", selectList);
                                {PictureSelectionConfig.imageEngine=GlideEngine.createGlideEngine()
                                    PictureSelector.create(this@OrderDetailFragment)
                                        .externalPicturePreview(position, selectList,R.anim.photo_dialog_in_anim)}

                                2 ->                             // 预览视频
                                    PictureSelector.create(activity)
                                        .externalPictureVideo(media.path)
                                3 ->                             // 预览音频
                                    PictureSelector.create(activity)
                                        .externalPictureAudio(media.path)
                            }
                        }
                    }else{

                    }
                }

            })
        }
        binding.rvReportSubmit.apply {
            layoutManager= FullyGridLayoutManager(context,4, GridLayoutManager.VERTICAL, false)
            adapter=gAdapter
        }
        viewModel.reportTypeData.observe(this,{
            DialogHelper.showBottomListDialog(requireContext(),it) { position, text ->
                viewModel.reportType.set(text)
            }
        })
        viewModel.reportLocationData.observe(this,{
            DialogHelper.showBottomListDialog(requireContext(),it) { position, text ->
                viewModel.reportLocation.set(text)
            }
        })
    }
    fun showpop(){
        var list=ArrayList<String>()
        list.add("相册")
        list.add("拍照")
        DialogHelper.showBottomListDialog(requireContext(), list) { position, text ->
            when (text){
                "相册" ->
                    PictureSelector.create(activity)
                        .openGallery(PictureMimeType.ofImage())
                        .imageEngine(GlideEngine.createGlideEngine())
                        .maxSelectNum(maxSelectNum-selectList.size)
                        .imageSpanCount(4)
                        .selectionMode(PictureConfig.MULTIPLE)
                        .forResult(object : OnResultCallbackListener<LocalMedia>{
                            @SuppressLint("NotifyDataSetChanged")
                            override fun onResult(result: MutableList<LocalMedia>?) {
                                if (selectList.size+ result!!.size <=maxSelectNum){
                                    result.let { selectList.addAll(it) }
                                    gAdapter?.setList(selectList)
                                    gAdapter?.notifyDataSetChanged()
                                }
                            }

                            override fun onCancel() {
                            }
                        })
                "拍照"->
                    PictureSelector.create(activity)
                        .openCamera(PictureMimeType.ofImage())
                        .imageEngine(GlideEngine.createGlideEngine())
                        .forResult(object : OnResultCallbackListener<LocalMedia>{
                            @SuppressLint("NotifyDataSetChanged")
                            override fun onResult(result: MutableList<LocalMedia>?) {
                                if (selectList.size+ result!!.size <=maxSelectNum){
                                    result.let { selectList.addAll(it) }
                                    gAdapter?.setList(selectList)
                                    gAdapter?.notifyDataSetChanged()
                                }
                            }

                            override fun onCancel() {
                            }
                        })
            }
        }
    }


}