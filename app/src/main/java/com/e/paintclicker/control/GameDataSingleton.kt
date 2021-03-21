package com.e.paintclicker.control

object GameDataSingleton{
    public lateinit var playerName:String
    public var currencies:List<Currency> = listOf()// = listOf(Currency("Paintings"), Currency("ArtBucks"))
    init {
        enumValues<currencyEnum>().forEach {
            currencies=currencies+listOf(Currency(it.name))
        }
    }
}

public class Currency{
    lateinit var name:String
    var amount:Int = 0

    constructor(currencyName:String){
        name=currencyName
    }
}
public enum class currencyEnum(var index: Int){
    Paintings(0),
    ArtBucks(1)
}