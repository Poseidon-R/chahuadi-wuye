package com.czl.base.view;

import android.content.Context;
import android.content.res.Resources;
import android.util.AttributeSet;

import com.czl.base.R;
import com.github.nikartm.button.FitButton;
import com.sjzn.keyboard.KeyboardInputController;
import com.sjzn.keyboard.OnInputChangedListener;
import com.sjzn.keyboard.PopupKeyboard;
import com.sjzn.keyboard.view.InputView;

import androidx.annotation.Nullable;

/**
 * Author:xch
 * Date:2021/9/7
 * Do:
 */
public class CarBoardView extends ViewBase {

    private FitButton lockBtn;
    private UpdateCarNumListener updateCarNumListener;
    private PopupKeyboard mPopupKeyboard;

    public void setUpdateCarNumListener(UpdateCarNumListener updateCarNumListener) {
        this.updateCarNumListener = updateCarNumListener;
    }

    public CarBoardView(Context context) {
        super(context);
    }

    public CarBoardView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public CarBoardView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public int getViewId() {
        return R.layout.view_car_board;
    }

    @Override
    public void initData() {
        final InputView mInputView = findViewById(R.id.input_view);
        lockBtn = findViewById(R.id.btn_lock);

        Resources resources = getResources();
        // 创建弹出键盘
        mPopupKeyboard = new PopupKeyboard(mContext);
        // 弹出键盘内部包含一个KeyboardView，在此绑定输入两者关联。
        mPopupKeyboard.attach(mInputView, getActivity());

        // 隐藏确定按钮
        mPopupKeyboard.getKeyboardEngine().setHideOKKey(false);

        // KeyboardInputController提供一个默认实现的新能源车牌锁定按钮
        mPopupKeyboard.getController()
                .setDebugEnabled(true)
                .bindLockTypeProxy(new KeyboardInputController.LockNewEnergyProxy() {
                    @Override
                    public void setOnClickListener(OnClickListener listener) {
                        lockBtn.setOnClickListener(listener);
                    }

                    @Override
                    public void onNumberTypeChanged(boolean isNewEnergyType) {
                        lockBtn.setTextColor(resources.getColor(isNewEnergyType ? R.color.color_00A468 : R.color.color_666666));
                        lockBtn.setIconColor(resources.getColor(isNewEnergyType ? R.color.color_00A468 : R.color.color_666666));
                        lockBtn.setButtonColor(resources.getColor(isNewEnergyType ? R.color.color_1A00A468 : R.color.color_1A666666));
                    }
                });
        mPopupKeyboard.getController().addOnInputChangedListener(new OnInputChangedListener() {
            @Override
            public void onChanged(String number, boolean isCompleted) {

            }

            @Override
            public void onCompleted(String number, boolean isAutoCompleted) {
                if (updateCarNumListener != null)
                    updateCarNumListener.notifyCarNum(number);
                mPopupKeyboard.dismiss(getActivity());
            }
        });
    }

    //处理按返回建隐藏车牌号键盘
    public boolean keyboardDismiss() {
        if (null == mPopupKeyboard) {
            return false;
        }
        if (mPopupKeyboard.isShown()) {
            mPopupKeyboard.dismiss(getActivity());
            return true;
        } else {
            return false;
        }
    }

    public void clearNum() {
        mPopupKeyboard.getController().updateNumber("");
        keyboardDismiss();
    }

    public interface UpdateCarNumListener {
        void notifyCarNum(String number);
    }
}
