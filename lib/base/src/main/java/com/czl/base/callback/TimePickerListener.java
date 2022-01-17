package com.czl.base.callback;

/**
 * 创建日期：2022/1/10  15:35
 * 类说明:
 *
 * @author：86152
 */
import android.view.View;

import java.util.Date;

public interface TimePickerListener {

    void onTimeChanged(Date date);
    void onTimeConfirm(Date date, View view);
}
