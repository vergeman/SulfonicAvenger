package org.vergeman.sulfonicavenger;

import java.util.ArrayList;
import java.util.List;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

public class MyGame extends StateBasedGame {
	/* original dims from which we scale off of */
	private final static int WIDTH = 1024;
	private final static int HEIGHT = 768;

	public final static int GAMEMENUSTATE = 0;
	public final static int GAMEPLAYSTATE = 1;
	
	public static void main(String[] args) throws SlickException {

		try {

			AppGameContainer app = new AppGameContainer(new MyGame());

			app.setDisplayMode(WIDTH, HEIGHT, false);
			app.setResizable(true);
			app.start();
			
		} catch (SlickException e) {
			e.printStackTrace();
		}
	}

	public MyGame() {
		super("Sulfonic Avenger");
	}

	@Override
	public void initStatesList(GameContainer gc) throws SlickException {
		List<Score> high_scores = new ArrayList<Score>();
		LevelManager level_manager = new LevelManager();
		
		addState(new GameMenuState(GAMEMENUSTATE, high_scores, level_manager));
		
		addState(new GamePlayState(GAMEPLAYSTATE, high_scores, level_manager)); //test passing args
		
	}

}