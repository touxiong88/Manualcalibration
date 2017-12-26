package com.stereo.ManualCalib.myGLSurfaceView;

import android.opengl.GLES10;
import android.opengl.GLES20;

import com.stereo.ManualCalib.LogUtils;
import com.stereo.ManualCalib.GlobalVariable;
import com.stereo.ManualCalib.glutils.myGLUtils;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import static com.stereo.ManualCalib.myGLSurfaceView.ShaderUtil.createProgram;

public class Render3D {
    private static final String TAG = "Render3D";
    private int mProgram;
    private int maPositionHandle;
    private int maTextureHandle;
    
    private int mSamplerLR;
    private int mSamplerR;
    private int mSamplerG;
    private int mSamplerB;
    
    private int muBlack;//////////////////

    int    posTextureR;
    int    posTextureG;
    int    posTextureB;
    
    public int  cDelt = 17;
    
    String mVertexShader;
    String mFragmentShader;
    
    private FloatBuffer mVertices;//矩阵buffer声明
    
    int mWidth ;
    int mHeight;
    
    private static final int FLOAT_SIZE_BYTES = 4;
    private static final int VERTICES_DATA_STRIDE_BYTES = 5 * FLOAT_SIZE_BYTES;
    private static final int VERTICES_DATA_POS_OFFSET = 0;
    private static final int VERTICES_DATA_UV_OFFSET = 3;
    private final float[] mVerticesData = {//顶点起始位置 纹理起始位置
            // X, Y, Z, U, V
            -1.0f, -1.0f, 0, 0.f, 0.f,
             1.0f, -1.0f, 0, 1.f, 0.f,
            -1.0f, 1.0f, 0, 0.f, 1.f,
            
            -1.0f,  1.0f, 0, 0.f, 1.f,
            1.0f,   -1.0f, 0, 1.f, 0.f,
            1.0f,   1.0f, 0, 1.f, 1.f,};

    public Render3D(MyGLSurfaceView mv,int w,int h){
    	LogUtils.d(TAG+"Render2D");
    	mWidth = w;
    	mHeight = h;
    	
    	initVertexData();
    	initShader(mv);

    	posTextureR = ShaderUtil.initTexture();
    	posTextureG = ShaderUtil.initTexture();
    	posTextureB = ShaderUtil.initTexture();

    	updateDelt(cDelt);
    }

    public void initVertexData()
    {
    	LogUtils.d(TAG+"initVertexData");
        mVertices = ByteBuffer.allocateDirect(mVerticesData.length
                * FLOAT_SIZE_BYTES).order(ByteOrder.nativeOrder()).asFloatBuffer();//申请buffer
        mVertices.put(mVerticesData).position(0);//将矩阵数据放入buffer
         
    }
    public void initShader(MyGLSurfaceView mv)
    {
    	LogUtils.d(TAG+"initShader");
        mVertexShader=ShaderUtil.loadFromAssetsFile("vertex3D.sh", mv.getResources());//加载定点着色器
        mFragmentShader=ShaderUtil.loadFromAssetsFile("frag3D.sh", mv.getResources());  //加载片元着色器
        mProgram = createProgram(mVertexShader, mFragmentShader);//生成着色程序
        maPositionHandle = GLES20.glGetAttribLocation(mProgram, "aPosition");//获取程序中顶点位置属性引用
        if (maPositionHandle == -1) {
        	throw new RuntimeException("Could not get attrib location for aPosition");
        }
        maTextureHandle = GLES20.glGetAttribLocation(mProgram, "aTextureCoord");//获取纹理坐标的引用,对应输入二维图像上的点，每一个点由2个float值标识坐标，且与mVerticesBuffer中的顶点按顺序一一对应
        checkGlError("glGetAttribLocation aTextureCoord");
        if (maTextureHandle == -1) {
        	throw new RuntimeException("Could not get attrib location for aTextureCoord");
        }
           
    	mSamplerLR = GLES20.glGetUniformLocation(mProgram, "SamplerLR");//采样器0的引用
        if (mSamplerLR == -1) {
        	throw new RuntimeException("Could not get attrib location for mSamplerLR");
    	}
        
    	mSamplerR = GLES20.glGetUniformLocation(mProgram, "SamplerR");//采样器1的引用
        if (mSamplerR == -1) {
        	throw new RuntimeException("Could not get attrib location for mSamplerR");
    	}
        
    	mSamplerG = GLES20.glGetUniformLocation(mProgram, "SamplerG");
        if (mSamplerG == -1) {
        	throw new RuntimeException("Could not get attrib location for mSamplerG");//采样器2的引用
    	}
        
    	mSamplerB = GLES20.glGetUniformLocation(mProgram, "SamplerB");
        if (mSamplerB == -1) {
        	throw new RuntimeException("Could not get attrib location for mSamplerB");
    	}
        
        muBlack = GLES20.glGetUniformLocation(mProgram, "black");//向量vec2 black; black的引用
        if (muBlack == -1) {
        	throw new RuntimeException("Could not get attrib location for muBlack");
    	}
    }
    public void drawSelf(int texId)
    {
       GLES20.glClearColor(0.f, 0.f, 0.f, 1.0f);//清除颜色缓冲区
       GLES20.glUseProgram(mProgram);//加载并使用连接好shader的程序
       checkGlError("glUseProgram");//检查错误
       GLES20.glClear( GLES20.GL_DEPTH_BUFFER_BIT | GLES20.GL_COLOR_BUFFER_BIT);//清除缓冲标志位

       updateDelt(cDelt);//重新生成纹理矩阵posTextureR posTextureG posTextureB并激活

       mVertices.position(VERTICES_DATA_POS_OFFSET);//偏移buffer
        //指定了渲染时索引值为 index 的顶点属性数组的数据格式和位置 指定顶点属性及三个元素的指针起点
        // 为画笔指定顶点位置数据(vPosition)
       GLES20.glVertexAttribPointer(maPositionHandle, 3, GLES20.GL_FLOAT, false,
               VERTICES_DATA_STRIDE_BYTES, mVertices);//顶点起始坐标
       GLES20.glEnableVertexAttribArray(maPositionHandle);//以顶点属性位置值作为参数，启用顶点属性；
       checkGlError("glEnableVertexAttribArray maPositionHandle2");
        //指定了渲染时索引值为 index 的顶点属性数组的数据格式和位置 指定顶点属性及三个元素的指针起点
       mVertices.position(VERTICES_DATA_UV_OFFSET);//纹理起始坐标
       GLES20.glVertexAttribPointer(maTextureHandle, 3, GLES20.GL_FLOAT, false,
               VERTICES_DATA_STRIDE_BYTES, mVertices);

       checkGlError("glVertexAttribPointer maTextureHandle2");
       GLES20.glEnableVertexAttribArray(maTextureHandle);//以顶点属性位置值作为参数，启用顶点属性；
       checkGlError("glEnableVertexAttribArray maTextureHandle2");

       GLES20.glActiveTexture(GLES20.GL_TEXTURE0);//在绑定纹理之前，先激活纹理单元 //后面渲染的时候，设置4层纹理
       GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, texId);//绑定这个纹理到当前激活的纹理单元
       GLES20.glUniform1i(mSamplerLR, 0);//设置uniform采样器的位置或曰纹理单元。//对应纹理第一层

