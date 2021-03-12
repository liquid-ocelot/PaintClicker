package com.e.paintclicker.view.opengl

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.opengl.GLES20
import android.opengl.GLSurfaceView
import android.opengl.GLU
import android.opengl.GLUtils
import android.util.Log
import android.view.View
import java.io.IOException
import java.io.InputStream
import java.lang.ref.WeakReference
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.FloatBuffer
import java.util.concurrent.locks.ReentrantLock
import javax.microedition.khronos.opengles.GL10
import kotlin.concurrent.withLock


const val COORDS_PER_VERTEX = 3



class OpenglCanvas(context: Context) : GLSurfaceView(context) {

    val renderer: MyGLRenderer
    val spriteFactory get() = renderer.spriteFactory

    val lock = ReentrantLock()
    val condition = lock.newCondition()

    fun addSprite(x: Int, y: Int, layer_z: Float, textureFile: String): Sprite{

            return spriteFactory.sprite(x, y, layer_z, textureFile)


    }


    init {

        // Create an OpenGL ES 2.0 context
        setEGLContextClientVersion(2)


        renderer = MyGLRenderer(context,WeakReference(this))





        // Set the Renderer for drawing on the GLSurfaceView
        setRenderer(renderer)
        renderMode = GLSurfaceView.RENDERMODE_WHEN_DIRTY
    }




}

class MyGLRenderer(val context: Context ,val view: WeakReference<OpenglCanvas>) : GLSurfaceView.Renderer {



    val screenWidth: Int? get() {return view.get()?.width }
    val screenHeight: Int? get() {return view.get()?.height}

    private var spriteManager: SpriteManager? = null
    private var _spriteFactory: SpriteFactory? = null
    val spriteFactory get() = _spriteFactory!!

    val drawLock = ReentrantLock()
    val drawCondition = drawLock.newCondition()





    override fun onDrawFrame(unused: GL10) {
        drawLock.withLock {
            // Redraw background color
            GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT)
            //GLES20.glClearColor(1.0f, 0.0f, 0.0f, 1.0f)
            GLES20.glEnable(GLES20.GL_BLEND);
            GLES20.glBlendFunc(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);
//draw your foreground scene here


            spriteManager!!.draw()
            GLES20.glDisable(GLES20.GL_BLEND);
            drawCondition.signal()
        }
    }

    override fun onSurfaceChanged(unused: GL10, width: Int, height: Int) {
        GLES20.glViewport(0, 0, width, height)
//        screenWidth = width
//        screenHeight = height
//        _spriteFactory = SpriteFactory(context, spriteManager, screenWidth, screenHeight)
    }



    override fun onSurfaceCreated(gl: GL10?, config: javax.microedition.khronos.egl.EGLConfig?) {
        GLES20.glClearColor(0.0f, 0.0f, 0.0f, 1.0f)


        spriteManager = SpriteManager(context)
        _spriteFactory = SpriteFactory(context, spriteManager!!, screenWidth!!, screenHeight!!)
        spriteFactory.sprite(0,0,0.0f, "t1.png")
        //spriteFactory.sprite(50,0,0.0f, "t1.png")
        //view.get()?.addSprite(150,0,0.0f, "t1.png")

        view.get()?.lock?.withLock {
            view.get()?.condition?.signal()
        }

    }
}






