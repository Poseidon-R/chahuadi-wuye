package com.czl.base.widget

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.SparseArray
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import com.blankj.utilcode.util.LogUtils
import com.blankj.utilcode.util.RegexUtils
import com.czl.base.R
import com.czl.base.base.BaseActivity
import com.czl.base.base.BaseBean
import com.czl.base.binding.command.BindingAction
import com.czl.base.binding.command.BindingCommand
import com.czl.base.config.AppConstants
import com.czl.base.databinding.PopShareArticleBinding
import com.czl.base.extension.ApiSubscriberHelper
import com.czl.base.util.RxThreadHelper
import com.gyf.immersionbar.ImmersionBar
import com.lxj.xpopup.core.BottomPopupView
import com.lxj.xpopup.util.KeyboardUtils
import com.lxj.xpopup.util.XPopupUtils
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject

/**
 * @author Alwyn
 * @Date 2020/12/9
 * @Description
 */
@SuppressLint("ViewConstructor")
class ShareArticlePopView(
    val activity: BaseActivity<*, *>,
    private val webDataArray: SparseArray<String>? = null
) : BottomPopupView(activity) {
    private var dataBinding: PopShareArticleBinding? = null
    val tvObservableTitle: ObservableField<String> = ObservableField("")
    val tvObservableLink: ObservableField<String> = ObservableField("")
    val tvOpenFlag: ObservableBoolean = ObservableBoolean(true)
    override fun getImplLayoutId(): Int {
        return R.layout.pop_share_article
    }

    override fun onCreate() {
        super.onCreate()
        dataBinding = DataBindingUtil.bind(popupImplView)
        dataBinding?.apply {
            pop = this@ShareArticlePopView
            clRoot.background = XPopupUtils.createDrawable(
                ContextCompat.getColor(context, R.color.white),
                30f,
                30f,
                0f,
                0f
            )
            initInputState()
            if (webDataArray != null) {
                tvObservableTitle.set(webDataArray[0])
                tvObservableLink.set(webDataArray[1])
            }
            tvOpenFlag.set(webDataArray == null)
            executePendingBindings()
        }
        dataBinding?.etTitle?.setSelection(webDataArray?.get(0)?.length ?: 0)
    }

    private fun PopShareArticleBinding.initInputState() {
        val accountSubject = PublishSubject.create<String>()
        val pwdSubject = PublishSubject.create<String>()
        etTitle.addTextChangedListener(EditTextMonitor(accountSubject))
        etLink.addTextChangedListener(EditTextMonitor(pwdSubject))
        btnShare.isEnabled = false
        btnShare.isClickable = false
        activity.viewModel.addSubscribe(
            Observable.combineLatest(
                accountSubject,
                pwdSubject,
                { account: String, pwd: String -> account.isNotEmpty() && pwd.isNotEmpty() })
                .subscribe { flag ->
                    sdlShare.setLayoutBackground(
                        ContextCompat.getColor(
                            activity,
                            if (flag) R.color.md_theme_red else R.color.md_grey_200
                        )
                    )
                    btnShare.setTextColor(
                        ContextCompat.getColor(
                            activity,
                            if (!flag) R.color.white_aa else R.color.defaultTextColor
                        )
                    )
                    btnShare.isEnabled = flag
                    btnShare.isClickable = flag
                })
    }

    val onBtnShareClickCommand: BindingCommand<Void> = BindingCommand(BindingAction {
        dataBinding?.apply {

        }
    })

    val onOpenLinkClick: BindingCommand<Void> = BindingCommand(BindingAction {
        KeyboardUtils.hideSoftInput(this)
        dataBinding?.apply {
            val link = etLink.text.toString().trim()
            if (RegexUtils.getMatches(AppConstants.Constants.REGEX_URL,link).isNotEmpty()) {
                activity.viewModel.startContainerActivity(
                    AppConstants.Router.Web.F_WEB,
                    Bundle().apply {
                        putString(AppConstants.BundleKey.WEB_URL, link)
                    })
            } else {
                activity.showNormalToast("链接格式不正确，请重新输入")
            }
        }
    })

    override fun onDestroy() {
        dataBinding?.unbind()
        super.onDestroy()
    }
}