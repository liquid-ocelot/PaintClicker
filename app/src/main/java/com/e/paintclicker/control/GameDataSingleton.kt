package com.e.paintclicker.control

object GameDataSingleton{
    //Gloal informations
    public var playerName:String=""

    //Currencies
    public var currencies:List<Currency> = listOf()// = listOf(Currency("Paintings"), Currency("ArtBucks"))

    //Global Ameliorations Levels
        //how many brush strokes a tap is worth
    public var clickAmount:Int=1
        //how many artBucks a painting is worth
    public var paintingWorth:Float=1f
        //The multiplier and amount per multipler for the amount of artbucks earned with each strokes
    public var sponsorLevel:Int=0
    public var sponsorGain:Float= 0.01F
        //the multiplier for the amount of paintings generated naturally by apprentices
    public var apprenticeLevel:Int=0
        //the multiplier and amount per multiplier for the amount of money gained
    public var sellingLevel:Int=0
    public var sellingUpgrade:Float=0.05f

    //Global Ameliorations Unlocks
    public var canSellPaintings:Boolean=false

    //Global Price




    init {
        enumValues<currencyEnum>().forEach {
            currencies=currencies+listOf(Currency(it.name))
        }
    }

    public fun GetDataToSave():List<Byte>{
        return listOf(
            currencies[currencyEnum.Paintings.index].amount.toByte(),
            currencies[currencyEnum.ArtBucks.index].amount.toByte(),
            clickAmount.toByte(),
            sponsorLevel.toByte(),
            apprenticeLevel.toByte(),
            sellingLevel.toByte()
        )
    }
    public fun SetDataFromSave(data:List<Byte>){
        currencies[currencyEnum.Paintings.index].amount=data[0].toFloat()
        currencies[currencyEnum.ArtBucks.index].amount=data[1].toFloat()
        clickAmount=data[2].toInt()
        sponsorLevel=data[3].toInt()
        apprenticeLevel=data[4].toInt()
        sellingLevel=data[5].toInt()
    }
}

public class Currency{
    lateinit var name:String
    var amount:Float = 0f

    constructor(currencyName:String){
        name=currencyName
    }
}
public enum class currencyEnum(var index: Int){
    Paintings(0),
    ArtBucks(1)
}