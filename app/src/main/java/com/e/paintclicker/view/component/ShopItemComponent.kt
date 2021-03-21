package com.e.paintclicker.view.component

import android.content.Context
import android.opengl.Visibility
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.e.paintclicker.R

//https://medium.com/mobile-app-development-publication/building-custom-component-with-kotlin-fc082678b080

class ShopItemComponent (
        context: Context,
        attrs: AttributeSet
) : LinearLayout(context,attrs) {



    init {
        inflate(context, R.layout.shop_item_view, this)

        val imageView: ImageView = findViewById(R.id.shopItemIconImageView)
        val textView: TextView = findViewById(R.id.shopItemDescriptionTextView)

        val attributes = context.obtainStyledAttributes(attrs, R.styleable.ShopItemView)
        imageView.setImageDrawable(attributes.getDrawable(R.styleable.ShopItemView_icon))
        textView.text = attributes.getString(R.styleable.ShopItemView_description)
        attributes.recycle()

    }
}