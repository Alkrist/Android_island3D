# version 300 es

in vec2 position;

out vec2 textureCoords;
out vec3 toCameraVector;
out vec3 fromLightVector;
out vec4 clipSpace;

//Fog
out float visibility;

uniform mat4 projectionMatrix;
uniform mat4 viewMatrix;
uniform mat4 modelMatrix;

//Light
uniform vec3 cameraPosition;
uniform vec3 lightPosition;

//Fog
uniform float density;
uniform float gradient;

const float tiling = 6.0;

void main() {
	
	vec4 worldPosition = modelMatrix * vec4(position.x, 0.0, position.y, 1.0);
	
	vec4 clipSpace = projectionMatrix * viewMatrix * worldPosition;
	
	gl_Position = clipSpace;
	textureCoords = vec2(position.x/2.0 + 0.5, position.y/2.0 + 0.5) * tiling;
 	
 	// **** LIGHT ****
 	toCameraVector = cameraPosition - worldPosition.xyz;
	fromLightVector = worldPosition.xyz - lightPosition;
	
	// *** FOG ***
	vec4 positionRelativeToCam = viewMatrix * worldPosition;
	float distance = length(positionRelativeToCam.xyz);
  	visibility = exp(-pow((distance*density), gradient));
  	visibility = clamp(visibility, 0.0, 1.0);
}