       GLES20.glActiveTexture(GLES20.GL_TEXTURE1);//在绑定纹理之前，先激活纹理单元 //后面渲染的时候，设置4层纹理
       GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, posTextureR);//绑定这个纹理到当前激活的纹理单元
       GLES20.glUniform1i(mSamplerR, 1);//设置uniform采样器的位置或曰纹理单元。//对应纹理第二层

       GLES20.glActiveTexture(GLES20.GL_TEXTURE2);//在绑定纹理之前，先激活纹理单元 //后面渲染的时候，设置4层纹理
       GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, posTextureG);//绑定这个纹理到当前激活的纹理单元
       GLES20.glUniform1i(mSamplerG, 2);//设置uniform采样器的位置或曰纹理单元。//对应纹理第三层


       GLES20.glActiveTexture(GLES20.GL_TEXTURE3);//在绑定纹理之前，先激活纹理单元 //后面渲染的时候，设置4层纹理
       GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, posTextureB);//绑定这个纹理到当前激活的纹理单元
       GLES20.glUniform1i(mSamplerB, 3);//设置uniform采样器的位置或曰纹理单元。//对应纹理第四层

       GLES20.glUniform2f(muBlack, blackx, blacky);//为当前程序对象指定Uniform变量的值 传递给顶点着色器 Camera预览放大到屏幕大小

       GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, 6);//绘制,根据顶点数组中的坐标数据和指定的模式，进行绘制
       checkGlError("glDrawArrays");
        }
    
    float blackx = 960.0f/1920.0f;
    float blacky =  540.0f/1080.0f;

    public  void  moveBlack(int x,int y){

    	blackx = x /1920.0f;
    	blacky = y /1080.0f;
    }
    private void checkGlError(String op) {
        int error;
        while ((error = GLES20.glGetError()) != GLES20.GL_NO_ERROR) {
            LogUtils.d(TAG+op + ": glError " + error);
            throw new RuntimeException(op + ": glError " + error);
        }
    }

    boolean pixle = true;
    private static int DEFAULT = 1;
    private static int RED = 1;
    private static int GREEN = 2;
    private static int BLUE = 3;

    private void  updateDelt(int mDelt){// mDelt=17重新生成纹理矩阵 2017-10-11 并激活RGB对应的纹理单元进行显示 2017-12-26
    	GLES10.glBindTexture(GLES10.GL_TEXTURE_2D, posTextureR);//绑定这个纹理到当前激活的纹理单元
    	myGLUtils.texImage2D(RED, mDelt, GlobalVariable.angle, GlobalVariable.dw, 0);//重新生成 Blue纹理
    	//glTexImage2D ( GL_TEXTURE_2D, 0, GL_LUMINANCE, gWidth, gHeight, 0, GL_LUMINANCE, GL_UNSIGNED_BYTE, bufBlue); //cpp重新生成纹理
    	GLES10.glBindTexture(GLES10.GL_TEXTURE_2D, posTextureG);//绑定这个纹理到当前激活的纹理单元
    	if(pixle){
    		myGLUtils.texImage2D(GREEN, mDelt, GlobalVariable.angle, GlobalVariable.dw, 0);//重新生成 Green纹理
    	}else{
    		myGLUtils.texImage2D(DEFAULT, mDelt, GlobalVariable.angle, GlobalVariable.dw, 0); //2D纹理
    		
    	}
    	GLES10.glBindTexture(GLES10.GL_TEXTURE_2D, posTextureB);//绑定这个纹理到当前激活的纹理单元
    	if(pixle){
    		myGLUtils.texImage2D(BLUE, mDelt, GlobalVariable.angle, GlobalVariable.dw, 0);//重新生成 Red纹理
    	}else{
    		myGLUtils.texImage2D(DEFAULT, mDelt, GlobalVariable.angle, GlobalVariable.dw, 0);//2D纹理
    	}
    }


	public void addDelts( int n) {
		// TODO Auto-generated method stub
		cDelt = n;
	}

    public void  finalize(){
        // 释放shader资源
        //GLES20.glDeleteShader(vertexShader );
        //GLES20.glDeleteShader(pixelShader);
    }
}
