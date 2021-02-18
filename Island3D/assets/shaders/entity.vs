# version 300 es

in vec4 position;
in vec2 textureCoords;
in vec3 normal;

//Texture
out vec2 pass_TextureCoords;

//Light
out vec3 surfaceNormal;
out vec3 toLightVector;
out vec3 toCameraVector;

//Fog
out float visibility;

uniform mat4 transformationMatrix;
uniform mat4 projectionMatrix;
uniform mat4 viewMatrix;
uniform mat4 invertedViewMatrix;
uniform vec3 lightPosition;

uniform float density;
uniform float gradient;

void main() {

	vec4 worldPosition = transformationMatrix * position;
	vec4 positionRelativeToCam = viewMatrix * worldPosition;
 	gl_Position = projectionMatrix * positionRelativeToCam;
  	pass_TextureCoords = textureCoords;
  
    //**** Light ****
  	surfaceNormal = (transformationMatrix * vec4(normal, 0.0)).xyz;
  	toLightVector = lightPosition - worldPosition.xyz;
  	toCameraVector = (invertedViewMatrix * vec4(0.0, 0.0, 0.0, 1.0)).xyz - worldPosition.xyz;
  	
  	//**** Fog ****
  	float distance = length(positionRelativeToCam.xyz);
  	visibility = exp(-pow((distance*density), gradient));
  	visibility = clamp(visibility, 0.0, 1.0);
}