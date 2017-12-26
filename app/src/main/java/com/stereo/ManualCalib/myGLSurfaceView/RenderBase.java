package com.stereo.ManualCalib.myGLSurfaceView;

import android.graphics.SurfaceTexture;
import android.opengl.GLES20;

import com.stereo.ManualCalib.LogUtils;

import java.nio.FloatBuffer;

public class RenderBase {
	private static final String TAG = "RenderBase";
	public  final int GL_TEXTURE_EXTERNAL_OES = 0x8D65;
	public int mTextureID;
	public SurfaceTexture mSurface;
    public static final int FLOAT_SIZE_BYTES = 4;
    public static final int VERTICES_DATA_STRIDE_BYTES = 5 * FLOAT_SIZE_BYTES;
    public static final int VERTICES_DATA_POS_OFFSET = 0;
    public static final int VERTICES_DATA_UV_OFFSET = 3;
    
    
	public FloatBuffer mVerticesFull;
    public FloatBuffer mVerticesLeft;
    public FloatBuffer mCurrentVertices;
	
    public int mProgram;
    public int muSTMatrixHandle;
    public int maPositionHandle;
    public int maTextureHandle;
    public int  mFrameBuffer;
    public int mCurrentFBO = 0;
    String mVertexShader;
    String mFragmentShader;
	FloatBuffer   mVertexBuffer;
	FloatBuffer   mTexCoorBuffer;
    
	public float[] mSTMatrix = {
			1,0,0,0,
			0,1,0,0,
			0,0,1,0,
			0,0,0,1
	};
	
	public float[] mIdeMatrix = {
			1,0,0,0,
			0,1,0,0,
			0,0,1,0,
			0,0,0,1
	};
	
	public float[] mTestMatrix = {
			1,0,0,0,
			0,1,0,0,
			0,0,1,0,
			0,0,0,1
	};
	
	public RenderBase(){
		
	}

	public void initTexture() {

        int[] textures = new int[1];
        GLES20.glGenTextures(1, textures, 0);

        mTextureID = textures[0];
        GLES20.glBindTexture(GL_TEXTURE_EXTERNAL_OES, mTextureID);
  //      checkGlError("glBindTexture mTextureID");

        // Can't do mipmapping with camera source
        GLES20.glTexParameterf(GL_TEXTURE_EXTERNAL_OES, GLES20.GL_TEXTURE_MIN_FILTER,
                GLES20.GL_LINEAR);
        GLES20.glTexParameterf(GL_TEXTURE_EXTERNAL_OES, GLES20.GL_TEXTURE_MAG_FILTER,
                GLES20.GL_LINEAR);
        // Clamp to edge is the only option
        GLES20.glTexParameteri(GL_TEXTURE_EXTERNAL_OES, GLES20.GL_TEXTURE_WRAP_S,
                GLES20.GL_CLAMP_TO_EDGE);
        GLES20.glTexParameteri(GL_TEXTURE_EXTERNAL_OES, GLES20.GL_TEXTURE_WRAP_T,
                GLES20.GL_CLAMP_TO_EDGE);
 //      checkGlError("glTexParameteri mTextureID");
	}
	
    public void setTransformMatrix(float[] mtx){
    	mSTMatrix = mtx;
    }
	public int getTexture() {
		// TODO Auto-generated method stub
		return mTextureID;
	}
	
	public void checkGlError(String op) {
        int error;
        while ((error = GLES20.glGetError()) != GLES20.GL_NO_ERROR) {
            LogUtils.d(TAG+op + ": glError " + error);
            throw new RuntimeException(op + ": glError " + error);
        }
    }
	
	public SurfaceTexture getSurfaceTexture() {
		// TODO Auto-generated method stub
	//	LogUtils.d(TAG+"getSurfaceTexture"+this);
		return mSurface;
	}

	public void updateSurface() {
		// TODO Auto-generated method stub
		
	}

	public void drawSelf() {
		// TODO Auto-generated method stub
		
	}

	public void setDelt(int n) {
		// TODO Auto-generated method stub
		
	}
	
}
