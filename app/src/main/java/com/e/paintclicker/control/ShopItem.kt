package com.e.paintclicker.control

import android.media.Image

class ShopItem {

    private lateinit var description:String
    public fun getDescription():String{
        return description
    }
    public fun setDescription(description:String){
        this.description=description
    }

    private lateinit var icon:Image
    public fun getImage():Image{
        return icon
    }
    public fun setImage(icon:Image){
        this.icon=icon
    }

    private lateinit var multiplier:MultiplierForBuying
    public fun getMultiplier():MultiplierForBuying{
        return multiplier
    }
    public fun setMultiplier(multiplier:MultiplierForBuying){
        this.multiplier=multiplier
    }


}
public enum class MultiplierForBuying(var multiplier: Int){
    I(1),
    X(10),
    XXV(25),
    C(100)
}