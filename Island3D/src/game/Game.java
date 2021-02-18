package game;


import com.test.AppMasterRenderer;

import android.content.Context;
import game.level.Level;
import renderEngine.Loader;


public class Game {
	private static final short TEAMS = 2;
	private Level level;
	private MoveBuffer buffer;
	
	private int totalGold;
	private int[] scores;
	private short stage = 0;
	private short currentTeam = 0;
	
	private float[] projectionMatrix;
	
	// **** START & INIT MODULE ****
	public Game(int size, int totalGold, float[] projection, Context context, Loader loader) {		
		scores = new int[TEAMS];
		buffer = new MoveBuffer();
		projectionMatrix = projection;
		//createNewLevel(size, totalGold, projection, context, loader);
	}
	
	public void createNewLevel(int size, int totalGold, Context context, Loader loader) {
		level = new Level(size, context, loader, projectionMatrix);
		buffer.cleanUp();
		for(int i=0; i<TEAMS; i++) {
			scores[i] = 0;
		}
		this.totalGold = totalGold;
	}
	
	// **** RUN MODULE ***
	//TODO: run module
	public void onClick(float position[]) {
		GameHandler.processMove(this, position);		
	}
	
	// **** FINISH MODULE ****
	public void onGameEnded() {
		AppMasterRenderer.endGame();
	}
		
	//**** OTHER ****
	
	public void increaseScore(short team) {
		if(team>= 0 && team<= scores.length-1)
			scores[team]++;
	}
	
	public void decreaseTotalGoldLeft() {
		totalGold--;
	}
	
	public void shuffleTeams() {
		if(currentTeam < TEAMS-1)
			currentTeam++;
		else currentTeam = 0;	
		buffer.cleanUp();
	}
	
	public void shuffleStage() {
		if(stage == 0)
			stage = 1;
		else stage = 0;		
	}
	
	public Level getLevel() {
		return level;
	}
	
	public short getStage() {
		return stage;
	}
	
	public short getTeam() {
		return currentTeam;
	}
	
	public void setProjectionMatrix(float[] projectionMatrix) {
		this.projectionMatrix = projectionMatrix;
		if(level!=null)
			level.setProjectionMatrix(projectionMatrix);
	}
	
	public MoveBuffer getBuffer() {
		return buffer;
	}

	public int getTotalGold() {
		return totalGold;
	}
	
	public int[] getScores() {
		return scores;
	}
}
