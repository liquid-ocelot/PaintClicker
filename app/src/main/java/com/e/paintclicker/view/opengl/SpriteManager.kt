package com.e.paintclicker.view.opengl

import android.content.Context
import android.opengl.GLES20
import android.opengl.GLU
import android.util.Log
import java.util.*

class SpriteManager (val context: Context){

    private var mProgram: Int
    private var positionHandle: Int = 0
    private var mColorHandle: Int = 0
    private var mBaseMapLoc = 0

    private val vertexShaderCode =
            "attribute vec4 vPosition;" +
                    "attribute vec2 texCoord;" +
                    "" +
                    "varying vec2 v_texCoord;" +
                    "void main() {" +
                    "  gl_Position = vPosition;" +
                    "v_texCoord = texCoord;" +
                    "}"

    private val fragmentShaderCode =
            "precision mediump float;" +
                    "" +
                    "varying vec2 v_texCoord;" +
                    "uniform sampler2D s_texture;" +
                    "void main() {" +
                    "  gl_FragColor = texture2D(s_texture, v_texCoord);" +
                    "}"

//    companion object {
//        @Volatile
//        private var INSTANCE: SpriteManager? = null
//        fun getInstance(context: Context) =
//                INSTANCE ?: synchronized(this) {
//                    INSTANCE ?: SpriteManager(context).also {
//                        INSTANCE = it
//                    }
//                }
//    }


    init{

        val vertexShader: Int = loadShader(GLES20.GL_VERTEX_SHADER, vertexShaderCode)
        val fragmentShader: Int = loadShader(GLES20.GL_FRAGMENT_SHADER, fragmentShaderCode)

        // create empty OpenGL ES Program
        mProgram = GLES20.glCreateProgram().also {

            // add the vertex shader to program
            GLES20.glAttachShader(it, vertexShader)

            // add the fragment shader to program
            GLES20.glAttachShader(it, fragmentShader)

            // creates OpenGL ES program executables
            GLES20.glLinkProgram(it)

            var foundError = false
            var glErr = GLES20.glGetError()
            while(glErr != GLES20.GL_NO_ERROR){
                Log.i("glError", GLU.gluErrorString(glErr))
                foundError = true
                glErr = GLES20.glGetError()
            }

    }
    }

    var spriteList = Vector<Sprite>()


    public fun add(sprite: Sprite){
        synchronized(spriteList) {
            spriteList.addElement(sprite)
        }
    }

    public fun remove(sprite: Sprite){
        spriteList.remove(sprite)
    }

        fun loadShader(type: Int, shaderCode: String): Int {

            // create a vertex shader type (GLES20.GL_VERTEX_SHADER)
            // or a fragment shader type (GLES20.GL_FRAGMENT_SHADER)
            return GLES20.glCreateShader(type).also { shader ->

                // add the source code to the shader and compile it
                GLES20.glShaderSource(shader, shaderCode)
                GLES20.glCompileShader(shader)
                var foundError = false
                var glErr = GLES20.glGetError()
                while(glErr != GLES20.GL_NO_ERROR){
                    Log.i("glError", GLU.gluErrorString(glErr))
                    foundError = true
                    glErr = GLES20.glGetError()
                }
            }
        }

    fun draw() {
        // Add program to OpenGL ES environment
        GLES20.glUseProgram(mProgram)

        // get handle to vertex shader's vPosition member
        positionHandle = GLES20.glGetAttribLocation(mProgram, "vPosition").also {

            // Enable a handle to the triangle vertices
            GLES20.glEnableVertexAttribArray(it)

            val tCoordPos =  GLES20.glGetAttribLocation(mProgram, "texCoord")
            GLES20.glEnableVertexAttribArray(tCoordPos)

            for (s in spriteList) {

                if(!s.textureLoaded){
                    s.loadTextureFromAssets(context)
                }

                // Prepare the triangle coordinate data
                GLES20.glVertexAttribPointer(
                        it,
                        COORDS_PER_VERTEX,
                        GLES20.GL_FLOAT,
                        false,
                        s.vertexStride,
                        s.vertexBuffer
                )

                GLES20.glVertexAttribPointer(tCoordPos,2 , GLES20.GL_FLOAT, false, 8, s.textureCoordBuffer)


                GLES20.glEnableVertexAttribArray(0)

                GLES20.glActiveTexture(GLES20.GL_TEXTURE0)
                GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, s.mBaseMapTexId)

                GLES20.glUniform1i(mBaseMapLoc, 0)

                // Draw the triangle
                GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP, 0, s.vertexCount)
            }

            // Disable vertex array
            GLES20.glDisableVertexAttribArray(it)
        }
    }


}