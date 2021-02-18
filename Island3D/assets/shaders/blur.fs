# version 300 es

precision mediump float;

in vec2 blurTextureCoords[11];

out vec4 fragColour;

uniform sampler2D originalTexture;

void main(){
	
	fragColour = vec4(0.0);
	
    fragColour += texture(originalTexture, blurTextureCoords[0]) * 0.0093;
    fragColour += texture(originalTexture, blurTextureCoords[1]) * 0.028002;
    fragColour += texture(originalTexture, blurTextureCoords[2]) * 0.065984;
    fragColour += texture(originalTexture, blurTextureCoords[3]) * 0.121703;
    fragColour += texture(originalTexture, blurTextureCoords[4]) * 0.175713;
    fragColour += texture(originalTexture, blurTextureCoords[5]) * 0.198596;
    fragColour += texture(originalTexture, blurTextureCoords[6]) * 0.175713;
    fragColour += texture(originalTexture, blurTextureCoords[7]) * 0.121703;
    fragColour += texture(originalTexture, blurTextureCoords[8]) * 0.065984;
    fragColour += texture(originalTexture, blurTextureCoords[9]) * 0.028002;
    fragColour += texture(originalTexture, blurTextureCoords[10]) * 0.0093;
}