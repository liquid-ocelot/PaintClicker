package com.e.paintclicker.view

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.e.paintclicker.databinding.FragmentClickBinding
import com.e.paintclicker.view.opengl.OpenglCanvas
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


            opgl = OpenglCanvas(it)
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


        Log.i("opengl", "test")

        opgl.addSprite(300,0,0.0f, "t1.png")




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