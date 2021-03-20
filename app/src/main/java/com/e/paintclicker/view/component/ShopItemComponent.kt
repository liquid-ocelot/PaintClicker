package com.e.paintclicker.view.component

import android.content.Context
import android.opengl.Visibility
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.e.paintclicker.R

//https://medium.com/mobile-app-development-publication/building-custom-component-with-kotlin-fc082678b080

class ShopItemComponent @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyle: Int = 0,
        defStyleRes: Int = 0
) : LinearLayout(context,attrs,defStyle,defStyleRes) {



    init {
        val inflate = LayoutInflater.from(context).inflate(R.layout.shop_item_view, this, true)

        orientation= HORIZONTAL
        attrs?.let {
            val allAttributes=context.obtainStyledAttributes(it,R.styleable.ShopItemViewAttributes, 0, 0)
            val description=resources.getText(allAttributes.getResourceId(R.styleable.ShopItemViewAttributes_description,
                            R.string.hello_blank_fragment))//le 2nd est le défaut, mais...quoi utiliser
            val image=resources.getDrawable(allAttributes.getResourceId(R.styleable.ShopItemViewAttributes_icon,
                    R.drawable.icon_placeholder))
            inflate.findViewWithTag<TextView>("shopItemDescriptionTextView").text=description
            inflate.findViewWithTag<ImageView>("shopItemIconImageView").setImageDrawable(image)
            this.isEnabled = resources.getBoolean(allAttributes.getResourceId(R.styleable.ShopItemViewAttributes_enabled,0))
            allAttributes.recycle()
        }
    }
}