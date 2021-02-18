# version 300 es

precision mediump float;


in vec3 textureCoords;

out vec4 fragColor;

uniform samplerCube cubeMapDay;
uniform samplerCube cubeMapNight;
uniform float blendFactor;

uniform float fogBlendFactor;
uniform vec3 fogColour1;
uniform vec3 fogColour2;

const float lowerLimit = -40.0;
const float upperLimit = 20.0;

void main(){
	
    vec4 texture1 = texture(cubeMapDay, textureCoords);
    vec4 texture2 = texture(cubeMapNight, textureCoords);
    
    vec4 finalColour = mix(texture1, texture2, blendFactor);
    
    float factor = (textureCoords.y - lowerLimit) / (upperLimit - lowerLimit);
    factor = clamp(factor, 0.0, 1.0);
    
    vec3 finalFogColour = mix(fogColour1, fogColour2, fogBlendFactor);
    fragColor = mix(vec4(finalFogColour, 1.0), finalColour, factor);
}