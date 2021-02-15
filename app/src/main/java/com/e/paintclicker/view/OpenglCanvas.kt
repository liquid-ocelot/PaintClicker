package com.e.paintclicker.view

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.opengl.GLES20
import android.opengl.GLSurfaceView
import android.opengl.GLU
import android.opengl.GLUtils
import android.util.Log
import java.io.IOException
import java.io.InputStream
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.FloatBuffer
import javax.microedition.khronos.opengles.GL10

class OpenglCanvas(context: Context) : GLSurfaceView(context) {

    private val renderer: MyGLRenderer

    init {

        // Create an OpenGL ES 2.0 context
        setEGLContextClientVersion(2)

        renderer = MyGLRenderer(context)

        // Set the Renderer for drawing on the GLSurfaceView
        setRenderer(renderer)
    }


}

class MyGLRenderer(val context: Context) : GLSurfaceView.Renderer {

    private lateinit var mTriangle: Triangle






    override fun onDrawFrame(unused: GL10) {
        // Redraw background color
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT)
        mTriangle.draw()
    }

    override fun onSurfaceChanged(unused: GL10, width: Int, height: Int) {
        GLES20.glViewport(0, 0, width, height)
    }

    override fun onSurfaceCreated(gl: GL10?, config: javax.microedition.khronos.egl.EGLConfig?) {
        GLES20.glClearColor(0.0f, 0.0f, 0.0f, 1.0f)
        // initialize a triangle
        mTriangle = Triangle(context)

    }
}


// number of coordinates per vertex in this array
const val COORDS_PER_VERTEX = 3
/*var triangleCoords = floatArrayOf(     // in counterclockwise order:
        0.0f, 0.622008459f, 0.0f,      // top
        -0.5f, -0.311004243f, 0.0f,    // bottom left
        0.5f, -0.311004243f, 0.0f      // bottom right
)*/


var triangleCoords = floatArrayOf(     // in counterclockwise order:
        -1.0f, 0.0f, 0.0f,      // top
        -1.0f, 0.711004243f, 0.0f,    // bottom left
        1.0f, 0.711004243f, 0.0f      // bottom right
)

var textCoords = floatArrayOf(
        0.0f, 0.0f,
        1.0f, 0.0f,
        0.0f, 1.0f
)


class Triangle(val mContext:Context) {

    private var mProgram: Int
    private var mBaseMapLoc = 0
    private var mBaseMapTexId = 0

    private val vertexShaderCode =
            "attribute vec4 vPosition;" +
                    "" +
                    "varying vec2 v_texCoord;" +
                    "void main() {" +
                    "  gl_Position = vPosition;" +
                    "v_texCoord = vPosition.xy;" +
                    "}"

    private val fragmentShaderCode =
            "precision mediump float;" +
                    "uniform vec4 vColor;" +
                    "varying vec2 v_texCoord;" +
                    "uniform sampler2D s_texture;" +
                    "void main() {" +
                    "  gl_FragColor = texture2D(s_texture, v_texCoord);" +
                    "}"


    /*private val vertexShaderCode =
            "attribute vec4 vPosition;" +
                    "out vec2 v_texCoord;" +
                    "uniform sampler2D s_texture;" +
                    "void main() {" +
                    "  gl_Position = vPosition;" +
                    "v_texCoord = vPosition; " +
                    "}"

    private val fragmentShaderCode =
            "precision mediump float;" +
                    "uniform vec4 vColor;" +
                    "in vec2 v_texCoord;" +
                    "uniform sampler2D s_texture;" +
                    "void main() {" +
                    "  " +
                    "gl_FragColor = vColor;" +
                    "}"*/

//gl_FragColor = texture2D(s_texture, v_texCoord);

    init {

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

            mBaseMapLoc = GLES20.glGetUniformLocation ( it, "s_texture" )

            mBaseMapTexId = loadTexture("test2.bmp")
        }
    }



    // Set color with red, green, blue and alpha (opacity) values
    val color = floatArrayOf(0.63671875f, 0.76953125f, 0.22265625f, 1.0f)

    private var vertexBuffer: FloatBuffer =
            // (number of coordinate values * 4 bytes per float)
            ByteBuffer.allocateDirect(triangleCoords.size * 4).run {
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


    private var positionHandle: Int = 0
    private var mColorHandle: Int = 0

    private val vertexCount: Int = triangleCoords.size / COORDS_PER_VERTEX
    private val vertexStride: Int = COORDS_PER_VERTEX * 4 // 4 bytes per vertex

    fun draw() {
        // Add program to OpenGL ES environment
        GLES20.glUseProgram(mProgram)

        // get handle to vertex shader's vPosition member
        positionHandle = GLES20.glGetAttribLocation(mProgram, "vPosition").also {

            // Enable a handle to the triangle vertices
            GLES20.glEnableVertexAttribArray(it)

            // Prepare the triangle coordinate data
            GLES20.glVertexAttribPointer(
                    it,
                    COORDS_PER_VERTEX,
                    GLES20.GL_FLOAT,
                    false,
                    vertexStride,
                    vertexBuffer
            )

            // GLES20.glGetAttribLocation(mProgram, "")

            // get handle to fragment shader's vColor member
            mColorHandle = GLES20.glGetUniformLocation(mProgram, "vColor").also { colorHandle ->

                // Set color for drawing the triangle
                GLES20.glUniform4fv(colorHandle, 1, color, 0)
            }

            GLES20.glEnableVertexAttribArray ( 0 )

            GLES20.glActiveTexture(GLES20.GL_TEXTURE0)
            GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, mBaseMapTexId )

            GLES20.glUniform1i ( mBaseMapLoc, 0 )

            // Draw the triangle
            GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, vertexCount)

            // Disable vertex array
            GLES20.glDisableVertexAttribArray(it)
        }
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

    private fun loadTexture(fileName:String): Int{
        val textureId = IntArray(1)
        var bitmap: Bitmap? = null
        var ins: InputStream? = null

        try {
            ins = mContext.assets.open(fileName)
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


}