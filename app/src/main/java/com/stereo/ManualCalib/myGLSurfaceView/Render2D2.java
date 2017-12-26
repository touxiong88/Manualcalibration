package com.stereo.ManualCalib.myGLSurfaceView;

import android.opengl.GLES20;

import com.stereo.ManualCalib.LogUtils;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import static com.stereo.ManualCalib.myGLSurfaceView.ShaderUtil.createProgram;

public class Render2D2 {
    private static final String TAG = "Render2D2";
    private int mProgram;
    private int maPositionHandle;
    private int maTextureHandle;
    
    private int muSapler0;
    
    String mVertexShader;
    String mFragmentShader;

    
    private FloatBuffer mVertices;
    
    private static final int FLOAT_SIZE_BYTES = 4;
    private static final int VERTICES_DATA_STRIDE_BYTES = 5 * FLOAT_SIZE_BYTES;
    private static final int VERTICES_DATA_POS_OFFSET = 0;
    private static final int VERTICES_DATA_UV_OFFSET = 3;
    private final float[] mVerticesData = {
            // X, Y, Z, U, V
            -1.0f, -1.0f, 0, 0.f, 1.f,
             1.0f, -1.0f, 0, 1.f, 1.f,
            -1.0f, 1.0f, 0, 0.f, 0.f,
            
            -1.0f,  1.0f, 0, 0.f, 0.f,
            1.0f,   -1.0f, 0, 1.f, 1.f,
            1.0f,   1.0f, 0, 1.f, 0.f,};
    
    int vCount=6;   
    public Render2D2(MyGLSurfaceView mv,int w,int h){
    	LogUtils.d(TAG+"Render2D");
    	initVertexData();
    	initShader(mv);

    }
	

    public void initVertexData()
    {
    	LogUtils.d(TAG+"initVertexData");
        mVertices = ByteBuffer.allocateDirect(mVerticesData.length
                * FLOAT_SIZE_BYTES).order(ByteOrder.nativeOrder()).asFloatBuffer();
        mVertices.put(mVerticesData).position(0);
         
    }
    public void initShader(MyGLSurfaceView mv)
    {
    	LogUtils.d(TAG+"initShader");
        mVertexShader=ShaderUtil.loadFromAssetsFile("vertex2DPic.sh", mv.getResources());
        mFragmentShader=ShaderUtil.loadFromAssetsFile("frag2DPic.sh", mv.getResources());  
        mProgram = createProgram(mVertexShader, mFragmentShader);
        maPositionHandle = GLES20.glGetAttribLocation(mProgram, "aPosition");
        if (maPositionHandle == -1) {
        	throw new RuntimeException("Could not get attrib location for aPosition");
        }
        maTextureHandle = GLES20.glGetAttribLocation(mProgram, "aTextureCoord");
        checkGlError("glGetAttribLocation aTextureCoord");
        if (maTextureHandle == -1) {
        	throw new RuntimeException("Could not get attrib location for aTextureCoord");
        }
           
    	muSapler0 = GLES20.glGetUniformLocation(mProgram, "Sampler0");
        if (muSapler0 == -1) {
        	throw new RuntimeException("Could not get attrib location for muSapler0");
    	}
    }
    public void drawSelf(int texId)
    {        
    	
 //  	 GLES20.glClearColor(0.f, 0.f, 0.f, 1.0f);
   	 GLES20.glUseProgram(mProgram);
       checkGlError("glUseProgram");
//       GLES20.glClear( GLES20.GL_DEPTH_BUFFER_BIT | GLES20.GL_COLOR_BUFFER_BIT);
   	
       
       mVertices.position(VERTICES_DATA_POS_OFFSET);
       GLES20.glVertexAttribPointer(maPositionHandle, 3, GLES20.GL_FLOAT, false,
               VERTICES_DATA_STRIDE_BYTES, mVertices);
       GLES20.glEnableVertexAttribArray(maPositionHandle);
       checkGlError("glEnableVertexAttribArray maPositionHandle2");

       mVertices.position(VERTICES_DATA_UV_OFFSET);
       GLES20.glVertexAttribPointer(maTextureHandle, 3, GLES20.GL_FLOAT, false,
               VERTICES_DATA_STRIDE_BYTES, mVertices);
       
       checkGlError("glVertexAttribPointer maTextureHandle2");
       GLES20.glEnableVertexAttribArray(maTextureHandle);
       checkGlError("glEnableVertexAttribArray maTextureHandle2");
       
       GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
       GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, texId);
       GLES20.glUniform1i(muSapler0, 0);


       GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, 6);
       checkGlError("glDrawArrays");
    }
    
    private void checkGlError(String op) {
        int error;
        while ((error = GLES20.glGetError()) != GLES20.GL_NO_ERROR) {
            LogUtils.d(TAG+op + ": glError " + error);
            throw new RuntimeException(op + ": glError " + error);
        }
    }
}
