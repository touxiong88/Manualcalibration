precision mediump float;
varying highp vec2 vTextureCoord;//片元坐标
uniform sampler2D SamplerLR;//采样器
uniform sampler2D SamplerR;//一个采样器变量代表一幅或一套纹理贴图
uniform sampler2D SamplerG;
uniform sampler2D SamplerB;
uniform vec2 black;//二维向量


void main()
{
	vec4 supv4;
	supv4.r = texture2D(SamplerR,vTextureCoord).r;//根据接收的纹理坐标，调用texture2D内建函数从采样器中进行纹理采样，提取颜色得到此片元的r值
	supv4.g = texture2D(SamplerG,vTextureCoord).r;//根据接收的纹理坐标，调用texture2D内建函数从采样器中进行纹理采样，得到此片元的r值
	supv4.b = texture2D(SamplerB,vTextureCoord).r;//根据接收的纹理坐标，调用texture2D内建函数从采样器中进行纹理采样，得到此片元的r值
	supv4.a = 1.0;
	
	vec2 coordv2 = vTextureCoord;//纹理坐标 xy值
	//取左右图给当前片元颜色赋值
    coordv2.s = coordv2.s*0.5;//纹理坐标轴 s轴 t轴 p d
    vec4 colRv4 = texture2D(SamplerLR,coordv2);//右视图
    coordv2.s += 0.5;
    vec4 colLv4 = texture2D(SamplerLR,coordv2) ;//左视图

    gl_FragColor =(1.0-supv4)*colLv4 + supv4*colRv4;//根据采样器获取的像素RGB值给片元颜色赋值

    if(supv4.b != 0.0 && supv4.b != 1.0){//不是蓝色或全蓝

        gl_FragColor = gl_FragColor*(abs(supv4- 0.5));
	}
    //超过分辨率范围
    if( (black.x  >= vTextureCoord.x  &&  black.x  <= vTextureCoord.x+0.000520833*2.1)
    && ( black.y  >= vTextureCoord.y  &&  black.y  <= vTextureCoord.y+0.000925926*2.1)){
        gl_FragColor = vec4(1.0,1.0,0.0,1.0);//RGBA b=0?
         gl_FragColor = gl_FragColor*0.5;
    }

}




