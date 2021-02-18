package game.entities.actors;

import game.entities.Actor;
import renderEngine.models.Model;

public class Ship extends Actor{
	
	private int crew = 3;
	private short team;
	public Ship(short team, Model model, float posX, float posY, float posZ, float rotX, float rotY, float rotZ) {
		super(model, posX, posY, posZ, rotX, rotY, rotZ);
		this.setTeam(team);
	}

	@Override
	public void move(float[] destination) {
		super.getEntity().setPosition(destination[0],destination[1],destination[2]);
	}

	public int getCrew() {
		return crew;
	}

	public void setCrew(int crew) {
		this.crew = crew;
	}

	public void increaseCrew() {
		crew++;
	}
	
	public void decreaseCrew() {
		if(crew>0)
			crew--;
	}

	public short getTeam() {
		return team;
	}

	public void setTeam(short team) {
		this.team = team;
	}
}
