# version 300 es

in vec3 in_position;

uniform mat4 mvpMatrix;

void main(){

	gl_Position = mvpMatrix * vec4(in_position, 1.0);

}