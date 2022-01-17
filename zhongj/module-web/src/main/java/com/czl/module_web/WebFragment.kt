package com.czl.module_web

import android.view.ViewGroup
import com.alibaba.android.arouter.facade.annotation.Route
import com.czl.base.base.BaseFragment
import com.czl.base.config.AppConstants
import com.czl.module_web.databinding.ActivityWebBinding
import com.czl.module_web.viewmodel.WebViewModel
import android.widget.LinearLayout

import com.just.agentweb.AgentWeb
import android.webkit.WebView

import android.graphics.Bitmap
import android.text.TextUtils
import com.just.agentweb.WebChromeClient
import com.just.agentweb.WebViewClient


@Route(path = AppConstants.Router.Web.F_WEB)
class WebFragment : BaseFragment<ActivityWebBinding, WebViewModel>() {

    var mAgentWeb: AgentWeb? = null
    var url = ""

    override fun initContentView(): Int {
        return R.layout.activity_web
    }

    override fun initVariableId(): Int {
        return BR.viewModel
    }

    override fun initData() {
        url = arguments?.getString(AppConstants.BundleKey.WEB_MENU_KEY).toString()
        mAgentWeb = AgentWeb.with(this)
            .setAgentWebParent(binding.ll, LinearLayout.LayoutParams(-1, -1))
            .useDefaultIndicator()
            .setWebChromeClient(mWebChromeClient)
            .setWebViewClient(mWebViewClient)
            .createAgentWeb()
            .ready()
            .go(url)

    }

    private val mWebViewClient: WebViewClient = object : WebViewClient() {

        override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
//            viewModel.tvTitle.set()
        }
    }
    private val mWebChromeClient: WebChromeClient = object : WebChromeClient() {
        override fun onReceivedTitle(view: WebView?, title: String?) {
            super.onReceivedTitle(view, title)
            if (!TextUtils.isEmpty(title)) {
                var topTitle = title
                if (title!!.length > 10) {
                    topTitle = title.substring(0, 10) + "..."
                }
                viewModel.tvTitle.set(topTitle)
            }

        }

        override fun onProgressChanged(view: WebView?, newProgress: Int) {
            //do you work
        }
    }
}