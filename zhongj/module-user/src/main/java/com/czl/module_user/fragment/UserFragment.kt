package com.czl.module_user.fragment

import android.content.Intent
import android.net.Uri
import com.alibaba.android.arouter.facade.annotation.Route
import com.annimon.stream.Collectors
import com.annimon.stream.Stream
import com.czl.base.base.BaseFragment
import com.czl.base.config.AppConstants
import com.czl.base.data.bean.AreaIdBean
import com.czl.base.event.LiveBusCenter
import com.czl.base.util.DayModeUtil
import com.czl.base.util.DialogHelper
import com.czl.module_user.BR
import com.czl.module_user.R
import com.czl.module_user.databinding.FragmentUserBinding
import com.czl.module_user.viewmodel.UserViewModel
import com.gyf.immersionbar.ImmersionBar
import com.lxj.xpopup.XPopup


@Route(path = AppConstants.Router.User.F_USER)
class UserFragment : BaseFragment<FragmentUserBinding, UserViewModel>() {

    private lateinit var areaList: List<AreaIdBean>

    override fun initContentView(): Int {
        return R.layout.fragment_user
    }

    override fun initVariableId(): Int {
        return BR.viewModel
    }

    override fun onSupportVisible() {
        ImmersionBar.with(this).fitsSystemWindows(false)
            .statusBarDarkFont(!DayModeUtil.isNightMode(requireContext())).init()
    }

    override fun initData() {
//        areaList = viewModel.model.getAreaList()
        viewModel.getUserInfo()
//        viewModel.getHotLine()
//        viewModel.getDeleteNotice()
    }

    override fun initViewObservable() {
        //退出登陆
        viewModel.uc.confirmLayoutEvent.observe(this, {
            DialogHelper.showBaseDialog(requireContext(), "注销", "确定退出吗？") {
                viewModel.logout()
            }
        })

        //客服热线
        viewModel.uc.hotLineEvent.observe(this, {
            DialogHelper.showBottomListDialog(
                requireContext(),
                "客服热线",
                arrayOf(it)
            ) { _: Int, text: String? -> text?.let { it1 -> callPhone(it1) } }
        })
        //注销须知
        viewModel.uc.confirmDeleteAcountEvent.observe(this, {
            DialogHelper.showBaseDialog(
                requireContext(),
                "提示",
                "注销后，您将无法使用当前账号，相关数据也将被删除无法找回，如需注销账户，请仔细阅读并理解注销须知。"
            )
            {
                XPopup.Builder(context)
                    .isDestroyOnDismiss(true) //对于只使用一次的弹窗，推荐设置这个
                    .asConfirm(
                        "罗湖棚改注销须知",
                        it,
                        null,
                        "同意须知并注销",
                        { viewModel.deleteUserAccount() },
                        null,
                        true,
                        R.layout.my_confim_popup
                    ) //最后一个参数绑定已有布局
                    .show()
            }
        })
        viewModel.uc.changeAreaEvent.observe(this, {
            if (areaList.isNullOrEmpty()) {
                showErrorToast("小区暂无可选")
                return@observe
            }
            DialogHelper.showBottomListDialog(
                requireContext(),
                Stream.of(areaList).map { t -> t.areaName }.withoutNulls()
                    .collect(Collectors.toList()) as ArrayList<String>
            ) { position, text ->
                viewModel.areaName.set(text)
                viewModel.model.saveAreaId(areaList!![position].areaId)
                viewModel.model.saveAreaName(text)
            }
        })

        LiveBusCenter.observeModifyUserInfoEvent(this) {
            viewModel.getUserInfo()
        }
    }

    override fun useBaseLayout(): Boolean {
        return false
    }

    /**
     * 拨打电话（跳转到拨号界面，用户手动点击拨打）
     *
     * @param phoneNum 电话号码
     */
    private fun callPhone(phoneNum: String) {
        val intent = Intent(Intent.ACTION_DIAL)
        val data: Uri = Uri.parse("tel:$phoneNum")
        intent.data = data
        startActivity(intent)
    }

}