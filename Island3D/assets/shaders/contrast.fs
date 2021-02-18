# version 300 es

precision mediump float;

in vec2 textureCoords;

out vec4 fragColour;

uniform sampler2D colourTexture;
uniform float contrast;

void main(){

	fragColour = texture(colourTexture, textureCoords);
	fragColour.rgb = (fragColour.rgb - 0.5) * (1.0 + contrast) + 0.5;
}