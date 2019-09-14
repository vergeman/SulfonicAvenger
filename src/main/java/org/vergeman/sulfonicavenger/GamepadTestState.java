package org.vergeman.sulfonicavenger;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

public class GamepadTestState extends BasicGameState {

	private int stateID;
	private Input input;

	public GamepadTestState(int id) {
		super();
		this.stateID = id;
	}

	@Override
	public void init(GameContainer container, StateBasedGame game)
			throws SlickException {
		// TODO Auto-generated method stub
		 input = container.getInput();
	}

	@Override
	public void render(GameContainer container, StateBasedGame game, Graphics g)
			throws SlickException {
		int num_controllers = input.getControllerCount();
		g.drawString("Detected " + String.valueOf(num_controllers) + " controllers", 500, 100);
		
		for (int i = 0; i < num_controllers; i++ ) {
			int num_axis = input.getAxisCount(i);
			
			g.drawString("Detected " + String.valueOf(num_axis) + " axes", 500, 200);
			
			for (int j = 0; j<num_axis; j++) {
				g.drawString("Axis #" + j + " :" + input.getAxisValue(i,  j), 500, 300+15*j);
			}
			
			
			
		}
	}

	@Override
	public void update(GameContainer container, StateBasedGame game, int delta)
			throws SlickException {
		// TODO Auto-generated method stub

		
	}

	@Override
	public int getID() {
		// TODO Auto-generated method stub
		return stateID;
	}
	

}
