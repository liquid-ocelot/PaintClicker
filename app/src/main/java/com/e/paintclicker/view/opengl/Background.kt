package com.e.paintclicker.view.opengl

import android.content.Context
import android.opengl.GLES20
import android.opengl.GLU
import android.util.Log

class Background (val context: Context){

    private var mProgram: Int
    private var positionHandle: Int = 0
    private var mColorHandle: Int = 0
    private var mBaseMapLoc = 0

    private lateinit var sprite: Sprite

    private val textureCoords = floatArrayOf(0.0f, 1.0f,     // top left     (V2)
            0.0f, 0.0f,     // bottom left  (V1)
            1.0f, 1.0f,     // top right    (V4)
            1.0f, 0.0f )

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

        sprite = setUpBackground("background2.png")




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

                if(!sprite.textureLoaded){
                    sprite.loadTextureFromAssets(context)
                }

                // Prepare the triangle coordinate data
                GLES20.glVertexAttribPointer(
                        it,
                        COORDS_PER_VERTEX,
                        GLES20.GL_FLOAT,
                        false,
                        sprite.vertexStride,
                        sprite.vertexBuffer
                )

                GLES20.glVertexAttribPointer(tCoordPos,2 , GLES20.GL_FLOAT, false, 8, sprite.textureCoordBuffer)


                GLES20.glEnableVertexAttribArray(0)

                GLES20.glActiveTexture(GLES20.GL_TEXTURE0)
                GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, sprite.mBaseMapTexId)

                GLES20.glUniform1i(mBaseMapLoc, 0)

                // Draw the triangle
                GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP, 0, sprite.vertexCount)


            // Disable vertex array
            GLES20.glDisableVertexAttribArray(it)
        }
    }


    fun setUpBackground(textureFile:String): Sprite{

        val layer_z = -1.0f

        val x0 =  - 1.0f //upper left
        val y0 =  - 1.0f

        val x1= - 1.0f     //down left
        val y1 =  1.0f

        val x2 =  1.0f //upper right
        val y2 = - 1.0f

        val x3 =   1.0f //down right
        val y3 =  1.0f

        val rSprite = Sprite()
        rSprite.layer_z = layer_z

        rSprite.triangleCoords = floatArrayOf(x0, y0, layer_z,
                x1, y1, layer_z,
                x2, y2, layer_z,
                x3, y3, layer_z)



        rSprite.textureCoords = textureCoords


        rSprite.textureName = textureFile



        return rSprite

    }

    fun changeBackground(filename:String){
        sprite.textureLoaded = false
        sprite.textureName = filename
    }



}