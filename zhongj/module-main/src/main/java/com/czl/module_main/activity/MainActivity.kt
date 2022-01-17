package com.czl.module_main.activity

import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import com.alibaba.android.arouter.facade.annotation.Route
import com.czl.base.base.AppManager
import com.czl.base.base.BaseActivity
import com.czl.base.config.AppConstants
import com.czl.base.event.LiveBusCenter
import com.czl.base.route.RouteCenter
import com.czl.base.util.DialogHelper
import com.czl.module_main.BR
import com.czl.module_main.R
import com.czl.module_main.adapter.MyViewPagerAdapter
import com.czl.module_main.databinding.ActivityMainBinding
import com.czl.module_main.viewmodel.MainViewModel
import com.michoi.calling.TalkHelper
import me.majiajie.pagerbottomtabstrip.NavigationController
import me.majiajie.pagerbottomtabstrip.item.BaseTabItem
import me.majiajie.pagerbottomtabstrip.item.NormalItemView
import me.yokeyword.fragmentation.SupportFragment


@Route(path = AppConstants.Router.Main.A_MAIN)
class MainActivity : BaseActivity<ActivityMainBinding, MainViewModel>() {

    private var touchTime: Long = 0L
    private var loginOutFlag: Int = 0

    override fun initContentView(): Int {
        return R.layout.activity_main
    }

    override fun initVariableId(): Int {
        return BR.viewModel
    }

    override fun useBaseLayout(): Boolean {
        return false
    }

    override fun initData() {
        /*
         *login 所需参数：手机号
         */
        TalkHelper.HELPER.imLogin(dataRepository.getLoginPhone())

        setSwipeBackEnable(false)
        initViewPager()
        initBottomBar()
        showLogoutPop()
    }

    private fun showLogoutPop() {
        loginOutFlag = dataRepository.getLoginOutFlag()
        if (loginOutFlag == 1) {
            DialogHelper.showNoCancelDialog(
                this,
                "提示",
                "您的帐号已申请注销流程。再次登录后，注销流程（30个工作日）即自动撤销。",
                false
            ) {
                dataRepository.saveLoginOutFlag(0)
            }.show()
        }
    }


    private fun initViewPager() {
        // 设置不可滑动
//        binding.viewPager.isUserInputEnabled = false
        val homeFragment = RouteCenter.navigate(AppConstants.Router.Main.F_HOME) as SupportFragment
        val serviceFragment =
            RouteCenter.navigate(AppConstants.Router.Service.F_SERVICE) as SupportFragment
        val userFragment = RouteCenter.navigate(AppConstants.Router.User.F_USER) as SupportFragment
        val fragments = arrayListOf(homeFragment, serviceFragment, userFragment)
        binding.viewPager.apply {
            adapter = MyViewPagerAdapter(supportFragmentManager, fragments as List<Fragment>?)
            offscreenPageLimit = fragments.size
            addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
                override fun onPageScrolled(
                    position: Int,
                    positionOffset: Float,
                    positionOffsetPixels: Int
                ) {
                }

                override fun onPageSelected(position: Int) {
                    when {
                        position != 0 -> homeFragment.setFragmentResult(
                            0,
                            Bundle().apply { putBoolean("hidekeyboard", true) })
                    }
                }

                override fun onPageScrollStateChanged(state: Int) {
                }
            })
        }


    }

    private fun initBottomBar() {
        val navigationController: NavigationController = binding.tab.custom()
            .addItem(
                newItem(
                    R.mipmap.ic_home_off,
                    R.mipmap.ic_home_on,
                    getString(R.string.main_tab_home)
                )
            )
            .addItem(
                newItem(
                    R.mipmap.ic_home_off,
                    R.mipmap.ic_home_on,
                    getString(R.string.main_tab_service)
                )
            )
            .addItem(
                newItem(
                    R.mipmap.ic_me_off,
                    R.mipmap.ic_me_on,
                    getString(R.string.main_tab_me)
                )
            )
            .build()
        //自动适配ViewPager页面切换
        navigationController.setupWithViewPager(binding.viewPager)
    }

    //创建一个Item
    private fun newItem(drawable: Int, checkedDrawable: Int, text: String): BaseTabItem? {
        val normalItemView = NormalItemView(this)
        normalItemView.initialize(drawable, checkedDrawable, text)
        normalItemView.setTextDefaultColor(Color.GRAY)
        normalItemView.setTextCheckedColor(-0xff6978)
        return normalItemView
    }

    override fun initViewObservable() {
        LiveBusCenter.observeLogoutEvent(this, {
            viewModel.model.clearLoginState()
            startContainerActivity(AppConstants.Router.Login.F_LOGIN)
            AppManager.instance.finishAllActivity()
        })
    }


    override fun onBackPressedSupport() {
        if (System.currentTimeMillis() - touchTime < 2000L) {
            AppManager.instance.appExit()
        } else {
            touchTime = System.currentTimeMillis()
            showNormalToast(getString(R.string.main_press_again))
        }
    }


}