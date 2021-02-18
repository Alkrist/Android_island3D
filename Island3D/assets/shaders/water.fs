# version 300 es

precision mediump float;

in vec2 textureCoords;
in vec3 toCameraVector;
in vec3 fromLightVector;
in vec4 clipSpace;

//Fog
in float visibility;

out vec4 fragColor;

uniform float moveFactor;
uniform sampler2D dudvMap;
uniform sampler2D normalMap;
uniform sampler2D water1;
uniform sampler2D water2;

//Light
uniform vec3 lightColour;

//Fog
uniform vec3 skyColour1;
uniform vec3 skyColour2;
uniform float blendFactor;

const float waveStrength = 0.02;
const float shineDamper = 20.0;
const float reflectivity = 0.8;
 
void main() {

	vec4 colour1 = vec4(0.0, 0.0, 1.0, 1.0);
	vec4 colour2 = vec4(0.5, 0.5, 1.0, 1.0);
	
	
	//Texture Coords on clipspace
	vec2 ndc = (clipSpace.xy / clipSpace.w)/2.0 + 0.5;
	vec2 refractTexCoords = vec2(ndc.x, ndc.y);
	vec2 reflectTexCoords = vec2(ndc.x, -ndc.y);
	
	
	//Total distortion on DUDV Map
	vec2 distortedTexCoords = texture(dudvMap, vec2(textureCoords.x + moveFactor, textureCoords.y)).rg * 0.1;
	distortedTexCoords = textureCoords + vec2(distortedTexCoords.x, distortedTexCoords.y + moveFactor);
	vec2 totalDistortion = (texture(dudvMap, distortedTexCoords).rg * 2.0 - 1.0) * waveStrength;
	
	
	//Texture coords
	refractTexCoords += totalDistortion;
	refractTexCoords = clamp(refractTexCoords, 0.001, 0.999);
	
	reflectTexCoords += totalDistortion;
	reflectTexCoords.x = clamp(reflectTexCoords.x, 0.001, 0.999);
	reflectTexCoords.y = clamp(reflectTexCoords.y, -0.999, -0.001);
	
	vec4 reflectColour = texture(water1, reflectTexCoords);
	vec4 refractColour = texture(water2, refractTexCoords);
	
	
	//Light
	vec4 normalMapColour = texture(normalMap, distortedTexCoords);
	vec3 normal = vec3(normalMapColour.r * 2.0  - 1.0, normalMapColour.b * 2.0, normalMapColour.g * 2.0 - 1.0);
	normal = normalize(normal);
	
	vec3 viewVector = normalize(toCameraVector);
	float refractiveFactor = dot(viewVector, normal);
	refractiveFactor = pow(refractiveFactor, 2.0);
	
	vec3 reflectedLight = reflect(normalize(fromLightVector), normal);
	float specular = max(dot(reflectedLight, viewVector), 0.0);
	specular = pow(specular, shineDamper);
		
	vec3 specularHighlights = lightColour * specular * reflectivity;
	
	
	fragColor = (mix(reflectColour, refractColour, refractiveFactor))+vec4(specularHighlights, 0.0);
	
	
	//vec3 finalSkyColour = mix(skyColour1, skyColour2, blendFactor);
	//fragColor = mix(vec4(finalSkyColour, 1.0), fragColor, visibility);
}