package com.e.paintclicker.view

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import com.e.paintclicker.databinding.FragmentClickBinding
import com.e.paintclicker.view.opengl.OpenglCanvas
import com.e.paintclicker.view.opengl.Sprite
import java.lang.ref.WeakReference
import kotlin.concurrent.withLock

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ClickFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ClickFragment : Fragment(), Runnable {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private var _binding: FragmentClickBinding? = null
    private val binding get() = _binding!!


    lateinit var opgl: OpenglCanvas
    var drawing = false
    lateinit var thread: Thread

    var currentHiding = 0
    val hidingList: ArrayList<Sprite> = ArrayList<Sprite>()

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
        // Inflate the layout for this fragment
        _binding = FragmentClickBinding.inflate(inflater, container, false)





        context?.let {


            opgl = object : OpenglCanvas(it){
                override fun onTouchEvent(e: MotionEvent): Boolean {
                    when (e.action) {

                        MotionEvent.ACTION_DOWN -> {
                            if(currentHiding < 10){
                                hidingList[currentHiding].isVisible = false
                                currentHiding++
                            }else{
                                for(s in hidingList)
                                    s.isVisible = true; currentHiding = 0
                            }

                        }

                    }

                    return true
                }

            }
            opgl.layoutParams = binding.vMain.layoutParams

            binding.root.addView(opgl) }



        val view = binding.root

        return view
    }


    override fun onPause(){
        super.onPause()
        drawing = false
        thread.join()
    }

    override fun onResume() {
        super.onResume()
        drawing = true
        thread = Thread(this)
        thread.start()
    }


    override fun run() {
        opgl.lock.withLock {
            opgl.condition.await()
        }
        opgl.renderer.drawLock.withLock {
            opgl.renderer.drawCondition.await()
        }


        val chevalet = opgl.addSprite(300, 60, 0.0f, "chevalet.png", 2)

        val painting = opgl.addSprite(300 + 11*2, 60 + 118 * 2,
                                      300 + 20 * 2, 60 + 189 * 2,
                                        300 + 97 * 2, 60 + 90 *2,
                                        300 + 113 * 2, 60 +  171 * 2, 0.1f, "painting1.jpg")
        val hiding9 = opgl.addSprite(300 + 11*2, 60 + 118 * 2, 300 + 20 * 2, 60 + 189 * 2, 300 + 97 * 2, 60 + 90 *2, 300 + 113 * 2, 60 +  171 * 2, 0.11f, "canvasProgress/progress9.png")
        val hiding8 = opgl.addSprite(300 + 11*2, 60 + 118 * 2, 300 + 20 * 2, 60 + 189 * 2, 300 + 97 * 2, 60 + 90 *2, 300 + 113 * 2, 60 +  171 * 2, 0.12f, "canvasProgress/progress8.png")
        val hiding7 = opgl.addSprite(300 + 11*2, 60 + 118 * 2, 300 + 20 * 2, 60 + 189 * 2, 300 + 97 * 2, 60 + 90 *2, 300 + 113 * 2, 60 +  171 * 2, 0.13f, "canvasProgress/progress7.png")
        val hiding6 = opgl.addSprite(300 + 11*2, 60 + 118 * 2, 300 + 20 * 2, 60 + 189 * 2, 300 + 97 * 2, 60 + 90 *2, 300 + 113 * 2, 60 +  171 * 2, 0.14f, "canvasProgress/progress6.png")
        val hiding5 = opgl.addSprite(300 + 11*2, 60 + 118 * 2, 300 + 20 * 2, 60 + 189 * 2, 300 + 97 * 2, 60 + 90 *2, 300 + 113 * 2, 60 +  171 * 2, 0.15f, "canvasProgress/progress5.png")
        val hiding4 = opgl.addSprite(300 + 11*2, 60 + 118 * 2, 300 + 20 * 2, 60 + 189 * 2, 300 + 97 * 2, 60 + 90 *2, 300 + 113 * 2, 60 +  171 * 2, 0.16f, "canvasProgress/progress4.png")
        val hiding3 = opgl.addSprite(300 + 11*2, 60 + 118 * 2, 300 + 20 * 2, 60 + 189 * 2, 300 + 97 * 2, 60 + 90 *2, 300 + 113 * 2, 60 +  171 * 2, 0.17f, "canvasProgress/progress3.png")
        val hiding2 = opgl.addSprite(300 + 11*2, 60 + 118 * 2, 300 + 20 * 2, 60 + 189 * 2, 300 + 97 * 2, 60 + 90 *2, 300 + 113 * 2, 60 +  171 * 2, 0.18f, "canvasProgress/progress2.png")
        val hiding1 = opgl.addSprite(300 + 11*2, 60 + 118 * 2, 300 + 20 * 2, 60 + 189 * 2, 300 + 97 * 2, 60 + 90 *2, 300 + 113 * 2, 60 +  171 * 2, 0.19f, "canvasProgress/progress1.png")
        val hiding0 = opgl.addSprite(300 + 11*2, 60 + 118 * 2, 300 + 20 * 2, 60 + 189 * 2, 300 + 97 * 2, 60 + 90 *2, 300 + 113 * 2, 60 +  171 * 2, 0.2f, "canvasProgress/progress0.png")


        hidingList.add(hiding0)
        hidingList.add(hiding1)
        hidingList.add(hiding2)
        hidingList.add(hiding3)
        hidingList.add(hiding4)
        hidingList.add(hiding5)
        hidingList.add(hiding6)
        hidingList.add(hiding7)
        hidingList.add(hiding8)
        hidingList.add(hiding9)



                while(drawing){


            opgl.requestRender()
            opgl.renderer.drawLock.withLock {
                opgl.renderer.drawCondition.await()
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
         * @return A new instance of fragment ClickFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ClickFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}




