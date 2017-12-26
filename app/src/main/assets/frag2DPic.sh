precision mediump float;
varying highp vec2 vTextureCoord;
uniform sampler2D Sampler0;
void main()
{
	gl_FragColor = texture2D(Sampler0,vTextureCoord);
}




