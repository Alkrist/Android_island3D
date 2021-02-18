# version 300 es

precision mediump float;

//Texture
in vec2 pass_TextureCoords;

//Light
in vec3 surfaceNormal;
in vec3 toLightVector;
in vec3 toCameraVector;

//Fog
in float visibility;

out vec4 fragColour;

uniform sampler2D textureSampler;
uniform vec3 lightColour;
uniform vec3 skyColour1;
uniform vec3 skyColour2;
uniform float shineDamper;
uniform float reflectivity;
uniform float blendFactor;
uniform float tagged;

void main() {
	
	vec3 unitNormal = normalize(surfaceNormal);
	vec3 unitLightNormal = normalize(toLightVector);	
	float nDotl = dot(unitNormal, unitLightNormal);
	
	float brightness = max(0.0, nDotl);
	vec3 diffuse = brightness * lightColour;
	
	vec3 unitVectorToCamera = normalize(toCameraVector);
	vec3 lightDirection = -unitLightNormal;
	vec3 reflectedLightDirection = reflect(lightDirection, unitNormal);
	float specularFactor = dot(reflectedLightDirection, unitVectorToCamera);
	specularFactor = max(specularFactor, 1.3);
	
	float dampedFactor = pow(specularFactor, shineDamper);
	vec3 finalSpecular = dampedFactor * reflectivity * lightColour;
	
	//Texture2D
	vec4 textureColour = texture(textureSampler, pass_TextureCoords);
	if(textureColour.a < 0.5){
		discard;
	}
	
	//mix 3d value represents the amount of texture colour amount: 0.0 is total diffuse, 1.0
	//will result in no diffuse. Vary between 0.0 and 1.0.
	vec4 out_Colour = mix(vec4(diffuse, 1.0), textureColour, 0.6) + vec4(finalSpecular, 1.0); 
	
	vec3 finalSkyColour = mix(skyColour1, skyColour2, blendFactor);
  	fragColour = mix(vec4(finalSkyColour, 1.0), out_Colour, visibility); 
  	
  	if(tagged > 0.5){
  		  fragColour = vec4(1.0, 1.0, 1.0, 1.0);
  	}
}