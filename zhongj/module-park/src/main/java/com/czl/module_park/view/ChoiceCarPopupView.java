package com.czl.module_park.view;

import android.content.Context;
import android.widget.TextView;

import com.czl.base.data.bean.CarItem;
import com.lxj.xpopup.core.CenterPopupView;
import com.lxj.xpopup.widget.VerticalRecyclerView;
import com.czl.module_park.R;
import com.czl.module_park.adapter.ChoiceCarAdapter;

import java.util.List;

public class ChoiceCarPopupView extends CenterPopupView {

    private VerticalRecyclerView rv;
    private ChoiceCarAdapter choiceCarAdapter;
    private List<CarItem> list;
    private TextView cancelBtn, confirmBtn;
    private ChoiceCarItemListener listener;

    public interface ChoiceCarItemListener {
        void choiceCarClick(int no);
    }

    public ChoiceCarPopupView(Context context, List<CarItem> list, ChoiceCarItemListener listener) {
        super(context);
        this.list = list;
        this.listener = listener;
    }

    @Override
    protected void onCreate() {
        super.onCreate();
        initView();
    }

    private void initView() {
        cancelBtn = findViewById(R.id.tv_cancel);
        confirmBtn = findViewById(R.id.tv_confirm);
        cancelBtn.setOnClickListener(v -> dismiss());
        confirmBtn.setOnClickListener(v -> {
            if (listener != null) listener.choiceCarClick(0);
            dismiss();
        });
        choiceCarAdapter = new ChoiceCarAdapter();
        rv = findViewById(R.id.rv);
        rv.setAdapter(choiceCarAdapter);
        choiceCarAdapter.setList(list);
    }

    @Override
    protected int getImplLayoutId() {
        return R.layout.view_choice_car;
    }

}
