# version 300 es

precision mediump float;

in vec2 textureCoords;

out vec4 fragColor;

uniform sampler2D celestialBody;
void main(){

	fragColor = texture(celestialBody, textureCoords);

}