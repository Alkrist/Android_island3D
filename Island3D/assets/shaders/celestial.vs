# version 300 es

in vec2 position;

out vec2 textureCoords;

uniform mat4 projectionMatrix;
uniform mat4 modelViewMatrix;

void main(){

	textureCoords = vec2((position.x+1.0)/2.0, 1.0 - (position.y+1.0)/2.0);
	
	gl_Position = projectionMatrix * modelViewMatrix * vec4(position, 0.0, 1.0);

}