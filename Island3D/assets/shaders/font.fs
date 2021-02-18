# version 300 es

precision mediump float;

in vec2 pass_textureCoords;

out vec4 fragColour;

uniform vec3 colour;
uniform sampler2D fontAtlas;

const float width = 0.5;
const float edge = 0.1;

void main(){

	float distance = 1.0 - texture(fontAtlas, pass_textureCoords).a;
	float alpha = 1.0 - smoothstep(width, width+edge, distance);
	
	fragColour = vec4(colour, alpha);

}