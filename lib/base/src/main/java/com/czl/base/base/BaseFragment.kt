package com.czl.base.base

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.cooltechworks.views.shimmer.ShimmerRecyclerView
import com.czl.base.R
import com.czl.base.callback.ErrorCallback
import com.czl.base.callback.LoadingCallback
import com.czl.base.mvvm.ui.ContainerFmActivity
import com.czl.base.route.RouteCenter
import com.czl.base.util.DayModeUtil
import com.czl.base.util.DialogHelper
import com.czl.base.util.ToastHelper
import com.czl.base.widget.ShareArticlePopView
import com.gyf.immersionbar.ImmersionBar
import com.kingja.loadsir.callback.Callback
import com.kingja.loadsir.callback.SuccessCallback
import com.kingja.loadsir.core.Convertor
import com.kingja.loadsir.core.LoadService
import com.kingja.loadsir.core.LoadSir
import com.lxj.xpopup.XPopup
import com.lxj.xpopup.core.BasePopupView
import com.scwang.smart.refresh.layout.SmartRefreshLayout
import me.yokeyword.fragmentation.SupportFragment
import org.koin.android.ext.android.get
import java.lang.reflect.ParameterizedType


/**
 * Created by Alwyn on 2020/10/10.
 */
abstract class BaseFragment<V : ViewDataBinding, VM : BaseViewModel<*>> :
    BaseRxFragment(), IBaseView {
    lateinit var binding: V
    lateinit var viewModel: VM
    private var viewModelId = 0
    private var dialog: BasePopupView? = null
    private lateinit var rootView: View
    protected var rootBinding: ViewDataBinding? = null
    protected var ryCommon: RecyclerView? = null
    lateinit var loadService: LoadService<BaseBean<*>?>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initParam()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val loadSir = LoadSir.Builder()
            .addCallback(ErrorCallback())
            .addCallback(LoadingCallback())
            .setDefaultCallback(SuccessCallback::class.java)
            .build()
        if (useBaseLayout()) {
            rootView = inflater.inflate(R.layout.activity_base, null, false)
                .findViewById(R.id.activity_root)
            rootBinding = DataBindingUtil.bind(rootView)
            binding =
                DataBindingUtil.inflate(inflater, initContentView(), rootView as ViewGroup, true)
            // ?????????????????????????????????View
            loadService = loadSir.register(binding.root,
                Callback.OnReloadListener { reload() },
                Convertor<BaseBean<*>?> { t ->
                    if (t == null || t.errorCode != 0) {
                        ErrorCallback::class.java
                    } else {
                        SuccessCallback::class.java
                    }
                }) as LoadService<BaseBean<*>?>
            return rootView
        } else {
            binding = DataBindingUtil.inflate(inflater, initContentView(), container, false)
            loadService = loadSir.register(binding.root,
                Callback.OnReloadListener { reload() },
                Convertor<BaseBean<*>?> { t ->
                    if (t == null || t.errorCode != 0) {
                        ErrorCallback::class.java
                    } else {
                        SuccessCallback::class.java
                    }
                }) as LoadService<BaseBean<*>?>
            return loadService.loadLayout
        }
    }

    /**
     * ????????????????????????????????????
     */
    open fun reload() {
        loadService.showCallback(LoadingCallback::class.java)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        ImmersionBar.destroy(this)
        //??????Messenger??????
//        Messenger.getDefault().unregister(viewModel)
        binding.unbind()
        rootBinding?.unbind()
    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?
    ) {
        super.onViewCreated(view, savedInstanceState)
        //??????????????????DataBinding???ViewModel??????
        initViewDataBinding()
        //?????????ViewModel???View???????????????????????????
        registerUIChangeLiveDataCallBack()
    }

    /**
     * ??????????????????Fragment?????? onViewCreated-onLazyInitView-onEnterAnimationEnd
     * Viewpager???????????? onViewCreated-onLazyInitView
     */
    override fun onLazyInitView(savedInstanceState: Bundle?) {
        super.onLazyInitView(savedInstanceState)
        if (enableLazy()) {
            //???????????????????????????
            initData()
            //??????????????????????????????????????????ViewModel?????????View??????????????????
            initViewObservable()
        }
    }

    /**
     * ???????????????????????????
     */
    override fun onEnterAnimationEnd(savedInstanceState: Bundle?) {
        super.onEnterAnimationEnd(savedInstanceState)
        if (!enableLazy()) {
            //???????????????????????????
            initData()
            //??????????????????????????????????????????ViewModel?????????View??????????????????
            initViewObservable()
        }
    }

    override fun onSupportVisible() {
        super.onSupportVisible()
        if (isImmersionBarEnabled()) {
            initStatusBar()
        }
        if (isThemeRedStatusBar()) {
            initFitThemeStatusBar()
        }
    }

    open fun initStatusBar() {
        ImmersionBar.with(this)
            .statusBarDarkFont(!DayModeUtil.isNightMode(requireContext()), 0.2f)
            .statusBarColor(R.color.color_toolbar).init()
    }

    open fun isThemeRedStatusBar(): Boolean {
        return false
    }

    open fun initFitThemeStatusBar() {
        if (!DayModeUtil.isNightMode(requireContext())) {
            ImmersionBar.with(this)
                .statusBarDarkFont(false, 0.2f)
                .statusBarColor(R.color.md_theme_red)
                .fitsSystemWindows(true)
                .init()
        } else {
            ImmersionBar.with(this)
                .statusBarDarkFont(false, 0.2f)
                .statusBarColor(R.color.color_toolbar)
                .fitsSystemWindows(true)
                .init()
        }

    }

    /**
     * ????????????
     */
    private fun initViewDataBinding() {
        viewModelId = initVariableId()
        viewModel = initViewModel()
//        if (viewModel == null) {
//            val modelClass: Class<*>
//            val type = javaClass.genericSuperclass
//            modelClass = if (type is ParameterizedType) {
//                type.actualTypeArguments[1] as Class<*>
//            } else {
//                //????????????????????????????????????????????????BaseViewModel
//                BaseViewModel::class.java
//            }
//            viewModel = createViewModel<ViewModel>(this, modelClass) as VM
//        }
        rootBinding?.setVariable(viewModelId, viewModel)
        rootBinding?.lifecycleOwner = this
        binding.setVariable(viewModelId, viewModel)
        //??????LiveData??????xml??????????????????UI???????????????
        binding.lifecycleOwner = this
        //???ViewModel??????View?????????????????????
        lifecycle.addObserver(viewModel)
        //??????RxLifecycle????????????
        viewModel.injectLifecycleProvider(this)
        //??????VM?????????M?????????????????????UI??????
        viewModel.loadService = loadService
    }

    /**
     * ??????ViewModel???View?????????UI????????????
     */
    private fun registerUIChangeLiveDataCallBack() {
        //?????????????????????
        viewModel.uC.getShowLoadingEvent()
            .observe(this, { title: String? -> showLoading(title) })
        //?????????????????????
        viewModel.uC.getDismissDialogEvent()
            .observe(this, { dismissLoading() })
        //???????????????
        viewModel.uC.getStartActivityEvent().observe(this, { map ->
            val routePath: String = map[BaseViewModel.ParameterField.ROUTE_PATH] as String
            val bundle = map[BaseViewModel.ParameterField.BUNDLE] as Bundle?
            RouteCenter.navigate(routePath, bundle)
        }
        )
        viewModel.uC.getStartFragmentEvent().observe(this, { map ->
            val routePath: String = map[BaseViewModel.ParameterField.ROUTE_PATH] as String
            val bundle: Bundle? = map[BaseViewModel.ParameterField.BUNDLE] as Bundle?
            start(RouteCenter.navigate(routePath, bundle) as SupportFragment)
        })
        //??????ContainerActivity
        viewModel.uC.getStartContainerActivityEvent().observe(
            this, { params: Map<String?, Any?> ->
                val canonicalName = params[BaseViewModel.ParameterField.ROUTE_PATH] as String?
                val bundle = params[BaseViewModel.ParameterField.BUNDLE] as Bundle?
                val code = params[BaseViewModel.ParameterField.REQUEST_CODE] as Int?
                startContainerActivity(canonicalName, bundle, code)
            }
        )
        //????????????
        viewModel.uC.getFinishEvent().observe(this, {
            back()
        })
        //???????????????
        viewModel.uC.getOnBackPressedEvent().observe(
            this, { onBackPressedSupport() }
        )
        viewModel.uC.getScrollTopEvent().observe(this, {
            ryCommon?.smoothScrollToPosition(0)
        })
        viewModel.uC.getShowSharePopEvent().observe(this, {
            XPopup.Builder(context)
                .enableDrag(true)
                .moveUpToKeyboard(true)
                .autoOpenSoftInput(true)
                .asCustom(ShareArticlePopView(requireActivity() as BaseActivity<*, *>, it))
                .show()
        })
    }

    /**
     * ????????????????????????
     */
    fun <T> handleRecyclerviewData(
        nullFlag: Boolean,
        data: MutableList<*>?,
        mAdapter: BaseQuickAdapter<T, *>,
        ryCommon: ShimmerRecyclerView,
        smartCommon: SmartRefreshLayout,
        currentPage: Int,
        over: Boolean?,
        defaultPage: Int = 1
    ) {
        this.ryCommon = ryCommon
        if (nullFlag) {
            if (currentPage == defaultPage - 1)
                showErrorStatePage()
            smartCommon.finishRefresh(false)
            smartCommon.finishLoadMore(false)
            return
        }
        showSuccessStatePage()
        if (currentPage == defaultPage) {
            ryCommon.hideShimmerAdapter()
            if (!mAdapter.hasEmptyView()) {
                val emptyView = View.inflate(context, R.layout.common_empty_layout, null)
                emptyView.findViewById<ViewGroup>(R.id.ll_empty).setOnClickListener {
                    smartCommon.autoRefresh()
                }
                mAdapter.setEmptyView(emptyView)
            }
            mAdapter.setList(data as MutableList<T>?)
            ryCommon.smoothScrollToPosition(0)
            if (over!!) smartCommon.finishRefreshWithNoMoreData()
            else smartCommon.finishRefresh(true)
            return
        }
        if (over!!) smartCommon.finishLoadMoreWithNoMoreData()
        else smartCommon.finishLoadMore(true)
        mAdapter.addData(data as MutableList<T>)
    }

    /**
     * ?????????????????????,??????true
     *
     * @return
     */
    protected open fun enableLazy(): Boolean {
        return true
    }

    /**
     * ????????????????????????
     */
    open fun back() {
        if (preFragment == null) {
            requireActivity().finish()
        } else {
            pop()
        }
    }

    fun showLoading(title: String?) {
        dialog = DialogHelper.showLoadingDialog(requireContext(), title)
    }

    fun dismissLoading() {
        dialog?.smartDismiss()
    }

    fun showErrorToast(msg: String?) {
        ToastHelper.showErrorToast(msg)
    }

    fun showNormalToast(msg: String?) {
        ToastHelper.showNormalToast(msg)
    }

    fun showSuccessToast(msg: String?) {
        ToastHelper.showSuccessToast(msg)
    }

    fun showErrorStatePage() {
        loadService.showCallback(ErrorCallback::class.java)
    }

    fun showLoadingStatePage() {
        loadService.showCallback(LoadingCallback::class.java)
    }

    fun showSuccessStatePage() {
        loadService.showCallback(SuccessCallback::class.java)
    }

    /**
     * ????????????
     *
     * @param clz ??????????????????Activity???
     */
    fun startActivity(clz: Class<*>?) {
        startActivity(Intent(context, clz))
    }

    /**
     * ????????????
     *
     * @param clz    ??????????????????Activity???
     * @param bundle ????????????????????????
     */
    fun startActivity(clz: Class<*>?, bundle: Bundle?) {
        val intent = Intent(context, clz)
        if (bundle != null) {
            intent.putExtras(bundle)
        }
        startActivity(intent)
    }

    /**
     * ??????????????????
     * @param routePath Fragment????????????
     * @param bundle        ????????????????????????
     */
    fun startContainerActivity(
        routePath: String?,
        bundle: Bundle? = null, reqCode: Int? = null
    ) {
        val intent = Intent(context, ContainerFmActivity::class.java)
        intent.putExtra(ContainerFmActivity.FRAGMENT, routePath)
        if (bundle != null) intent.putExtra(ContainerFmActivity.BUNDLE, bundle)
        if (reqCode == null)
            startActivity(intent)
        else
            startActivityForResult(intent, reqCode)
    }

    //????????????
    fun refreshLayout() {
        binding.setVariable(viewModelId, viewModel)
    }

    /**
     * @return ?????????????????????
     */
    protected open fun useBaseLayout(): Boolean {
        return true
    }

    override fun initParam() {}

    /**
     * ??????????????????
     *
     * @return ??????layout???id
     */
    abstract fun initContentView(): Int


    /**
     * ?????????ViewModel???id
     *
     * @return BR???id
     */
    abstract fun initVariableId(): Int

    /**
     * ???????????????ViewModel
     *
     * @return ??????BaseViewModel???ViewModel
     */
    private fun initViewModel(): VM {
        val type = javaClass.genericSuperclass
        val modelClass: Class<VM> =
            (type as ParameterizedType).actualTypeArguments[1] as Class<VM>
        return ViewModelProvider(this, get() as AppViewModelFactory).get(modelClass)
    }

    override fun initData() {}

    override fun initViewObservable() {}

    open fun isImmersionBarEnabled(): Boolean {
        return useBaseLayout()
    }
}