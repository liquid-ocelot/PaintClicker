package com.e.paintclicker.view.opengl

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.opengl.GLES20
import android.opengl.GLSurfaceView
import java.io.IOException
import java.io.InputStream
import javax.microedition.khronos.opengles.GL

class SpriteFactory(val context: Context, val spriteManager: SpriteManager, val screenWidth: Int, val screenHeight: Int) {

    private val textureCoords = floatArrayOf(0.0f, 1.0f,     // top left     (V2)
                                             0.0f, 0.0f,     // bottom left  (V1)
                                             1.0f, 1.0f,     // top right    (V4)
                                             1.0f, 0.0f )


        fun sprite(x: Int, y: Int, layer_z: Float, textureFile: String): Sprite {


            //load texture
            var bitmap: Bitmap? = null
            var ins: InputStream? = null


            ins = context.assets.open(textureFile)


            bitmap = BitmapFactory.decodeStream(ins)


            // set up vertices coords
            val x0 = (2.0f * x / screenWidth) - 1 //upper left
            val y0 = (2.0f * y / screenHeight) - 1

            val x1= (2.0f * x / screenWidth) - 1     //down left
            val y1 = (2.0f * (y + bitmap.height) / screenHeight) - 1

            val x2 = (2.0f * (x + bitmap.width)/screenWidth) - 1 //upper right
            val y2 = (2.0f * y / screenHeight) - 1

            val x3 = (2.0f * (x + bitmap.width)/screenWidth) - 1 //down right
            val y3 = (2.0f * (y + bitmap.height) / screenHeight) - 1

            val rSprite = Sprite()
            rSprite.x = x
            rSprite.y = y
            rSprite.layer_z = layer_z

            rSprite.triangleCoords = floatArrayOf(x0, y0, layer_z,
                                                    x1, y1, layer_z,
                                                    x2, y2, layer_z,
                                                    x3, y3, layer_z)

//            rSprite.triangleCoords = floatArrayOf(     // in counterclockwise order:
//                          // top
//                    0.0f, 0.0f, 0.0f,
//                    0.0f, 1.0f, 0.0f ,// top
//                    1.0f, 0.0f, 0.0f,    // bottom left
//                    1.0f, 1.0f, 0.0f
//            )

            rSprite.textureCoords = textureCoords

            //rSprite.loadTexture(bitmap)
            rSprite.textureName = textureFile

            spriteManager.add(rSprite)

            return rSprite


        }



}