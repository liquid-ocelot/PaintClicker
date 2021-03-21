package com.e.paintclicker.view.component

import android.content.Context
import android.opengl.Visibility
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.e.paintclicker.R

//https://medium.com/mobile-app-development-publication/building-custom-component-with-kotlin-fc082678b080

class ShopItemComponent (
        context: Context,
        attrs: AttributeSet
) : LinearLayout(context,attrs) {

    public fun getPrice():Int{
        return (findViewById<Button>(R.id.shopItemBuyButton)).text.toString().toInt()
    }
    public fun setPrice(price:Float){
        (findViewById<Button>(R.id.shopItemBuyButton)).text=price.toString()
    }
    public fun setLevel(level:Int){
        (findViewById<TextView>(R.id.shopItemLevelTextView)).text=level.toString()
    }
    public fun getBuyButton():Button{
        return this.findViewById<Button>(R.id.shopItemBuyButton)
    }
    init {
        inflate(context, R.layout.shop_item_view, this)

        val imageView: ImageView = findViewById(R.id.shopItemIconImageView)
        val textView: TextView = findViewById(R.id.shopItemDescriptionTextView)
        val level:TextView=findViewById(R.id.shopItemLevelTextView)
        val buy:Button=findViewById(R.id.shopItemBuyButton)

        val attributes = context.obtainStyledAttributes(attrs, R.styleable.ShopItemView)

        imageView.setImageDrawable(attributes.getDrawable(R.styleable.ShopItemView_icon))
        textView.text = attributes.getString(R.styleable.ShopItemView_description)
        level.text=attributes.getString(R.styleable.ShopItemView_level)
        buy.text=attributes.getString(R.styleable.ShopItemView_price)
        buy.tag=attributes.getString(R.styleable.ShopItemView_buttonTag)
        attributes.recycle()

    }
}