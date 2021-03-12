package com.e.paintclicker.view.opengl

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.opengl.GLES20
import android.opengl.GLUtils
import java.io.IOException
import java.io.InputStream
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.FloatBuffer

class Sprite {

    var mBaseMapTexId = 0
    var textureLoaded = false
    var textureName = ""

    val COORDS_PER_VERTEX = 3

    var triangleCoords: FloatArray? = null
        get() = field
        set(value) {
            field = value

            vertexBuffer=
            // (number of coordinate values * 4 bytes per float)
            ByteBuffer.allocateDirect(triangleCoords!!.size * 4).run {
                // use the device hardware's native byte order
                order(ByteOrder.nativeOrder())

                // create a floating point buffer from the ByteBuffer
                asFloatBuffer().apply {
                    // add the coordinates to the FloatBuffer
                    put(triangleCoords)
                    // set the buffer to read the first coordinate
                    position(0)
                }
            }
        }

    var textureCoords: FloatArray? = null
        get() = field
        set(value) {
            field = value

            textureCoordBuffer=
                    // (number of coordinate values * 4 bytes per float)
                    ByteBuffer.allocateDirect(textureCoords!!.size * 4).run {
                        // use the device hardware's native byte order
                        order(ByteOrder.nativeOrder())

                        // create a floating point buffer from the ByteBuffer
                        asFloatBuffer().apply {
                            // add the coordinates to the FloatBuffer
                            put(textureCoords)
                            // set the buffer to read the first coordinate
                            position(0)
                        }
                    }
        }




    val vertexCount: Int = 4
    val vertexStride: Int = COORDS_PER_VERTEX * 4

     var x = 0
     var y = 0
     var layer_z = 0.0f

    var vertexBuffer: FloatBuffer? = null
    var textureCoordBuffer: FloatBuffer? = null







    private fun loadTexture(fileName:String): Int{
        val textureId = IntArray(1)
        var bitmap: Bitmap? = null
        var ins: InputStream? = null

        try {



            //ins = Context.assets.open(fileName)
        }catch (ioe: IOException)
        {
            ins = null
        }

        if(ins == null)
            return 0

        bitmap = BitmapFactory.decodeStream(ins)

        GLES20.glGenTextures ( 1, textureId, 0 )
        GLES20.glBindTexture ( GLES20.GL_TEXTURE_2D, textureId[0] )

        GLUtils.texImage2D ( GLES20.GL_TEXTURE_2D, 0, bitmap, 0 );

        GLES20.glTexParameteri ( GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_LINEAR )
        GLES20.glTexParameteri ( GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR )
        GLES20.glTexParameteri ( GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_S, GLES20.GL_REPEAT )
        GLES20.glTexParameteri ( GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_T, GLES20.GL_REPEAT )

        return textureId[0]


    }

    public fun loadTexture(bitmap: Bitmap){
        val textureId = IntArray(1)

        GLES20.glGenTextures ( 1, textureId, 0 )
        GLES20.glBindTexture ( GLES20.GL_TEXTURE_2D, textureId[0] )

        GLUtils.texImage2D ( GLES20.GL_TEXTURE_2D, 0, bitmap, 0 );

        GLES20.glTexParameteri ( GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_LINEAR )
        GLES20.glTexParameteri ( GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR )
        GLES20.glTexParameteri ( GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_S, GLES20.GL_REPEAT )
        GLES20.glTexParameteri ( GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_T, GLES20.GL_REPEAT )

        mBaseMapTexId = textureId[0]
        textureLoaded = true

    }

    public fun loadTextureFromAssets(context: Context){
        var bitmap: Bitmap? = null
        var ins: InputStream? = null

        ins = context.assets.open(textureName)

        bitmap = BitmapFactory.decodeStream(ins)

        loadTexture(bitmap)
    }
}