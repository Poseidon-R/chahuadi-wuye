package com.czl.base.widget

import android.content.Context
import android.graphics.Typeface
import com.czl.base.R
import android.content.res.TypedArray
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView


/**
 *
 * @Description:
 * @Author: XCH
 * @CreateDate: 2021/12/24 10:10
 */
class TypefaceTextView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defAttrStyle: Int = 0
) : AppCompatTextView(context, attrs, defAttrStyle) {

    private var mTypefaces: MutableMap<String, Typeface?>? = null

    init {
        initAttrs(context, attrs)
    }

    private fun initAttrs(context: Context, attrs: AttributeSet?) {

        if (mTypefaces == null) {
            mTypefaces = HashMap()
        }

        if (this.isInEditMode) {
            return
        }
        val array: TypedArray = context.obtainStyledAttributes(attrs, R.styleable.TypefaceTextView)
        if (array != null) {
            val typefaceAssetPath = array.getString(
                R.styleable.TypefaceTextView_customTypeface
            )
            if (typefaceAssetPath != null) {
                var typeface: Typeface?
                if (mTypefaces!!.containsKey(typefaceAssetPath)) {
                    typeface = mTypefaces!![typefaceAssetPath]
                } else {
                    val assets = context.assets
                    typeface = Typeface.createFromAsset(assets, typefaceAssetPath)
                    mTypefaces!![typefaceAssetPath] = typeface
                }
                setTypeface(typeface)
            }
            array.recycle()
        }
    }
}