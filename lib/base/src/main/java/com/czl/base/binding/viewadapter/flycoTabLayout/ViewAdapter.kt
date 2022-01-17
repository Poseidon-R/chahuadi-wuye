package com.czl.base.binding.viewadapter.flycoTabLayout

import androidx.databinding.BindingAdapter
import com.annimon.stream.Collectors
import com.annimon.stream.Stream
import com.czl.base.R
import com.czl.base.binding.command.BindingCommand
import com.flyco.tablayout.listener.CustomTabEntity
import com.flyco.tablayout.listener.OnTabSelectListener

object ViewAdapter {
    @JvmStatic
    @BindingAdapter(
        value = ["tabs", "onTabSelectCommand"],
        requireAll = false
    )
    fun initTabLayout(
        tabLayout: CommonTabLayout,
        tabs: String,
        onTabSelectCommand: BindingCommand<Int?>?
    ) {
        var tabs =
            Stream.of(tabs.split(",")).map { r -> TabEntity(r, R.mipmap.ic_add, R.mipmap.ic_add) }
                .collect(Collectors.toList()) as
                    ArrayList<CustomTabEntity>
        tabLayout.setTabData(tabs)
        tabLayout.setOnTabSelectListener(object : OnTabSelectListener {
            override fun onTabSelect(position: Int) {
                onTabSelectCommand?.execute(position)
            }

            override fun onTabReselect(position: Int) {
            }

        })

    }
}