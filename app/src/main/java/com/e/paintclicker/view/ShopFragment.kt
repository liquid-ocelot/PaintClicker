package com.e.paintclicker.view

import android.opengl.Visibility
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.view.forEach
import com.e.paintclicker.R
import com.e.paintclicker.control.GameDataSingleton
import com.e.paintclicker.control.currencyEnum
import com.e.paintclicker.databinding.FragmentShopBinding
import com.e.paintclicker.view.component.ShopItemComponent
import org.w3c.dom.Text
import java.io.File
import kotlin.math.pow

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"



/**
 * A simple [Fragment] subclass.
 * Use the [ShopFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ShopFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private var _binding: FragmentShopBinding? = null
    private val binding get() = _binding!!



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentShopBinding.inflate(inflater, container, false)
        val view = binding.root
        // Inflate the layout for this fragment

        //check if necessary to load data from the singleton
        var setupAsUsual:Boolean=true
        var directory: File
        val file= File(activity?.applicationContext?.filesDir, GameDataSingleton.playerName+".txt")
        if(file.exists()){
            setupAsUsual=false
        }

        //act on every shop items to set them up
        for (i in 0..view.findViewById<LinearLayout>(R.id.shopLinearLayout).childCount){
            val element=view.findViewById<LinearLayout>(R.id.shopLinearLayout).getChildAt(i)
            if(element is ShopItemComponent){

                //link all OnClicks of the shopItems to the onBuyClick function
                (element as ShopItemComponent).findViewById<Button>(R.id.shopItemBuyButton).setOnClickListener { onBuyClick(it) }
                (element as ShopItemComponent).findViewById<Button>(R.id.shopItemBuyButton).setOnClickListener(
                    View.OnClickListener { onBuyClick(it) })
                //only show items we want, hide the others
                if(setupAsUsual||!GameDataSingleton.canSellPaintings) {
                    when (element.tag) {
                        "startVisible" -> element.visibility = View.VISIBLE
                        else -> {
                            element.visibility = View.INVISIBLE
                            element.visibility = View.GONE
                        }
                    }
                }
            }

            //reload data and setup shop
            binding.getBetterAtArtItem.setPrice(5f*1.1f.pow(GameDataSingleton.sellingLevel))
            binding.getBetterAtArtItem.setLevel(GameDataSingleton.sellingLevel)
            binding.upgradeSponsorItem.setPrice(20f*1.3f.pow(GameDataSingleton.sponsorLevel))
            binding.upgradeSponsorItem.setLevel(GameDataSingleton.sponsorLevel)
            binding.upgradeApprenticeItem.setPrice(20f*4f.pow(GameDataSingleton.apprenticeLevel))
            binding.upgradeApprenticeItem.setLevel(GameDataSingleton.apprenticeLevel)
            if(GameDataSingleton.canSellPaintings){
                binding.sellPaintingsUnlockItem.visibility=View.GONE
            }
            binding.upgradeToolsItem.setPrice(5f*7f.pow(GameDataSingleton.clickAmount-1))
            binding.upgradeToolsItem.setLevel(GameDataSingleton.clickAmount)
            if(GameDataSingleton.clickAmount>=4){
                binding.upgradeToolsItem.visibility=View.GONE
            }
        }

        return view
    }

    //Do stuff (upgrade, unlock upgrades, etc) when the player presses a buy button
    private fun onBuyClick(view:View){
        when(view.tag){
            "getBetterAtArtItem"->if(GameDataSingleton.currencies[currencyEnum.Paintings.index].amount>=5f*1.1f.pow(GameDataSingleton.sellingLevel)){
                GameDataSingleton.currencies[currencyEnum.Paintings.index].amount-=5f*1.1f.pow(GameDataSingleton.sellingLevel)
                GameDataSingleton.sellingLevel+=1
                binding.getBetterAtArtItem.findViewById<Button>(R.id.shopItemBuyButton).text=(5f*1.1f.pow(GameDataSingleton.sellingLevel)).toString()
                binding.getBetterAtArtItem.setPrice(5f*1.1f.pow(GameDataSingleton.sellingLevel))
                binding.getBetterAtArtItem.setLevel(GameDataSingleton.sellingLevel)
            }
            "upgradeSponsorItem"->if(GameDataSingleton.currencies[currencyEnum.Paintings.index].amount>=20f*1.3f.pow(GameDataSingleton.sponsorLevel)){
                GameDataSingleton.currencies[currencyEnum.Paintings.index].amount-=20f*1.3f.pow(GameDataSingleton.sponsorLevel)
                GameDataSingleton.sponsorLevel+=1
                binding.upgradeSponsorItem.setPrice(20f*1.3f.pow(GameDataSingleton.sponsorLevel))
                binding.upgradeSponsorItem.setLevel(GameDataSingleton.sponsorLevel)
            }
            "upgradeApprenticeItem"->if(GameDataSingleton.currencies[currencyEnum.ArtBucks.index].amount>=20f*4f.pow(GameDataSingleton.apprenticeLevel)){
                GameDataSingleton.currencies[currencyEnum.ArtBucks.index].amount-=20f*4f.pow(GameDataSingleton.apprenticeLevel)
                GameDataSingleton.apprenticeLevel+=1
                binding.upgradeApprenticeItem.setPrice(20f*4f.pow(GameDataSingleton.apprenticeLevel))
                binding.upgradeApprenticeItem.setLevel(GameDataSingleton.apprenticeLevel)

            }
            "sellPaintingsUnlockItem"->if(GameDataSingleton.currencies[currencyEnum.Paintings.index].amount>=10){
                GameDataSingleton.currencies[currencyEnum.Paintings.index].amount-=10
                binding.upgradeSponsorItem.visibility=View.VISIBLE
                binding.upgradeApprenticeItem.visibility=View.VISIBLE
                binding.sellPaintingsUnlockItem.visibility=View.GONE
                GameDataSingleton.canSellPaintings=true
            }
            "upgradeToolsItem"->if(GameDataSingleton.currencies[currencyEnum.ArtBucks.index].amount>=5f*7f.pow(GameDataSingleton.clickAmount-1)){
                GameDataSingleton.currencies[currencyEnum.ArtBucks.index].amount-=5f*7f.pow(GameDataSingleton.clickAmount-1)
                GameDataSingleton.clickAmount+=1
                binding.upgradeToolsItem.setPrice(5f*7f.pow(GameDataSingleton.clickAmount-1))
                binding.upgradeToolsItem.setLevel(GameDataSingleton.clickAmount)
                if(GameDataSingleton.clickAmount>=4){
                    binding.upgradeToolsItem.visibility=View.GONE
                }
                //binding.upgradeObtainedListView.add
            }
            else->{

            }
        }
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment ShopFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ShopFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}
